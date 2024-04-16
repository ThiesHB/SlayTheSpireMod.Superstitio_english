package SuperstitioMod.utils;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ActionUtility {
    public static void addToTopAbstract(final VoidSupplier func) {
        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                func.get();
                this.isDone = true;
            }
        });
    }
    public interface VoidSupplier
    {
        void get();
    }
}
