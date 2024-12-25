package superstitio.cards.general.SkillCard.hangUpCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitio.delayHpLose.RemoveDelayHpLoseBlock
import superstitioapi.hangUpCard.*

class PassiveGangBang : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), Card_TriggerHangCardManually
{
    init
    {
        this.setupBlock(BLOCK, UPGRADE_BLOCK, RemoveDelayHpLoseBlock())
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_gainBlock()
        val self: AbstractCard = this
        for (i in 0 until this.magicNumber)
        {
            HangUpCardGroup.forEachHangUpCard { orb: CardOrb ->
                if (orb is ICardOrb_WaitTime || orb is ICardOrb_EachTime)
                    orb.forceAcceptAction(self)
            }.addToBotAsAbstractAction()
        }
    }

    override fun upgradeAuto()
    {
    }

    override fun forceFilterCardOrbToHoveredMode(orb: CardOrb): Boolean
    {
        return orb is ICardOrb_WaitTime || orb is ICardOrb_EachTime
    }

    override fun forceChangeOrbCounterShown(orb: CardOrb): Int
    {
        return if (orb is ICardOrb_WaitTime || orb is ICardOrb_EachTime)
            orb.orbCounter.toInt { it - this.magicNumber }
        else
            orb.orbCounter.toInt()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(PassiveGangBang::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2
        private const val BLOCK = 14
        private const val UPGRADE_BLOCK = 4
        private const val MAGIC = 2
        private const val UPGRADE_MAGIC = 1
    }
}
