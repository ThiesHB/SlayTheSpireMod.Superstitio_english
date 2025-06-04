package superstitio.cards.lupa.BaseCard

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.block.DrySemenBlock
import superstitio.cards.IsLupaCard
import superstitio.cards.lupa.LupaCard
import superstitioapi.cards.addRetainMod
@IsLupaCard
class DrySemen : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base")
{
    init
    {
        this.setupBlock(BLOCK, UPGRADE_BLOCK, DrySemenBlock())
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.addRetainMod()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        if (hasEnoughSemen(this.magicNumber))
        {
            addToBot_useSemenAndAutoRemove(this.magicNumber)
            addToBot_gainBlock()
        }
    }

    override fun canUse(p: AbstractPlayer?, m: AbstractMonster?): Boolean
    {
        return super.canUse(p, m) && hasEnoughSemen(this.magicNumber)
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(DrySemen::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.BASIC

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val BLOCK = 13
        private const val UPGRADE_BLOCK = 5
        private const val MAGIC = 3
        private const val UPGRADE_MAGIC = 0
    }
}

