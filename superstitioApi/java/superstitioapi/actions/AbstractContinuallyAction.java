package superstitioapi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitioapi.utils.ActionUtility;

public abstract class AbstractContinuallyAction extends AbstractGameAction {

    public AbstractContinuallyAction(ActionType actionType, float duration) {
        this.actionType = actionType;
        this.startDuration = duration;
        this.duration = duration;
    }


    @Override
    public final void update() {
        if (ActionUtility.isNotInBattle()) {
            this.isDone = true;
            return;
        }
        if (this.duration == this.startDuration)
            ActionSetUp();
        else
            RunAction();
        this.tickDuration();
    }

    protected abstract void RunAction();

    protected abstract void ActionSetUp();

    public void addToBot() {
        AbstractDungeon.actionManager.addToBottom(this);
    }
}
