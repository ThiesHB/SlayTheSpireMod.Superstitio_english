package superstitioapi.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitioapi.utils.ActionUtility

abstract class AbstractContinuallyAction(actionType: ActionType?, duration: Float) : AbstractGameAction()
{
    init
    {
        this.actionType = actionType
        this.startDuration = duration
        this.duration = duration
    }

    protected open val isDoneCheck: Boolean
        get() = false

    /**
     * 在第一帧调用本函数
     *
     * 请不要在本函数的最开头写“是否怪物存活”一类的检测，因为在此之前就会调用 [ActionUtility.isNotInBattle]
     */
    protected abstract fun StartAction()

    /**
     * 不会在第一帧调用本函数
     *
     * 请不要在本函数的最开头写“是否怪物存活”一类的检测，因为在此之前就会调用 [ActionUtility.isNotInBattle]
     */
    protected abstract fun RunAction()

    /**
     * 在最后一帧调用本函数，注意，最后一帧 [.RunAction] 依旧会被调用
     *
     * 请不要在本函数的最开头写“是否怪物存活”一类的检测，因为在此之前就会调用 [ActionUtility.isNotInBattle]
     */
    protected abstract fun EndAction()

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
        if (isDoneCheck)
        {
            this.isDone = true
            return
        }
        if (ActionUtility.isNotInBattle)
        {
            this.isDone = true
            return
        }
        if (this.duration == this.startDuration) StartAction()
        else RunAction()
        this.tickDuration()
        if (isDone) EndAction()
    }
}
