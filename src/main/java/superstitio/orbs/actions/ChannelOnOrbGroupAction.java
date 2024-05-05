package superstitio.orbs.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import superstitio.orbs.orbgroup.OrbGroup;

public class ChannelOnOrbGroupAction extends AbstractGameAction {
    public OrbGroup target;
    public AbstractOrb orb;

    public ChannelOnOrbGroupAction(final OrbGroup target, final AbstractOrb orb) {
        this.target = target;
        this.orb = orb;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            target.channelOrb(this.orb);
            this.isDone = true;
        }
    }
}
