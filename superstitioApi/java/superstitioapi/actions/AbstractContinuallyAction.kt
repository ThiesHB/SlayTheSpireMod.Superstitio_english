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

    protected boolean isDoneCheck() {
        return false;
    }

    /**
     * 在第一帧调用本函数
     * <p></p>请不要在本函数的最开头写“是否怪物存活”一类的检测，因为在此之前就会调用 {@link ActionUtility#isNotInBattle()}
     */
    protected abstract void StartAction();

    /**
     * 不会在第一帧调用本函数
     * <p></p>请不要在本函数的最开头写“是否怪物存活”一类的检测，因为在此之前就会调用 {@link ActionUtility#isNotInBattle()}
     */
    protected abstract void RunAction();

    /**
     * 在最后一帧调用本函数，注意，最后一帧 {@link #RunAction()} 依旧会被调用
     * <p></p>请不要在本函数的最开头写“是否怪物存活”一类的检测，因为在此之前就会调用 {@link ActionUtility#isNotInBattle()}
     */
    protected abstract void EndAction();

    public final void addToBot() {
        AbstractDungeon.actionManager.addToBottom(this);
    }

    public final void addToTop() {
        AbstractDungeon.actionManager.addToTop(this);
    }

    @Override
    public final void update() {
        if (isDoneCheck()) {
            this.isDone = true;
            return;
        }
        if (ActionUtility.isNotInBattle()) {
            this.isDone = true;
            return;
        }
        if (this.duration == this.startDuration)
            StartAction();
        else
            RunAction();
        this.tickDuration();
        if (isDone)
            EndAction();
    }
}
