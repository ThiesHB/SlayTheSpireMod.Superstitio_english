package superstitio.orbs

import superstitio.orbs.orbgroup.SexMarkOrbGroup
import superstitioapi.utils.setDescriptionArgs

abstract class SexMarkOrb(id: String, amount: Int, var sexMarkName: String) :
    AbstractLupaOrb(id, amount, amount, false) {
    init {
        this.name = String.format(orbStringsSet.getNAME(), sexMarkName)
        this.updateDescription()
    }

    fun setSexMarkName(sexMarkName: String): SexMarkOrb {
        this.sexMarkName = sexMarkName
        this.name = String.format(orbStringsSet.getNAME(), sexMarkName)
        this.updateDescription()
        return this
    }

    abstract fun attack(): Int

    abstract fun block(): Int

    override fun updateDescriptionArgs() {
        setDescriptionArgs(SexMarkOrbGroup.SexMarkSetupOrbMax, this.evokeAmount)
    }

    //    @Override
    //    public void updateDescription() {
    //        this.description = String.format(orbStringsSet.getDESCRIPTION()[0]);
    //    }
    override fun onEvoke() {
    }

    override fun applyFocus() {
    }

    override fun onEndOfTurn() {
    }
}
