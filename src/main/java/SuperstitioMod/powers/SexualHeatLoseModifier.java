package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.powers.interFace.OnOrgasm;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SexualHeatLoseModifier extends AbstractLupaPower implements OnOrgasm {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(SexualHeatLoseModifier.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SexualHeatLoseModifier(final AbstractCreature owner, int amount) {
        //大于0会降低潮吹时的快感消耗，小于0会提高
        super(POWER_ID, powerStrings, owner, amount, amount > 0 ? PowerType.BUFF : PowerType.DEBUFF);

        this.canGoNegative = true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }


    @Override
    public void updateDescription() {
        this.description = String.format(SexualHeatLoseModifier.powerStrings.DESCRIPTIONS[this.amount < 0 ? 1 : 0], Math.abs(this.amount));
    }

    @Override
    public void onCheckOrgasm(SexualHeat SexualHeatPower) {
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
        SexualHeatPower.setHeatReducePerCard(SexualHeat.HeatReducePerCard_Origin - this.amount);
    }
}
