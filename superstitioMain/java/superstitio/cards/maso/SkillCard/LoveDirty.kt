package superstitio.cards.maso.SkillCard

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.IsMasoCard
import superstitio.cards.maso.MasoCard
import superstitio.delayHpLose.DelayRemoveDelayHpLoseBlock
import superstitioapi.cards.addExhaustMod
import superstitioapi.cards.addRetainMod

//恋污，被消耗或丢弃时触发
@IsMasoCard
class LoveDirty : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        //        this.setupMagicNumber(MAGIC);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, DelayRemoveDelayHpLoseBlock())
        this.addExhaustMod()
        this.addRetainMod()
    }

    private fun Trigger()
    {
        addToBot_gainBlock()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
    }

    override fun triggerOnManualDiscard()
    {
        Trigger()
    }

    override fun triggerOnExhaust()
    {
        Trigger()
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(LoveDirty::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1

        //    private static final int MAGIC = 8;
        private const val BLOCK = 12
        private const val UPGRADE_BLOCK = 4
    }
}
