package superstitio.cards.lupa.BaseCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.lupa.LupaCard
import superstitio.powers.SexualHeat
import superstitioapi.cards.patch.GoSomewhereElseAfterUse
import superstitioapi.hangUpCard.CardOrb_CardTrigger
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger
import superstitioapi.utils.CardUtility
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.setDescriptionArgs

class Masturbate : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base"), GoSomewhereElseAfterUse
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(WAIT, DRAWCard)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        SexualHeat.addToBot_addSexualHeat(AbstractDungeon.player, this.magicNumber)
    }

    override fun afterInterruptMoveToCardGroup(cardGroup: CardGroup)
    {
        CardOrb_WaitCardTrigger(
            this,
            cardGroup,
            CardUtility.CostSmart(WAIT)
        ) { orb: CardOrb_CardTrigger, playedCard: AbstractCard? ->
            orb.StartHitCreature(AbstractDungeon.player)
            addToBot_drawCards(DRAWCard)
            PowerUtility.BubbleMessage(orb.originCard.hb, false, cardStrings.getEXTENDED_DESCRIPTION(0))
        }
            .setDiscardOnEndOfTurn()
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(1), DRAWCard)
            .addToBot_HangCard()
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Masturbate::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.BASIC

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 0
        private const val MAGIC = 4
        private const val UPGRADE_MAGIC = 2
        private const val DRAWCard = 1

        private const val WAIT = 2
    }
}
