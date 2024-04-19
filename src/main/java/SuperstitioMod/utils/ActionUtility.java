package SuperstitioMod.utils;

import SuperstitioMod.actions.AbstractAutoDoneAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ActionUtility {
    public static void addToTopAbstract(final VoidSupplier func) {
        AbstractDungeon.actionManager.addToTop(new AbstractAutoDoneAction() {
            @Override
            public void autoDoneUpdate() {
                func.get();
            }
        });
    }
    public interface VoidSupplier
    {
        void get();
    }
}
