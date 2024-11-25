package superstitio.cards.general.AttackCard.genitalSpecialEffect

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.general.GeneralCard
import superstitioapi.SuperstitioApiSetup
import superstitioapi.hangUpCard.*


class Fuck_Vaginal : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card,
    Card_TriggerHangCardManually {
    init {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC)
    }

    override fun upgradeAuto() {
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
        for (i in 0 until this.magicNumber) {
            HangUpCardGroup.forEachHangUpCard { orb: CardOrb ->
                if (orb is ICardOrb_EachTime) {
                    orb.orbCounter++
                    orb.forceAcceptAction(this)
                }
                if (orb is ICardOrb_WaitTime) orb.forceAcceptAction(this)
            }.addToBotAsAbstractAction()
        }
    }

    override fun forceFilterCardOrbToHoveredMode(orb: CardOrb): Boolean {
        if (orb is ICardOrb_EachTime) return true
        if (orb is ICardOrb_WaitTime && orb is CardOrb_CardTrigger) return (orb as CardOrb_CardTrigger).cardMatcher.test(
            this
        )
        return false
    }

    override fun forceChangeOrbCounterShown(orb: CardOrb): Int {
        if (orb is ICardOrb_WaitTime && orb is CardOrb_CardTrigger) {
            if ((orb as CardOrb_CardTrigger).cardMatcher.test(this)) {
                return orb.orbCounter - 1
            }
        }
        return orb.orbCounter
    }

    companion object {
        val ID: String = DataManager.MakeTextID(Fuck_Vaginal::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 1
        private const val DAMAGE = 8
        private const val UPGRADE_DAMAGE = 4
        private const val MAGIC = 1
    }
}
