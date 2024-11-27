package superstitio.delayHpLose

import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitio.delayHpLose.DelayHpLosePower_ApplyOnAttacked.IPreventHpLimit
import superstitio.powers.AbstractSuperstitioPower
import superstitioapi.utils.setDescriptionArgs

class PreventHpLimit_Turns(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(POWER_ID, owner, amount),
    IPreventHpLimit
{
    override fun updateDescriptionArgs()
    {
        this.setDescriptionArgs(amount)
    }

    override fun atEndOfRound()
    {
        addToBot_AutoRemoveOne(this)
    }

    override fun onPreventHpLimit()
    {
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(PreventHpLimit_Turns::class.java)
    }
}
