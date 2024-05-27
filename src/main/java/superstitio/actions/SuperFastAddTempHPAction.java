package superstitio.actions;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

public class SuperFastAddTempHPAction extends AutoDoneInstantAction {
    public SuperFastAddTempHPAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.setValues(target, source, amount);
        this.actionType = ActionType.HEAL;
    }

    @Override
    public void autoDoneUpdate() {
        TempHPField.tempHp.set(this.target, TempHPField.tempHp.get(this.target) + this.amount);
        if (this.amount > 0) {
            AbstractDungeon.effectsQueue.add(new HealEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY, this.amount));
            this.target.healthBarUpdatedEvent();
        }
    }

}
