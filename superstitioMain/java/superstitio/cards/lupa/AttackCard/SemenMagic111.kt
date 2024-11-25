package superstitio.cards.lupa.AttackCard

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.lupa.LupaCard

class SemenMagic : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    init {
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        if (!hasEnoughSemen(this.magicNumber)) return
        addToBot_dealDamage(monster, AttackEffect.LIGHTNING)
    }

    override fun canUse(p: AbstractPlayer, m: AbstractMonster): Boolean {
        return super.canUse(p, m) && hasEnoughSemen(this.magicNumber)
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(SemenMagic::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 1
        private const val DAMAGE = 18
        private const val UPGRADE_DAMAGE = 6

        private const val MAGIC = 4
    }
}
