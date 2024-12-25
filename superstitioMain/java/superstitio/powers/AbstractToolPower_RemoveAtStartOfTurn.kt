package superstitio.powers

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower
import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.addToBot_removeSelf

abstract class AbstractToolPower_RemoveAtStartOfTurn(owner: AbstractCreature) :
    AbstractSuperstitioPower(POWER_ID, owner, -1), NonStackablePower, InvisiblePower
{
    protected abstract fun atStartOfTurnAction()

    override fun updateDescriptionArgs()
    {
    }

    override fun atStartOfTurn()
    {
        AutoDoneInstantAction.addToBotAbstract(this::atStartOfTurnAction)
        this.addToBot_removeSelf()
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(SexualHeat::class.java)
    }
}
