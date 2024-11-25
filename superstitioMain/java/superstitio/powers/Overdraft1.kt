package superstitio.powers

import com.megacrit.cardcrawl.actions.common.ExhaustAction
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitioapi.utils.setDescriptionArgs

class Overdraft(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(POWER_ID, owner, amount) {
    override fun updateDescriptionArgs() {
        setDescriptionArgs(amount)
    }


    override fun atStartOfTurnPostDraw() {
        this.flash()
        this.addToBot(ExhaustAction(amount, false, false))
        this.addToBot(RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID))
    }

    companion object {
        val POWER_ID: String = DataManager.MakeTextID(Overdraft::class.java)
    }
}
