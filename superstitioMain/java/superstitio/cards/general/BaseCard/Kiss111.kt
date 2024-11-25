package superstitio.cards.general.BaseCard

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitioapi.SuperstitioApiSetup

class Kiss : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base") {
    init {
        //添加基础攻击标签和将伤害设为6
        tags.add(CardTags.STARTER_STRIKE)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(Kiss::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.BASIC

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 1
        private const val DAMAGE = 6
        private const val UPGRADE_DAMAGE = 3
    }
}
