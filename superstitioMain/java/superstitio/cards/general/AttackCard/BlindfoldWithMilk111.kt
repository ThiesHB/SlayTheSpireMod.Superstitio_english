package superstitio.cards.general.AttackCard

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitio.powers.Milk
import superstitioapi.SuperstitioApiSetup
import superstitioapi.actions.AutoDoneInstantAction

class BlindfoldWithMilk : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    init {
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        addToBot_dealDamage(monster!!, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
        if (monster.isDeadOrEscaped) return
        AutoDoneInstantAction.addToBotAbstract {
            monster.rollMove()
            monster.createIntent()
        }
        addToBot_applyPower(Milk(monster, magicNumber))
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(BlindfoldWithMilk::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 2
        private const val DAMAGE = 20
        private const val UPGRADE_DAMAGE = 8
        private const val MAGIC = 6
    }
}
