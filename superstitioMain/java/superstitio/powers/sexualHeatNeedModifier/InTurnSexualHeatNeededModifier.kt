package superstitio.powers.sexualHeatNeedModifier

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitio.powers.AbstractSuperstitioPower
import superstitio.powers.SexualHeat
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.powers.interfaces.OnPostApplyThisPower
import superstitioapi.utils.setDescriptionArgs
import kotlin.math.abs

class InTurnSexualHeatNeededModifier(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(
    POWER_ID, owner, amount, if (owner is AbstractPlayer
    ) if (amount < 0) PowerType.DEBUFF else PowerType.BUFF
    else if (amount < 0) PowerType.BUFF else PowerType.DEBUFF
), SexualHeatNeedModifier, OnPostApplyThisPower<InTurnSexualHeatNeededModifier>
{
    init
    {
        //大于0会降低高潮需求，小于0会提高高潮需求
        this.canGoNegative = true
    }

    override fun atEndOfTurn(isPlayer: Boolean)
    {
        this.addToBot(RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID))
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(abs(amount.toDouble()))
    }

    override fun getDescriptionStrings(): String
    {
        return powerStrings.getDESCRIPTION(if (this.amount < 0) 1 else 0)
    }

    //    @Override
    //    public void onCheckOrgasm(SexualHeat SexualHeatPower) {
    //        SexualHeatPower.setHeatRequired(SexualHeat.HEAT_REQUIREDOrigin - this.amount);
    //    }
    override fun InitializePostApplyThisPower(addedPower: InTurnSexualHeatNeededModifier)
    {
//        AutoDoneInstantAction.addToBotAbstract(() ->
//                SexualHeat.getActiveSexualHeat(owner)
//                        .ifPresent(SexualHeatPower -> SexualHeatPower.setHeatRequired(SexualHeat.HEAT_REQUIREDOrigin - this.amount)));
        AutoDoneInstantAction.addToBotAbstract {
            SexualHeat.getActiveSexualHeat(owner)?.let { sexualHeat: SexualHeat ->
                sexualHeat.CheckOrgasm()
                sexualHeat.updateDescription()
            }
        }
    }

    override fun reduceSexualHeatNeeded(): Int
    {
        return this.amount
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(InTurnSexualHeatNeededModifier::class.java)
    }
}
