package superstitio.delayHpLose

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockInstance
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitio.DataManager
import superstitio.powers.AbstractSuperstitioPower
import superstitioapi.cardModifier.RenderAsBlockPower
import superstitioapi.cardModifier.RenderStackedBlockInstancesPatch
import superstitioapi.powers.interfaces.OnPostApplyThisPower
import superstitioapi.utils.addToBot_removeSelf
import superstitioapi.utils.setDescriptionArgs

class DelayRemoveDelayHpLosePower(owner: AbstractCreature, amount: Int) :
    AbstractSuperstitioPower(POWER_ID, owner, amount),
    RenderAsBlockPower, InvisiblePower,
    OnPostApplyThisPower<DelayRemoveDelayHpLosePower>
{
    override var _blockInstance: BlockInstance? = null

    private fun makeBlockInstance(): BlockInstance
    {
        val block = ArrayList<AbstractBlockModifier>()
        block.add(DelayRemoveDelayHpLoseBlock())
        return BlockInstance(owner, amount, block)
    }

    override fun atStartOfTurnPostDraw()
    {
        DelayHpLosePower.addToBot_removePower(
            DelayHpLosePower::class.java,
            amount,
            AbstractDungeon.player,
            true
        )
        addToBot_removeSelf()
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(this.amount)
    }

    override fun getBlockInstance(): BlockInstance
    {
        if (_blockInstance == null)
        {
            this._blockInstance = makeBlockInstance()
        }
        return _blockInstance!!
    }

    override fun InitializePostApplyThisPower(addedPower: DelayRemoveDelayHpLosePower)
    {
        this._blockInstance = makeBlockInstance()
        RenderStackedBlockInstancesPatch.BlockStackElementField.forceDrawBlock[owner] = true
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(DelayRemoveDelayHpLosePower::class.java)
    }
}
