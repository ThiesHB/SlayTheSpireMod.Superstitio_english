package superstitio.orbs.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import superstitio.orbs.orbgroup.OrbGroup;

public class AnimationOrbOnMonsterAction extends AbstractGameAction {
    public OrbGroup target;

    public AnimationOrbOnMonsterAction(final OrbGroup target, final int amt) {
        this.target = target;
        this.amount = amt;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration != Settings.ACTION_DUR_FAST) return;
        for (int i = 0; i < this.amount; i++) {
            target.triggerEvokeAnimation(i);
        }
        this.isDone = true;
    }
}
