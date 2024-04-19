package SuperstitioMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

public abstract class AbstractAutoDoneAction extends AbstractGameAction {
    @Override
    public void update() {
        this.isDone = true;
        autoDoneUpdate();
    }

    public abstract void autoDoneUpdate();
}
