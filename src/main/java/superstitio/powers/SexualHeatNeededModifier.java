package superstitio.powers;

import superstitio.DataManager;
import superstitio.powers.interfaces.orgasm.OnOrgasm_onCheck;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class SexualHeatNeededModifier extends AbstractLupaPower implements OnOrgasm_onCheck {
    public static final String POWER_ID = DataManager.MakeTextID(SexualHeatNeededModifier.class.getSimpleName() );

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
        return powerStringsSet.getRightVersion().DESCRIPTIONS[this.amount < 0 ? 1 : 0];
    }

    @Override
    public void onCheckOrgasm(SexualHeat SexualHeatPower) {
        SexualHeatPower.setHeatRequired(SexualHeat.HEAT_REQUIREDOrigin - this.amount);
    }
}
