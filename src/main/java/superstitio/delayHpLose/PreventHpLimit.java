package superstitio.delayHpLose;

import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.powers.AbstractSuperstitioPower;

import static superstitio.delayHpLose.DelayHpLosePower_ApplyOnAttacked.*;

public class PreventHpLimit extends AbstractSuperstitioPower implements IPreventHpLimit {
    public static final String POWER_ID = DataManager.MakeTextID(PreventHpLimit.class);

    public PreventHpLimit(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void updateDescriptionArgs() {
        this.setDescriptionArgs(amount);
    }

}
