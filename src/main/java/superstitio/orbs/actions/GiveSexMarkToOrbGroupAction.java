package superstitio.orbs.actions;

import superstitio.actions.AutoDoneAction;
import superstitio.orbs.SexMarkOrb;
import superstitio.orbs.orbgroup.SexMarkOrbGroup;

public class GiveSexMarkToOrbGroupAction extends AutoDoneAction {
    public SexMarkOrbGroup target;
    public SexMarkOrb sexMark;

    public GiveSexMarkToOrbGroupAction(final SexMarkOrbGroup target, final SexMarkOrb sexMark) {
        this.target = target;
        this.sexMark = sexMark;
    }

    @Override
    public void autoDoneUpdate() {
        target.evokeOrb(this.sexMark);
        target.channelOrb(this.sexMark);
    }
}
