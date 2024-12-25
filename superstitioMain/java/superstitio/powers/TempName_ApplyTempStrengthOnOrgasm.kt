package superstitio.powers

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.LoseStrengthPower
import com.megacrit.cardcrawl.powers.StrengthPower
import superstitio.DataManager
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm
import superstitioapi.utils.addToBot_applyPower

class TempName_ApplyTempStrengthOnOrgasm(owner: AbstractCreature, amount: Int) :
    AbstractSuperstitioPower(POWER_ID, owner, amount), OnOrgasm_onOrgasm
{
    override fun onOrgasm(SexualHeatPower: SexualHeat)
    {
        addToBot_applyPower(StrengthPower(this.owner, this.amount))
        addToBot_applyPower(LoseStrengthPower(this.owner, this.amount))
    }

    override fun updateDescriptionArgs()
    {
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(TempName_ApplyTempStrengthOnOrgasm::class.java)
    }
}
