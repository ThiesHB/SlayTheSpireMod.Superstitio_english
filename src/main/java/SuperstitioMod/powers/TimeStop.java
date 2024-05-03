package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TimeStop extends AbstractLupaPower implements BetterOnApplyPowerPower {
    public static final String POWER_ID = DataManager.MakeTextID(TimeStop.class.getSimpleName() + "Power");
    public static final int sexualReturnRate = 2;

    public TimeStop(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(amount, sexualReturnRate);
    }


    @Override
    public void atEndOfRound() {
        //if (!isPlayer) return;
        addToBot_AutoRemoveWhenTurnPast(POWER_ID);
    }

    @Override
    public boolean betterOnApplyPower(AbstractPower power, AbstractCreature creature, AbstractCreature creature1) {
        if (power instanceof SexualHeat && power.amount > 0) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new DelaySexualHeat(this.owner, power.amount * sexualReturnRate)));
            return false;
        }
        return true;
    }
}
