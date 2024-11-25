package superstitio.cards.maso.AttackCard

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.maso.MasoCard
import superstitio.powers.Milk
import superstitioapi.SuperstitioApiSetup
import superstitioapi.utils.CreatureUtility
import superstitioapi.utils.setDescriptionArgs

class Fuck_Nipple : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card {
    init {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        for (i in 0 until DAMAGE_TIME) {
            val randomMonster = CreatureUtility.getRandomMonsterSafe()
            addToBot_dealDamage(randomMonster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
            addToBot_applyPower(Milk(randomMonster, this.magicNumber))
        }
    }

    override fun updateDescriptionArgs() {
        setDescriptionArgs(DAMAGE_TIME)
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(Fuck_Nipple::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.ALL_ENEMY

        private const val COST = 1
        private const val DAMAGE = 5
        private const val UPGRADE_DAMAGE = 2
        private const val MAGIC = 2
        private const val DAMAGE_TIME = 2
    }
}
