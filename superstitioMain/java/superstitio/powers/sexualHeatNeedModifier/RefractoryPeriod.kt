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

class RefractoryPeriod(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(
    POWER_ID, owner, amount, if (owner is AbstractPlayer
    ) if (amount < 0) PowerType.BUFF else PowerType.DEBUFF
    else if (amount < 0) PowerType.DEBUFF else PowerType.BUFF
), SexualHeatNeedModifier, OnPostApplyThisPower<RefractoryPeriod>
{
    private var isAddedThisTurn = true

    init
    {
        //大于0会提高高潮需求，小于0会降低高潮需求
        this.canGoNegative = true
    }

    override fun onRemove()
    {
        super.onRemove()
        AutoDoneInstantAction.addToBotAbstract {
            SexualHeat.getActiveSexualHeat(owner)?.let { sexualHeat: SexualHeat ->
                sexualHeat.CheckOrgasm()
                sexualHeat.updateDescription()
            }
        }
    }

    //    @Override
    //    public void atStartOfTurn() {
    //
    //    }
    override fun atEndOfTurn(isPlayer: Boolean)
    {
        if (isAddedThisTurn)
        {
            isAddedThisTurn = false
            return
        }
        this.addToBot(RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID))
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(abs(amount))
    }

    override fun getDescriptionStrings(): String
    {
        return powerStrings.getDESCRIPTION(if (isAddedThisTurn) 0 else 1)
    }

    override fun InitializePostApplyThisPower(addedPower: RefractoryPeriod)
    {
        AutoDoneInstantAction.addToBotAbstract { addedPower.isAddedThisTurn = true }
        AutoDoneInstantAction.addToBotAbstract {
            SexualHeat.getActiveSexualHeat(owner)?.let { sexualHeat: SexualHeat ->
                sexualHeat.CheckOrgasm()
                sexualHeat.updateDescription()
            }
        }
    }

    override fun reduceSexualHeatNeeded(): Int
    {
        return this.amount * -1
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(RefractoryPeriod::class.java)
    }
}
