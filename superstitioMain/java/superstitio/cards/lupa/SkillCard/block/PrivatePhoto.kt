package superstitio.cards.lupa.SkillCard.block

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.block.SexBlock
import superstitio.cards.lupa.LupaCard
import superstitio.delayHpLose.PreventHpLimit_Times

class PrivatePhoto : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    init {
        this.setupBlock(BLOCK, UPGRADE_BLOCK, SexBlock())
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        addToBot_applyPower(PreventHpLimit_Times(AbstractDungeon.player, this.magicNumber))
        addToBot_gainBlock()
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(PrivatePhoto::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val BLOCK = 4
        private const val UPGRADE_BLOCK = 4
        private const val MAGIC = 2
        private const val UPGRADE_MAGIC = 1
    }
}

