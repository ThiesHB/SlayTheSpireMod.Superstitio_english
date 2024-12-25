package superstitioapi.cards.patch

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import javassist.CannotCompileException
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.hangUpCard.CardOrb
import superstitioapi.utils.ActionUtility.VoidSupplier

@SpirePatch2(clz = UseCardAction::class, method = "update")
object GoSomewhereElseAfterUsePatch
{
    @SpireInstrumentPatch
    @JvmStatic
    fun Instrument(): ExprEditor
    {
        return object : ExprEditor()
        {
            @Throws(CannotCompileException::class)
            override fun edit(m: MethodCall)
            {
                if (m.className == CardGroup::class.qualifiedName && m.methodName == "moveToDiscardPile")
                {
                    m.replace(
                        "if (" + GoSomewhereElseAfterUsePatch::class.qualifiedName + ".Do($1,"
                                + AbstractDungeon::class.qualifiedName +
                                ".player.discardPile)) " +
                                "{\$_ = \$proceed($$);}"
                    )
                }
                else if (m.className == CardGroup::class.qualifiedName && m.methodName == "moveToExhaustPile")
                {
                    m.replace(
                        "if (" + GoSomewhereElseAfterUsePatch::class.qualifiedName + ".Do($1,"
                                + AbstractDungeon::class.qualifiedName + ".player.exhaustPile))" +
                                " {\$_ = \$proceed($$);}"
                    )
                }
                else if (m.className == CardGroup::class.qualifiedName && m.methodName == "moveToDeck")
                {
                    m.replace(
                        "if (" + GoSomewhereElseAfterUsePatch::class.qualifiedName + ".Do($1,"
                                + AbstractDungeon::class.qualifiedName + ".player.drawPile))" +
                                " {\$_ = \$proceed($$);}"
                    )
                }
                else if (m.className == CardGroup::class.qualifiedName && m.methodName == "moveToHand")
                {
                    m.replace(
                        "if (" + GoSomewhereElseAfterUsePatch::class.qualifiedName + ".Do($1,"
                                + AbstractDungeon::class.qualifiedName + ".player.hand)) " +
                                "{\$_ = \$proceed($$);}"
                    )
                }
            }
        }
    }

    @JvmStatic
    fun Do(card: AbstractCard, cardGroup: CardGroup): Boolean
    {
        if (card !is GoSomewhereElseAfterUse) return true
        if (card.purgeOnUse) return true
        card.targetDrawScale = CardOrb.DRAW_SCALE_SMALL
        AbstractDungeon.player.limbo.addToTop(card)
        AutoDoneInstantAction.addToBotAbstract {
            (card as GoSomewhereElseAfterUse).afterInterruptMoveToCardGroup(cardGroup)
        }
        AutoDoneInstantAction.addToBotAbstract { AbstractDungeon.player.limbo.removeCard(card) }
        return false
    }
}
