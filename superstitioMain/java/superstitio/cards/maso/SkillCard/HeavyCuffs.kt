package superstitio.cards.maso.SkillCard

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.block.WhenLostDrawCardBlock
import superstitio.cards.IsMasoCard
import superstitio.cards.maso.MasoCard
@IsMasoCard
class HeavyCuffs : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.setupBlock(BLOCK, UPGRADE_BLOCK, WhenLostDrawCardBlock(this.magicNumber))
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_gainBlock()
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(HeavyCuffs::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val MAGIC = 2
        private const val UPGRADE_MAGIC = 1
        private const val BLOCK = 8
        private const val UPGRADE_BLOCK = 3
    }
}
