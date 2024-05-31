package superstitio.delayHpLose;

import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.powers.AbstractSuperstitioPower;

import static superstitio.delayHpLose.DelayHpLosePower_ApplyOnAttacked.IPreventHpLimit;

public class PreventHpLimit_Turns extends AbstractSuperstitioPower implements IPreventHpLimit {
    public static final String POWER_ID = DataManager.MakeTextID(PreventHpLimit_Turns.class);

    public PreventHpLimit_Turns(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void updateDescriptionArgs() {
        this.setDescriptionArgs(amount);
    }

    @Override
    public void atEndOfRound() {
        addToBot_AutoRemoveOne(this);
    }

    @Override
    public void onPreventHpLimit() {
    }
}
