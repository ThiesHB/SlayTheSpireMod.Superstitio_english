package superstitioapi.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.ActionUtility.VoidSupplier

abstract class AutoDoneInstantAction : AbstractGameAction()
{
    abstract fun autoDoneUpdate()

    fun addToBot()
    {
        AbstractDungeon.actionManager.addToBottom(this)
    }

    fun addToTop()
    {
        AbstractDungeon.actionManager.addToTop(this)
    }

    override fun update()
    {
        this.isDone = true
        if (ActionUtility.isNotInBattle) return
        autoDoneUpdate()
    }

    companion object
    {
        fun addToBotAbstract(func: VoidSupplier)
        {
            newAutoDone(func).addToBot()
        }

        fun addToBotAbstract(func: VoidSupplier, time: Int)
        {
            if (time <= 1) AbstractDungeon.actionManager.addToBottom(newAutoDone(func))
            else addToBotAbstract { addToBotAbstract(func, time - 1) }
        }

        fun addToTopAbstract(func: VoidSupplier)
        {
            newAutoDone(func).addToTop()
        }

        fun newAutoDone(func: VoidSupplier): AutoDoneInstantAction
        {
            return object : AutoDoneInstantAction()
            {
                override fun autoDoneUpdate()
                {
                    func.get()
                }
            }
        }
    }
}
