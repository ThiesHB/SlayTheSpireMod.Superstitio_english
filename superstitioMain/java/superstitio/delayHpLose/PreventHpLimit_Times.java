package superstitio.delayHpLose;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.powers.AbstractSuperstitioPower;

import static superstitio.delayHpLose.DelayHpLosePower_ApplyOnAttacked.IPreventHpLimit;

public class PreventHpLimit_Times extends AbstractSuperstitioPower implements IPreventHpLimit {
    public static final String POWER_ID = DataManager.MakeTextID(PreventHpLimit_Times.class);

    public PreventHpLimit_Times(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void updateDescriptionArgs() {
        this.setDescriptionArgs(amount);
    }

    @Override
    public void onPreventHpLimit() {
        addToTop(new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    @Override
    public void atEndOfRound() {
        addToBot_removeSpecificPower(this);
    }
}
