package superstitio.orbs.actions;

import superstitio.orbs.SexMarkOrb;
import superstitio.orbs.orbgroup.SexMarkOrbGroup;
import superstitioapi.actions.AutoDoneInstantAction;

public class GiveSexMarkToOrbGroupInstantAction extends AutoDoneInstantAction {
    public SexMarkOrbGroup target;
    public SexMarkOrb sexMark;

    public GiveSexMarkToOrbGroupInstantAction(final SexMarkOrbGroup target, final SexMarkOrb sexMark) {
        this.target = target;
        this.sexMark = sexMark;
    }

    @Override
    public void autoDoneUpdate() {
//        target.evokeOrb(this.sexMark);
        target.channelOrb(this.sexMark);
    }
}
