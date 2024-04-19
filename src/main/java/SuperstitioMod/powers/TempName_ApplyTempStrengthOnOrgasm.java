package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.powers.interFace.OnOrgasm;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class TempName_ApplyTempStrengthOnOrgasm extends AbstractLupaPower implements OnOrgasm {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(TempName_ApplyTempStrengthOnOrgasm.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public TempName_ApplyTempStrengthOnOrgasm(final AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings, owner, amount);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(TempName_ApplyTempStrengthOnOrgasm.powerStrings.DESCRIPTIONS[0]);
    }

    @Override
    public void onCheckOrgasm(SexualHeat SexualHeatPower) {
    }

    @Override
    public void afterOrgasm(SexualHeat SexualHeatPower) {
        addToBot_applyPowerToOwner(new StrengthPower(this.owner, this.amount));
        addToBot_applyPowerToOwner(new LoseStrengthPower(this.owner, this.amount));
    }

    @Override
    public void afterEndOrgasm(SexualHeat SexualHeatPower) {

    }

    @Override
    public boolean preventOrgasm(SexualHeat SexualHeatPower) {
        return false;
    }

    @Override
    public void beforeSquirt(SexualHeat SexualHeatPower) {
    }
}
