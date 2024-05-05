package superstitio.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.utils.ActionUtility;

public abstract class AutoDoneInstantAction extends AbstractGameAction {

    public static void addToBotAbstract(final ActionUtility.VoidSupplier func) {
        AbstractDungeon.actionManager.addToBottom(newAutoDone(func));
    }

    public static AbstractGameAction newAutoDone(final ActionUtility.VoidSupplier func) {
        return new AutoDoneInstantAction() {
            @Override
            public void autoDoneUpdate() {
                func.get();
            }
        };
    }

    @Override
    public final void update() {
        this.isDone = true;
        autoDoneUpdate();
    }

    public abstract void autoDoneUpdate();

}
