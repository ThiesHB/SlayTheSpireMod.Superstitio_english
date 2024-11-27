package superstitio.cards.general.SkillCard.hangUpCard

import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.actions.ChoseCardFromHandCardSelectScreen
import superstitioapi.hangUpCard.CardOrb_CardTrigger
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger
import superstitioapi.utils.CardUtility

class ReadyToSex : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    private fun HangUpSpecificCard(card: AbstractCard)
    {
        val copyCard = card.makeStatEquivalentCopy()
        copyCard.exhaust = true
        val showUpCard = card.makeStatEquivalentCopy()
        showUpCard.cardsToPreview = copyCard
        AutoDoneInstantAction.addToBotAbstract {
            AbstractDungeon.player.hand.removeCard(card)
            CardOrb_WaitCardTrigger(
                card,
                AbstractDungeon.player.discardPile,
                CardUtility.CostSmart(magicNumber)
            ) { orb: CardOrb_CardTrigger?, usedcard: AbstractCard? ->
                addToBot(NewQueueCardAction(copyCard, true, false, true))
            }
                .setShowCard(showUpCard)
                .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(1))
                .setTriggerDiscardIfMoveToDiscard()
                .addToBot_HangCard()
        }
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        ChoseCardFromHandCardSelectScreen(this::HangUpSpecificCard)
            .setWindowText(String.format(cardStrings.getEXTENDED_DESCRIPTION(0), CHOSE_CARD))
            .setChoiceAmount(CHOSE_CARD)
            .addToBot()
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(ReadyToSex::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 0
        private const val MAGIC = 8
        private const val UPGRADE_MAGIC = -2
        private const val CHOSE_CARD = 1
        private const val WAIT_TIME = 3
    }
}
