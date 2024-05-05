package superstitio.orbs.actions;

import superstitio.orbs.orbgroup.OrbGroup;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

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
