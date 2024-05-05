package superstitio.powers;

import superstitio.DataManager;
import superstitio.powers.interfaces.orgasm.OnOrgasm_afterOrgasm;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class TempName_ApplyTempStrengthOnOrgasm extends AbstractLupaPower implements OnOrgasm_afterOrgasm {
    public static final String POWER_ID = DataManager.MakeTextID(TempName_ApplyTempStrengthOnOrgasm.class.getSimpleName() );

    public TempName_ApplyTempStrengthOnOrgasm(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void afterTriggerOrgasm(SexualHeat SexualHeatPower) {
        addToBot_applyPowerToOwner(new StrengthPower(this.owner, this.amount));
        addToBot_applyPowerToOwner(new LoseStrengthPower(this.owner, this.amount));
    }

    @Override
    public void updateDescriptionArgs() {
    }
}
