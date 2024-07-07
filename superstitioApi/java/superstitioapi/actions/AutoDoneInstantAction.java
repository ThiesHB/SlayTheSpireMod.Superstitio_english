package superstitioapi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitioapi.utils.ActionUtility;

public abstract class AutoDoneInstantAction extends AbstractGameAction {

    public static void addToBotAbstract(final ActionUtility.VoidSupplier func) {
        newAutoDone(func).addToBot();
    }

    public static void addToBotAbstract(final ActionUtility.VoidSupplier func, int time) {
        if (time <= 1)
            AbstractDungeon.actionManager.addToBottom(newAutoDone(func));
        else
            addToBotAbstract(() -> addToBotAbstract(func, time - 1));
    }

    public static void addToTopAbstract(final ActionUtility.VoidSupplier func) {
        newAutoDone(func).addToTop();
    }

    public static AutoDoneInstantAction newAutoDone(final ActionUtility.VoidSupplier func) {
        return new AutoDoneInstantAction() {
            @Override
            public void autoDoneUpdate() {
                func.get();
            }
        };
    }

    public abstract void autoDoneUpdate();

    public final void addToBot() {
        AbstractDungeon.actionManager.addToBottom(this);
    }

    public final void addToTop() {
        AbstractDungeon.actionManager.addToTop(this);
    }

    @Override
    public final void update() {
        this.isDone = true;
        if (ActionUtility.isNotInBattle()) return;
        autoDoneUpdate();
    }

}
