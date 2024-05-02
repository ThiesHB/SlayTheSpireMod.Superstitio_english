package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import SuperstitioMod.actions.AutoDoneAction;
import SuperstitioMod.powers.interFace.OnOrgasm.OnOrgasm_afterOrgasm;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class ChokeChoker extends AbstractLupaPower implements NonStackablePower, OnOrgasm_afterOrgasm {
    public static final String POWER_ID = DataManager.MakeTextID(ChokeChoker.class.getSimpleName() + "Power");
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
        AutoDoneAction.addToBotAbstract(() ->
                SexualHeat.getActiveSexualHeat(owner).ifPresent(SexualHeat::CheckOrgasm));
    }


    @Override
    public void afterTriggerOrgasm(SexualHeat SexualHeatPower) {
//        this.flash();
        this.addToBot(new LoseHPAction(this.owner, null, this.amount));
    }

    @Override
    public void updateDescriptionArgs() {
        this.setDescriptionArgs(amount, ChokeAmount);
    }
}
