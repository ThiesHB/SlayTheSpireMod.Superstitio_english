package superstitio.delayHpLose

import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitio.delayHpLose.DelayHpLosePower_ApplyOnAttacked.IPreventHpLimit
import superstitio.powers.AbstractSuperstitioPower
import superstitioapi.utils.setDescriptionArgs

class PreventHpLimit_Times(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(POWER_ID, owner, amount),
    IPreventHpLimit {
    override fun updateDescriptionArgs() {
        this.setDescriptionArgs(amount)
    }

    override fun onPreventHpLimit() {
        addToTop(ReducePowerAction(this.owner, this.owner, this, 1))
    }

    override fun atEndOfRound() {
        addToBot_removeSpecificPower(this)
    }

    companion object {
        val POWER_ID: String = DataManager.MakeTextID(PreventHpLimit_Times::class.java)
    }
}
