package superstitio.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.powers.interfaces.orgasm.OnOrgasm_onOrgasm;

public class ChokeChoker extends AbstractLupaPower implements NonStackablePower, OnOrgasm_onOrgasm {
    public static final String POWER_ID = DataManager.MakeTextID(ChokeChoker.class.getSimpleName());
    public static final int ChokeAmount = 1;

    public ChokeChoker(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.loadRegion("choke");
    }

    @Override
    public void atStartOfTurnPostDraw() {
        AddPowers();
    }

    public void AddPowers() {
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new SexualHeatNeededModifier(this.owner, amount)));
        AutoDoneInstantAction.addToBotAbstract(() ->
                SexualHeat.getActiveSexualHeat(owner).ifPresent(SexualHeat::CheckOrgasm));
    }


    @Override
    public void onOrgasm(SexualHeat SexualHeatPower) {
//        this.flash();
        this.addToBot(new LoseHPAction(this.owner, null, this.amount));
    }

    @Override
    public void updateDescriptionArgs() {
        this.setDescriptionArgs(amount, ChokeAmount);
    }
}
