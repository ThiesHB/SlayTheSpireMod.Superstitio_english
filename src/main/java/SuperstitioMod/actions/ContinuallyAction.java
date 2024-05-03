package SuperstitioMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public abstract class ContinuallyAction extends AbstractGameAction {

    public ContinuallyAction(ActionType actionType,float duration) {
        this.actionType = actionType;
        this.startDuration = duration;
        this.duration = duration;
    }


    @Override
    public final void update() {
        if (this.duration == this.startDuration)
            ActionSetUp();
        else
            RunAction();
        this.tickDuration();
    }

    protected abstract void RunAction();

    protected abstract void ActionSetUp();
}
