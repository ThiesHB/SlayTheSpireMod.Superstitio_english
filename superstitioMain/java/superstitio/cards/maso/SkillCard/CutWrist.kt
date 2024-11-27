package superstitio.cards.maso.SkillCard

import basemod.cardmods.RetainMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.actions.common.LoseHPAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.maso.MasoCard
import superstitio.delayHpLose.RemoveDelayHpLoseBlock

class CutWrist : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC)
        this.setupBlock(BLOCK, UPGRADE_BLOCK, RemoveDelayHpLoseBlock())
        CardModifierManager.addModifier(this, RetainMod())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot(LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber))
        addToBot_gainBlock()
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(CutWrist::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val MAGIC = 8
        private const val BLOCK = 18
        private const val UPGRADE_BLOCK = 4
    }
}
