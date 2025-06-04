package superstitio.cards.lupa.SkillCard.block

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.block.SexBlock
import superstitio.cards.IsLupaCard
import superstitio.cards.lupa.LupaCard
import superstitioapi.cards.addRetainMod
@IsLupaCard
class CoitalVocal : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupBlock(BLOCK, UPGRADE_BLOCK, SexBlock())
        this.addRetainMod()
    }

    override fun upgradeAuto()
    {
    }


    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val monsterNum = AbstractDungeon.getMonsters().monsters.stream()
            .filter { m: AbstractMonster -> !m.isDeadOrEscaped }.count().toInt()
        for (i in 0 until monsterNum + 1)
        {
            addToBot_gainBlock()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(CoitalVocal::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val BLOCK = 4
        private const val UPGRADE_BLOCK = 2
    }
}
