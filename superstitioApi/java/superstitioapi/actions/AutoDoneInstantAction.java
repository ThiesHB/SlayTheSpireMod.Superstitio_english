package superstitioapi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitioapi.utils.ActionUtility;
import superstitioapi.utils.CardUtility;

public abstract class AutoDoneInstantAction extends AbstractGameAction {

    public static void addToBotAbstract(final ActionUtility.VoidSupplier func) {
        AbstractDungeon.actionManager.addToBottom(newAutoDone(func));
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

    @Override
    public final void update() {
        this.isDone = true;
        if (CardUtility.isNotInBattle()) return;
        autoDoneUpdate();
    }

    public abstract void autoDoneUpdate();

}
