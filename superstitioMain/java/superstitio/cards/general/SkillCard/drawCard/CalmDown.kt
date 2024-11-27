package superstitio.cards.general.SkillCard.drawCard

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitioapi.hangUpCard.CardOrb
import superstitioapi.hangUpCard.Card_TriggerHangCardManually
import superstitioapi.hangUpCard.HangUpCardGroup

class CalmDown : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), Card_TriggerHangCardManually
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        HangUpCardGroup.forEachHangUpCard { _: HangUpCardGroup, cardOrb: CardOrb ->
            if (cardOrb.ifShouldRemove()) return@forEachHangUpCard
            cardOrb.setTriggerDiscardIfMoveToDiscard()
            cardOrb.setShouldRemove()
            addToBot_drawCards()
        }.addToBotAsAbstractAction()
        if (this.magicNumber >= 1) addToBot_drawCards(this.magicNumber)
    }

    override fun upgradeAuto()
    {
    }

    override fun forceFilterCardOrbToHoveredMode(orb: CardOrb): Boolean
    {
        orb.targetType = CardOrb.HangOnTarget.None
        orb.actionType = CardOrb.HangEffectType.Bad
        return true
    }

    override fun forceChangeOrbCounterShown(orb: CardOrb): Int
    {
        return 0
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(CalmDown::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 0
        private const val MAGIC = 0
        private const val UPGRADE_MAGIC = 1
        private const val ExtraDrawNum = 1


        private const val HeatReduce = 6
    }
}

