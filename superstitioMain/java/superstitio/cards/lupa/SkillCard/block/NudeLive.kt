package superstitio.cards.lupa.SkillCard.block

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.block.SexBlock
import superstitio.cards.lupa.LupaCard
import superstitio.delayHpLose.PreventHpLimit_Turns
import superstitioapi.cards.addExhaustMod

class NudeLive : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupBlock(BLOCK, UPGRADE_BLOCK, SexBlock())
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.addExhaustMod()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(PreventHpLimit_Turns(AbstractDungeon.player, this.magicNumber))
        addToBot_gainBlock()
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(NudeLive::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2
        private const val BLOCK = 10
        private const val UPGRADE_BLOCK = 6
        private const val MAGIC = 1
        private const val UPGRADE_MAGIC = 0
    }
}

