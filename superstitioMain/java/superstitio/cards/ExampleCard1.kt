package superstitio.cards

import basemod.AutoAdd
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.block.SexBlock
import superstitio.cards.lupa.LupaCard

@AutoAdd.Ignore
class ExampleCard : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    init {
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupBlock(BLOCK, UPGRADE_BLOCK, SexBlock())
        this.setupMagicNumber(MAGIC, UPGRADE_BLOCK)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(ExampleCard::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2
        private const val COST_UPGRADED_NEW = 10
        private const val DAMAGE = 2
        private const val UPGRADE_DAMAGE = 10
        private const val BLOCK = 24
        private const val UPGRADE_BLOCK = 10
        private const val MAGIC = 10
        private const val UPGRADE_MAGIC = 10
    }
}
