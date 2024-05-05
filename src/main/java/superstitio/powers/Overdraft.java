package superstitio.powers;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;

public class Overdraft extends AbstractLupaPower {
    public static final String POWER_ID = DataManager.MakeTextID(Overdraft.class.getSimpleName());

    public Overdraft(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }


    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(amount);
    }


    @Override
    public void atStartOfTurnPostDraw() {
        this.addToBot(new ExhaustAction(amount, false, false));
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }
}
