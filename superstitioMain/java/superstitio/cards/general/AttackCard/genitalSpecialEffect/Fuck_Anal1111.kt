package superstitio.cards.general.AttackCard.genitalSpecialEffect

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.general.GeneralCard
import superstitio.powers.SexualDamage
import superstitioapi.SuperstitioApiSetup
import superstitioapi.actions.AutoDoneInstantAction


class Fuck_Anal : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card {
    init {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun upgradeAuto() {
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        addToBot_dealDamage(monster!!, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
        AutoDoneInstantAction.addToBotAbstract {
            monster.powers.filterIsInstance<SexualDamage>()
                .firstOrNull()
                ?.let { sexualDamage: SexualDamage ->
                    addToBot_applyPower(
                        SexualDamage(monster,
                            (sexualDamage.amount * DAMAGE_RATE * this.magicNumber).toInt(),
                            AbstractDungeon.player
                        )
                    )
                }
        }
    }

    companion object {
        val ID: String = DataManager.MakeTextID(Fuck_Anal::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY
        private const val DAMAGE_RATE = 0.1f
        private const val COST = 1
        private const val DAMAGE = 6
        private const val UPGRADE_DAMAGE = 2
        private const val MAGIC = 4
        private const val UPGRADE_MAGIC = 2
    }
}
