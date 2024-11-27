package superstitio.powers

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitioapi.utils.setDescriptionArgs

class DelaySexualHeat(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(POWER_ID, owner, amount) {
    override fun updateDescriptionArgs() {
        this.setDescriptionArgs(amount)
    }


    override fun atStartOfTurn() {
        this.flash()
        SexualHeat.addToBot_addSexualHeat(this.owner, amount)
        this.addToBot(RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID))
    }

    companion object {
        val POWER_ID: String = DataManager.MakeTextID(DelaySexualHeat::class.java)
    }
}
