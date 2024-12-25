package superstitio.delayHpLose

import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitioapi.utils.addToBot_reducePowerToOwner

class DelayHpLosePower_ApplyOnAttacked(owner: AbstractCreature, amount: Int) :
    DelayHpLosePower(POWER_ID, owner, amount)
{
    override fun checkShouldInvisibleTips(): Boolean
    {
        return false
    }

    override fun wasHPLost(info: DamageInfo, damageAmount: Int)
    {
        if (info.type != DamageType.NORMAL)
            return
        val preventHpLimit =
            owner.powers.firstOrNull { power: AbstractPower? -> power is IPreventHpLimit }
        if (preventHpLimit == null)
        {
            immediate_applyDamage(this)
            return
        }
        if (preventHpLimit.amount == 0)
        {
            immediate_applyDamage(this)
            return
        }
        preventHpLimit.let { (it as IPreventHpLimit).onPreventHpLimit() }
        return
    }


    override fun onVictory()
    {
//        immediate_applyDamage(this);
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

    interface IPreventHpLimit
    {
        fun onPreventHpLimit()
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(DelayHpLosePower_ApplyOnAttacked::class.java)
    }
}
