package superstitio.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onCheckOrgasm;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.powers.interfaces.OnPostApplyThisPower;

public class SexualHeatNeededModifier extends AbstractSuperstitioPower implements OnOrgasm_onCheckOrgasm, OnPostApplyThisPower {
    public static final String POWER_ID = DataManager.MakeTextID(SexualHeatNeededModifier.class);

    public SexualHeatNeededModifier(final AbstractCreature owner, int amount) {
        //大于0会降低高潮需求，小于0会提高高潮需求
        super(POWER_ID, owner, amount, amount < 0 ? PowerType.DEBUFF : PowerType.BUFF);
        this.canGoNegative = true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(Math.abs(this.amount));
    }

    @Override
    public String getDescriptionStrings() {
        return powerStrings.getDESCRIPTIONS()[this.amount < 0 ? 1 : 0];
    }

    @Override
    public void onCheckOrgasm(SexualHeat SexualHeatPower) {
        SexualHeatPower.setHeatRequired(SexualHeat.HEAT_REQUIREDOrigin - this.amount);
    }

    @Override
    public void InitializePostApplyThisPower(AbstractPower addedPower) {
        AutoDoneInstantAction.addToBotAbstract(() ->
                SexualHeat.getActiveSexualHeat(owner)
                        .ifPresent(SexualHeatPower -> SexualHeatPower.setHeatRequired(SexualHeat.HEAT_REQUIREDOrigin - this.amount)));
        AutoDoneInstantAction.addToBotAbstract(() ->
                SexualHeat.getActiveSexualHeat(owner).ifPresent(SexualHeat::CheckOrgasm));
    }
}
