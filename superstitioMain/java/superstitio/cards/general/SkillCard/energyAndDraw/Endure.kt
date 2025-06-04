package superstitio.cards.general.SkillCard.energyAndDraw

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.block.SexBlock
import superstitio.cards.NormalCard
import superstitio.powers.DelaySexualHeat

class Endure : NormalCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.setupBlock(BLOCK, UPGRADE_BLOCK, SexBlock())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_gainBlock()
        addToBot_applyPower(DelaySexualHeat(AbstractDungeon.player, this.magicNumber))
        addToBot_drawCards(DRAWCard_NUM)
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Endure::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val MAGIC = 10
        private const val UPGRADE_MAGIC = 5
        private const val BLOCK = 5
        private const val UPGRADE_BLOCK = 0
        private const val DRAWCard_NUM = 2
    }
}
