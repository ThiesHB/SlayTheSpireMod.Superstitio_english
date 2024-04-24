package SuperstitioMod.utils;

import SuperstitioMod.actions.AbstractAutoDoneAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

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

    public static void addToBot_applyPowerToPlayer(final AbstractPower power) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, power));
    }

    public static void addToBot_applyPowerToEnemy(final AbstractPower power, AbstractMonster monster) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, AbstractDungeon.player, power));
    }
}
