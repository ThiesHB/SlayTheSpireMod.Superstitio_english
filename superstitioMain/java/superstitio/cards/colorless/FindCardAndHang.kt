package superstitio.cards.colorless

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.SuperstitioCard
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.actions.ChoseCardFromGridSelectWindowAction
import superstitioapi.cards.addExhaustMod
import superstitioapi.hangUpCard.CardOrb_CardTrigger
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger
import superstitioapi.utils.CostSmart

class FindCardAndHang : SuperstitioCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, CardColor.COLORLESS, "special")
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.addExhaustMod()
    }

    private fun HangUpSpecificCard(card: AbstractCard)
    {
        val copyCard = card.makeStatEquivalentCopy()
        copyCard.exhaust = true
        val showUpCard = card.makeStatEquivalentCopy()
        showUpCard.cardsToPreview = copyCard
        AutoDoneInstantAction.addToBotAbstract {
            AbstractDungeon.player.drawPile.removeCard(card)
            CardOrb_WaitCardTrigger(
                card,
                AbstractDungeon.player.hand,
                CostSmart(magicNumber)
            ) { orb: CardOrb_CardTrigger?, usedcard: AbstractCard? -> }
                .setShowCard(showUpCard)
                .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(1))
                .addToBot_HangCard()
        }
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        ChoseCardFromGridSelectWindowAction(AbstractDungeon.player.drawPile, this::HangUpSpecificCard)
            .setWindowText(String.format(cardStrings.getEXTENDED_DESCRIPTION(0), CHOSE_CARD))
            .setChoseAmount(CHOSE_CARD)
            .setAnyNumber(true)
            .addToBot()
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(FindCardAndHang::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val MAGIC = 10
        private const val UPGRADE_MAGIC = -4
        private const val CHOSE_CARD = 10
        private const val WAIT_TIME = 3
    }
}
