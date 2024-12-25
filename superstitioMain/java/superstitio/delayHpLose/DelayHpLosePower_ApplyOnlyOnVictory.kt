package superstitio.delayHpLose

import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitioapi.utils.addToBot_reducePowerToOwner

class DelayHpLosePower_ApplyOnlyOnVictory(owner: AbstractCreature, amount: Int) :
    DelayHpLosePower(POWER_ID, owner, amount)
{
    override fun checkShouldInvisibleTips(): Boolean
    {
        return false
    }

    override fun onVictory()
    {
        immediate_applyDamage(this)
    }

    override fun showDecreaseAmount(): Boolean
    {
        return true
    }

    override fun addToBot_removeDelayHpLoss(amount: Int, removeOther: Boolean): Int
    {
        val lastAmount = amount - this.amount
        addToBot_reducePowerToOwner(this.ID, amount)
        return lastAmount
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(DelayHpLosePower_ApplyOnlyOnVictory::class.java)
    }
}
