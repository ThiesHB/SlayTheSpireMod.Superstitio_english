package superstitio.orbs.actions;

import superstitio.orbs.OrbGroup;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class EvokeOnMonsterAction extends AbstractGameAction
{
    public OrbGroup target;

    public EvokeOnMonsterAction(final OrbGroup target, final int amt) {
        this.target = target;
        this.amount = amt;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            for (int i = 0; i < this.amount; ++i) {
                target.evokeOrb();
            }
        }
        this.tickDuration();
    }
}
