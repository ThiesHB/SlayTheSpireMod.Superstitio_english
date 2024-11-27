package superstitio.cardModifier.modifiers.block

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.cards.DamageInfo
import superstitio.DataManager
import superstitio.cardModifier.modifiers.AbstractLupaBlock
import superstitioapi.utils.setDescriptionArgs

class WhenLostDrawCardBlock @JvmOverloads constructor(private val drawAmount: Int = 1) : AbstractLupaBlock(ID)
{
    override fun amountLostAtStartOfTurn(): Int
    {
        return 0
    }

    override fun shouldStack(): Boolean
    {
        return false
    }

    override fun onRemove(lostByStartOfTurn: Boolean, info: DamageInfo?, remainingDamage: Int): Int
    {
        if (info != null)
        {
            addToBot(DrawCardAction(drawAmount))
        }
        return remainingDamage
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(this.drawAmount)
    }

    override fun priority(): Priority
    {
        return Priority.TOP
    }

    override fun subPriority(): Short
    {
        return 255
    }

    override fun makeCopy(): AbstractBlockModifier
    {
        return WhenLostDrawCardBlock()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(WhenLostDrawCardBlock::class.java)
    }
}