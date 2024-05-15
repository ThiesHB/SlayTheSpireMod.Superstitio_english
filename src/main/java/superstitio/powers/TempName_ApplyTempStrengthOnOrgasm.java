package superstitio.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import superstitio.DataManager;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm;

public class TempName_ApplyTempStrengthOnOrgasm extends AbstractLupaPower implements OnOrgasm_onOrgasm {
    public static final String POWER_ID = DataManager.MakeTextID(TempName_ApplyTempStrengthOnOrgasm.class);

    public TempName_ApplyTempStrengthOnOrgasm(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void onOrgasm(SexualHeat SexualHeatPower) {
        addToBot_applyPower(new StrengthPower(this.owner, this.amount));
        addToBot_applyPower(new LoseStrengthPower(this.owner, this.amount));
    }

    @Override
    public void updateDescriptionArgs() {
    }
}
