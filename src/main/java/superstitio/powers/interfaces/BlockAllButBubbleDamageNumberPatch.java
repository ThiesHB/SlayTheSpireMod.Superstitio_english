package superstitio.powers.interfaces;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import superstitio.utils.CardUtility;

@SpirePatch(clz = AbstractPlayer.class, method = "damage")
public class BlockAllButBubbleDamageNumberPatch {
    public static ExprEditor Instrument() {
        return new ExprEditor() {
            private int count = 0;

            public void edit(final FieldAccess m) throws CannotCompileException {
                if (!m.getFieldName().equals("currentHealth") || !m.isWriter()) return;
                if (this.count <= 0) {
                    m.replace("if( " + BlockAllButBubbleDamageNumberPatch.class.getName() + ".isImmunity( $0 , info,damageAmount )  )" +
                            "{$0.currentHealth = " + BlockAllButBubbleDamageNumberPatch.class.getName() + ".ForceSetCurrentHealth( $0 , info ) ;}" +
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
        return IsImmunityFields.checkShouldImmunity.get(abstractPlayer).test(abstractPlayer, info, damageAmount);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "<class>")
    public static class IsImmunityFields {
        public static SpireField<TriPredicate<AbstractPlayer, DamageInfo, Integer>> checkShouldImmunity;

        static {
            IsImmunityFields.checkShouldImmunity = (SpireField<TriPredicate<AbstractPlayer, DamageInfo, Integer>>) new SpireField(() -> null);
        }
    }
}
