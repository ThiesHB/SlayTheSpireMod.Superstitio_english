package superstitioapi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitioapi.utils.ActionUtility;

public abstract class AutoDoneInstantAction extends AbstractGameAction {

    public static void addToBotAbstract(final ActionUtility.VoidSupplier func) {
        AbstractDungeon.actionManager.addToBottom(newAutoDone(func));
    }

    public static void addToBotAbstract(final ActionUtility.VoidSupplier func, int time) {
        if (time <= 1)
            AbstractDungeon.actionManager.addToBottom(newAutoDone(func));
        else
            addToBotAbstract(() -> addToBotAbstract(func, time - 1));
    }

    public static void addToTopAbstract(final ActionUtility.VoidSupplier func) {
        AbstractDungeon.actionManager.addToTop(newAutoDone(func));
    }

    public static AbstractGameAction newAutoDone(final ActionUtility.VoidSupplier func) {
        return new AutoDoneInstantAction() {
            @Override
            public void autoDoneUpdate() {
                func.get();
            }
        };
    }

    public abstract void autoDoneUpdate();

    @Override
    public final void update() {
        this.isDone = true;
        if (ActionUtility.isNotInBattle()) return;
        autoDoneUpdate();
    }

}
