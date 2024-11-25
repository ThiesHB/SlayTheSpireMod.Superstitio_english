package superstitio.powers

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.setDescriptionArgs

class SexToy(owner: AbstractCreature, amount: Int, sexToyName: String) :
    AbstractSuperstitioPower(POWER_ID, owner, amount, PowerType.BUFF, false) {
    var sexToyNames: MutableMap<String, Int>

    init {
        this.sexToyNames = HashMap()
        sexToyNames[sexToyName] = amount

        this.updateDescription()
    }

    fun Trigger() {
        this.flash()
        SexualHeat.addToBot_addSexualHeat(this.owner, this.amount / SEXUAL_HEAT_RATE)
    }

    override fun updateDescriptionArgs() {
        val SexToysString = arrayOf("")
        sexToyNames.forEach { (name: String?, num: Int) ->
            SexToysString[0] += String.format(
                powerStrings.getDESCRIPTION(1), name, num
            )
        }
        setDescriptionArgs(SEXUAL_HEAT_RATE, SexToysString[0])
    }

    override fun atStartOfTurn() {
        AutoDoneInstantAction.addToBotAbstract(this::Trigger)
        //        Trigger();
    }

    override fun onApplyPower(power: AbstractPower, target: AbstractCreature, source: AbstractCreature) {
        if (power !is SexToy) {
            return
        }
        this.sexToyNames = PowerUtility.mergeAndSumMaps(this.sexToyNames, power.sexToyNames)
        updateDescription()
    }

    companion object {
        val POWER_ID: String = DataManager.MakeTextID(SexToy::class.java)
        private const val SEXUAL_HEAT_RATE = 1
    }
}
