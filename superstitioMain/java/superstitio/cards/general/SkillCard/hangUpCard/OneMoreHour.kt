package superstitio.cards.general.SkillCard.hangUpCard

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitio.delayHpLose.RemoveDelayHpLoseBlock
import superstitioapi.hangUpCard.CardOrb
import superstitioapi.hangUpCard.Card_TriggerHangCardManually
import superstitioapi.hangUpCard.HangUpCardGroup

class OneMoreHour : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), Card_TriggerHangCardManually
{
    init
    {
        this.setupBlock(BLOCK, UPGRADE_BLOCK, RemoveDelayHpLoseBlock())
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_gainBlock()
        //        for (int i = 0; i < this.magicNumber; i++) {
        HangUpCardGroup.forEachHangUpCard { it.orbCounter += this.magicNumber }
            .addToBotAsAbstractAction()
        //        }
    }

    override fun upgradeAuto()
    {
    }

    override fun forceFilterCardOrbToHoveredMode(orb: CardOrb): Boolean
    {
        orb.targetType = CardOrb.HangOnTarget.None
        orb.actionType = CardOrb.HangEffectType.Good
        return true
    }

    override fun forceChangeOrbCounterShown(orb: CardOrb): Int
    {
        return orb.orbCounter.toInt { it + this.magicNumber }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(OneMoreHour::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val BLOCK = 7
        private const val UPGRADE_BLOCK = 3
        private const val MAGIC = 3
        private const val UPGRADE_MAGIC = 1
    }
}
