package superstitio.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;

public class DelaySexualHeat extends AbstractSuperstitioPower {
    public static final String POWER_ID = DataManager.MakeTextID(DelaySexualHeat.class);

    public DelaySexualHeat(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }


    @Override
    public void updateDescriptionArgs() {
        this.setDescriptionArgs(amount);
    }


    @Override
    public void atStartOfTurn() {
        this.flash();
        SexualHeat.addToBot_addSexualHeat(this.owner,amount);
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

}
