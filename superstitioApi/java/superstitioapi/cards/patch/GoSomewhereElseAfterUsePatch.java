package superstitioapi.cards.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.hangUpCard.CardOrb_CardTrigger;

@SpirePatch(clz = UseCardAction.class, method = "update")
public class GoSomewhereElseAfterUsePatch {
    public static ExprEditor Instrument() {
        return new ExprEditor() {
            public void edit(final MethodCall m) throws CannotCompileException {
                if (m.getClassName().equals(CardGroup.class.getName()) && m.getMethodName().equals("moveToDiscardPile")) {
                    m.replace("if (" + GoSomewhereElseAfterUsePatch.class.getName() + ".Do($1,"
                            + AbstractDungeon.class.getName() +
                            ".player.discardPile)) " +
                            "{$_ = $proceed($$);}");
                } else if (m.getClassName().equals(CardGroup.class.getName()) && m.getMethodName().equals("moveToExhaustPile")) {
                    m.replace("if (" + GoSomewhereElseAfterUsePatch.class.getName() + ".Do($1,"
                            + AbstractDungeon.class.getName() + ".player.exhaustPile))" +
                            " {$_ = $proceed($$);}");
                } else if (m.getClassName().equals(CardGroup.class.getName()) && m.getMethodName().equals("moveToDeck")) {
                    m.replace("if (" + GoSomewhereElseAfterUsePatch.class.getName() + ".Do($1,"
                            + AbstractDungeon.class.getName() + ".player.drawPile))" +
                            " {$_ = $proceed($$);}");
                } else if (m.getClassName().equals(CardGroup.class.getName()) && m.getMethodName().equals("moveToHand")) {
                    m.replace("if (" + GoSomewhereElseAfterUsePatch.class.getName() + ".Do($1,"
                            + AbstractDungeon.class.getName() + ".player.hand)) " +
                            "{$_ = $proceed($$);}");
                }
            }
        };
    }

    public static boolean Do(final AbstractCard card, final CardGroup cardGroup) {
        if (!(card instanceof GoSomewhereElseAfterUse)) return true;
        if (card.purgeOnUse) return true;
        card.targetDrawScale = CardOrb_CardTrigger.DRAW_SCALE_SMALL;
        AbstractDungeon.player.limbo.addToTop(card);
        AutoDoneInstantAction.addToBotAbstract(() ->
                ((GoSomewhereElseAfterUse) card).afterInterruptMoveToCardGroup(cardGroup));
        AutoDoneInstantAction.addToBotAbstract(() ->
                AbstractDungeon.player.limbo.removeCard(card));
        return false;
    }
}
