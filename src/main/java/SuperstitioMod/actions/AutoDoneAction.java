package SuperstitioMod.actions;

import SuperstitioMod.utils.ActionUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class AutoDoneAction extends AbstractGameAction {

    public static void addToBotAbstract(final ActionUtility.VoidSupplier func) {
        AbstractDungeon.actionManager.addToBottom(newAutoDone(func));
    }

    public static AbstractGameAction newAutoDone(final ActionUtility.VoidSupplier func) {
         return new AutoDoneAction() {
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
