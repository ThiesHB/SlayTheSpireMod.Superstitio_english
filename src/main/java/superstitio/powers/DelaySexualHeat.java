package superstitio.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;

public class DelaySexualHeat extends AbstractLupaPower {
    public static final String POWER_ID = DataManager.MakeTextID(DelaySexualHeat.class.getSimpleName());

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
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new SexualHeat(this.owner, amount)));
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

}
