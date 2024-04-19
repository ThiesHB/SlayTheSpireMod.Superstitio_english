package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.powers.interFace.OnOrgasm;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SexualHeatNeededModifier extends AbstractLupaPower implements OnOrgasm {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(SexualHeatNeededModifier.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SexualHeatNeededModifier(final AbstractCreature owner, int amount) {
        //大于0会降低高潮需求，小于0会提高高潮需求
        super(POWER_ID, powerStrings, owner, amount, amount < 0 ? PowerType.DEBUFF : PowerType.BUFF);
        this.canGoNegative = true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(SexualHeatNeededModifier.powerStrings.DESCRIPTIONS[this.amount < 0 ? 1 : 0], Math.abs(this.amount));
    }

    @Override
    public void onCheckOrgasm(SexualHeat SexualHeatPower) {
        SexualHeatPower.setHeatRequired(SexualHeat.HEAT_REQUIREDOrigin - this.amount);
    }

    @Override
    public void afterOrgasm(SexualHeat SexualHeatPower) {
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
