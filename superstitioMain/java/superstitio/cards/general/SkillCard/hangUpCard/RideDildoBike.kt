package superstitio.cards.general.SkillCard.hangUpCard

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
import superstitioapi.utils.CostSmart
import superstitioapi.utils.setDescriptionArgs

class RideDildoBike : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        //欲求数量
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    private fun HangUpSpecificCard(card: AbstractCard)
    {
        val copyCard = card.makeStatEquivalentCopy()
        //        copyCard.exhaust = true;
        val showUpCard = card.makeStatEquivalentCopy()
        showUpCard.cardsToPreview = copyCard
        AutoDoneInstantAction.addToBotAbstract {
            AbstractDungeon.player.hand.removeCard(card)
            CardOrb_WaitCardTrigger(
                card,
                AbstractDungeon.player.discardPile,
                CostSmart(this.magicNumber)
            ) { orb: CardOrb_CardTrigger, usedcard: AbstractCard? ->
                AutoDoneInstantAction.addToBotAbstract {
                    orb.cardHolder.moveToHand(card)
                    card.setCostForTurn(costForTurn - COST_SAVE)
                }
            }
                .setDiscardOnEndOfTurn()
                .setShowCard(showUpCard)
                .setCardRawDescriptionWillShow(String.format(cardStrings.getEXTENDED_DESCRIPTION(1), COST_SAVE))
                .addToBot_HangCard()
        }
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        ChoseCardFromHandCardSelectScreen { card: AbstractCard ->
            HangUpSpecificCard(card)
            addToBot_drawCards()
        }
            .setWindowText(String.format(cardStrings.getEXTENDED_DESCRIPTION(0), CAN_HANGUP, COST_SAVE))
            .setChoiceAmount(CAN_HANGUP)
            .setAnyNumber(true)
            .setCanPickZero(true)
            .addToBot()
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(CAN_HANGUP, COST_SAVE)
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(RideDildoBike::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val MAGIC = 5
        private const val UPGRADE_MAGIC = -2
        private const val CAN_HANGUP = 2
        private const val COST_SAVE = 1
    }
}
