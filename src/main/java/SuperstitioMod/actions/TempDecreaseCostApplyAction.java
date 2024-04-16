package SuperstitioMod.actions;

import SuperstitioMod.powers.TempDecreaseCost;
import SuperstitioMod.powers.interFace.HasTempDecreaseCostEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Arrays;


public class TempDecreaseCostApplyAction extends ApplyPowerAction {

    HasTempDecreaseCostEffect holder;

    public TempDecreaseCostApplyAction(int amount, HasTempDecreaseCostEffect holder) {
        super(AbstractDungeon.player, AbstractDungeon.player, new TempDecreaseCost(AbstractDungeon.player, amount,holder));
    }

    @Override
    public void update() {
        Arrays.stream(this.getClass().getSuperclass().getDeclaredFields()).forEach(field -> {
            if (field.getName().equals("powerToApply")) {
                try {
                    AbstractPower power = (AbstractPower) field.get(this);
                    if (power instanceof TempDecreaseCost) {
                        TempDecreaseCost power2 = (TempDecreaseCost) power;
                        power2.order = TempDecreaseCost.getAllTempDecreaseCost().map(p -> p.order).min(Integer::compareTo).orElse(0);
                        field.set(this, power2);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        super.update();
        if (isDone)
            if (TempDecreaseCost.getAllTempDecreaseCost().findAny().isPresent())
                TempDecreaseCost.tryActivateLowestOrder();
    }
}
