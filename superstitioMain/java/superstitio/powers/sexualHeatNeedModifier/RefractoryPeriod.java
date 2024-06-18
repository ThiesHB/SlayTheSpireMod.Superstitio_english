package superstitio.powers.sexualHeatNeedModifier;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.SexualHeat;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.powers.interfaces.OnPostApplyThisPower;

public class RefractoryPeriod extends AbstractSuperstitioPower implements SexualHeatNeedModifier, OnPostApplyThisPower<RefractoryPeriod> {
    public static final String POWER_ID = DataManager.MakeTextID(RefractoryPeriod.class);
    private boolean isAddedThisTurn = true;

    public RefractoryPeriod(final AbstractCreature owner, int amount) {
        //大于0会提高高潮需求，小于0会降低高潮需求
        super(POWER_ID, owner, amount, owner instanceof AbstractPlayer
                ? amount < 0 ? PowerType.BUFF : PowerType.DEBUFF
                : amount < 0 ? PowerType.DEBUFF : PowerType.BUFF);
        this.canGoNegative = true;
    }

    @Override
    public void onRemove() {
        super.onRemove();
        AutoDoneInstantAction.addToBotAbstract(() -> {
            SexualHeat.getActiveSexualHeat(owner).ifPresent(sexualHeat -> {
                sexualHeat.CheckOrgasm();
                sexualHeat.updateDescription();
            });
        });
    }

//    @Override
//    public void atStartOfTurn() {
//
//    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isAddedThisTurn) {
            isAddedThisTurn = false;
            return;
        }
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(Math.abs(this.amount));
    }

    @Override
    public String getDescriptionStrings() {
        return powerStrings.getDESCRIPTIONS()[isAddedThisTurn ? 0 : 1];
    }

    @Override
    public void InitializePostApplyThisPower(RefractoryPeriod addedPower) {
        AutoDoneInstantAction.addToBotAbstract(() ->
                addedPower.isAddedThisTurn = true);
        AutoDoneInstantAction.addToBotAbstract(() -> {
            SexualHeat.getActiveSexualHeat(owner).ifPresent(sexualHeat -> {
                sexualHeat.CheckOrgasm();
                sexualHeat.updateDescription();
            });
        });
    }

    @Override
    public int reduceSexualHeatNeeded() {
        return this.amount * -1;
    }
}
