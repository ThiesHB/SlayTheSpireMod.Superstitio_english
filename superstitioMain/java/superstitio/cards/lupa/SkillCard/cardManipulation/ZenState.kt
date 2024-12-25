package superstitio.cards.lupa.SkillCard.cardManipulation

import basemod.cardmods.ExhaustMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.lupa.LupaCard
import superstitio.delayHpLose.RemoveDelayHpLoseBlock
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.actions.ChoseCardFromHandCardSelectScreen
import superstitioapi.cards.addExhaustMod
import superstitioapi.utils.CardUtility
import java.util.function.Predicate

class ZenState : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC)
        this.setupBlock(BLOCK, UPGRADE_BLOCK, RemoveDelayHpLoseBlock())
    }

    fun addToBot_letSpecificCardExhaust(card: AbstractCard)
    {
        AutoDoneInstantAction.addToBotAbstract {
            card.superFlash()
            card.addExhaustMod()
        }
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_gainBlock()
        ChoseCardFromHandCardSelectScreen { card: AbstractCard ->
            addToBot_letSpecificCardExhaust(card)
            if (!CardUtility.canUseWithoutEnvironment(card)) addToBot(
                ExhaustSpecificCardAction(
                    card,
                    AbstractDungeon.player.hand
                )
            )
        }
            .setWindowText(String.format(cardStrings.getEXTENDED_DESCRIPTION(0), this.magicNumber))
            .setChoiceAmount(this.magicNumber)
            .setRetainFilter(
                Predicate { card: AbstractCard -> !card.exhaust },
                Predicate { card: AbstractCard -> !CardModifierManager.hasModifier(card, ExhaustMod.ID) })
            .addToBot()
    }

    override fun upgradeAuto()
    {
        this.upgradeBaseCost(COST_UPGRADED_NEW)
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(ZenState::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 0
        private const val COST_UPGRADED_NEW = 0
        private const val BLOCK = 4
        private const val UPGRADE_BLOCK = 2
        private const val MAGIC = 1
    }
}
