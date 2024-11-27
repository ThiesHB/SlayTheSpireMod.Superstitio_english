package superstitio.orbs.actions

import superstitio.orbs.SexMarkOrb
import superstitio.orbs.orbgroup.SexMarkOrbGroup
import superstitioapi.actions.AutoDoneInstantAction

class GiveSexMarkToOrbGroupInstantAction(target: SexMarkOrbGroup, sexMark: SexMarkOrb) : AutoDoneInstantAction() {
    var targetOrbGroup: SexMarkOrbGroup
    var sexMark: SexMarkOrb

    init {
        this.targetOrbGroup = target
        this.sexMark = sexMark
    }

    override fun autoDoneUpdate() {
//        target.evokeOrb(this.sexMark);
        targetOrbGroup.channelOrb(this.sexMark)
    }
}
