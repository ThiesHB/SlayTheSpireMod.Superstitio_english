package superstitio.delayHpLose;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import superstitio.powers.interfaces.TriPredicate;
import superstitio.utils.CardUtility;

@SpirePatch(clz = AbstractPlayer.class, method = "damage")
public class DelayHpLosePatch {
    public static ExprEditor Instrument() {
        return new ExprEditor() {
            private int count = 0;

            public void edit(final FieldAccess m) throws CannotCompileException {
                if (!m.getFieldName().equals("currentHealth") || !m.isWriter()) return;
                if (this.count <= 0) {
                    m.replace("if( " + DelayHpLosePatch.class.getName() + ".isImmunity( $0 , info,damageAmount )  )" +
                            "{$0.currentHealth = " + DelayHpLosePatch.class.getName() + ".ForceSetCurrentHealth( $0 , info ) ;}" +
                            "else { $proceed($$) ;}");
                }

                ++this.count;
            }
        };
    }

    public static int ForceSetCurrentHealth(AbstractPlayer abstractPlayer, DamageInfo info) {
        return abstractPlayer.currentHealth;
    }

    public static boolean isImmunity(AbstractPlayer abstractPlayer, DamageInfo info, int damageAmount) {
        if (CardUtility.isNotInBattle()) return false;
        TriPredicate<AbstractPlayer, DamageInfo, Integer> predicate =
                IsImmunityFields.checkShouldImmunity.get(abstractPlayer);
        if (predicate == null) return false;
        return predicate.test(abstractPlayer, info, damageAmount);

    }

    @SpirePatch(clz = AbstractPlayer.class, method = "<class>")
    public static class IsImmunityFields {
        public static SpireField<TriPredicate<AbstractPlayer, DamageInfo, Integer>> checkShouldImmunity =
                (SpireField<TriPredicate<AbstractPlayer, DamageInfo, Integer>>) new SpireField(() -> null);

    }

    @SpirePatch(clz = AbstractCard.class, method = "<class>")
    public static class GainBlockTypeFields {
        public static SpireField<Boolean> ifTransGainBlockToReduceDelayHpLose =
                (SpireField<Boolean>) new SpireField(() -> false);

    }
}
