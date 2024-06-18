package superstitio.powers.sexualHeatNeedModifier;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.SexualHeat;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.powers.interfaces.OnPostApplyThisPower;

public class InTurnSexualHeatNeededModifier extends AbstractSuperstitioPower implements SexualHeatNeedModifier,
        OnPostApplyThisPower<InTurnSexualHeatNeededModifier> {
    public static final String POWER_ID = DataManager.MakeTextID(InTurnSexualHeatNeededModifier.class);

    public InTurnSexualHeatNeededModifier(final AbstractCreature owner, int amount) {
        //大于0会降低高潮需求，小于0会提高高潮需求
        super(POWER_ID, owner, amount, owner instanceof AbstractPlayer
                ? amount < 0 ? PowerType.DEBUFF : PowerType.BUFF
                : amount < 0 ? PowerType.BUFF : PowerType.DEBUFF);
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

//    @Override
//    public void onCheckOrgasm(SexualHeat SexualHeatPower) {
//        SexualHeatPower.setHeatRequired(SexualHeat.HEAT_REQUIREDOrigin - this.amount);
//    }

    @Override
    public void InitializePostApplyThisPower(InTurnSexualHeatNeededModifier addedPower) {
//        AutoDoneInstantAction.addToBotAbstract(() ->
//                SexualHeat.getActiveSexualHeat(owner)
//                        .ifPresent(SexualHeatPower -> SexualHeatPower.setHeatRequired(SexualHeat.HEAT_REQUIREDOrigin - this.amount)));
        AutoDoneInstantAction.addToBotAbstract(() -> {
            SexualHeat.getActiveSexualHeat(owner).ifPresent(sexualHeat -> {
                sexualHeat.CheckOrgasm();
                sexualHeat.updateDescription();
            });
        });
    }

    @Override
    public int reduceSexualHeatNeeded() {
        return this.amount;
    }
}
