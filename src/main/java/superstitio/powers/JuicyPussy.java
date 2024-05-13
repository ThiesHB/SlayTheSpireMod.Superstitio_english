package superstitio.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.powers.interfaces.orgasm.OnOrgasm_onOrgasm;

public class JuicyPussy extends AbstractLupaPower implements OnOrgasm_onOrgasm {
    public static final String POWER_ID = DataManager.MakeTextID(JuicyPussy.class.getSimpleName());

    public JuicyPussy(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void onOrgasm(SexualHeat SexualHeatPower) {
        this.flash();
        DelayHpLosePower.addToBot_removePower(this.amount, this.owner, this.owner, true);
    }

    @Override
    public void updateDescriptionArgs() {
    }
}
