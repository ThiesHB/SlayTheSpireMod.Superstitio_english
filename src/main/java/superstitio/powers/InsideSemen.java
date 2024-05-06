package superstitio.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.powers.barIndepend.HasBarRenderOnCreature_Power;
import superstitio.powers.interfaces.InvisiblePower_StillRenderApplyAndRemove;
import superstitio.powers.interfaces.OnPostApplyThisPower;
import superstitio.utils.PowerUtility;

public class InsideSemen extends AbstractLupaPower implements OnPostApplyThisPower, InvisiblePower_StillRenderApplyAndRemove, HasBarRenderOnCreature_Power {
    public static final String POWER_ID = DataManager.MakeTextID(InsideSemen.class.getSimpleName());
    public static final int MAX_Semen = 10;
    private static final int ToOutSideSemenRate = 1;

    public InsideSemen(final AbstractCreature owner, final int amount) {
        super(POWER_ID, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
    }

    @Override
    public void InitializePostApplyThisPower() {
        CheckOverflow();
        updateDescription();
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(maxBarAmount(), ToOutSideSemenRate);
    }

    @Override
    public void stackPower(final int stackAmount) {
        if (this.amount < 0)
            this.amount = 0;
        super.stackPower(stackAmount);
        CheckOverflow();
    }

    public void CheckOverflow() {
        if (amount <= maxBarAmount()) return;
        this.Overflow(amount - maxBarAmount());
        amount = maxBarAmount();
    }

    private void Overflow(int flowAmount) {
        AbstractPower power = this;
        AutoDoneInstantAction.addToBotAbstract(() ->
                PowerUtility.BubbleMessageHigher(power, false, powerStrings.getDESCRIPTIONS()[1]));
        this.addToBot_applyPowerToOwner(new OutsideSemen(this.owner, flowAmount * ToOutSideSemenRate));
    }

    @Override
    public AbstractPower getSelf() {
        return this;
    }

    @Override
    public String uuidOfSelf() {
        return this.ID;
    }

    @Override
    public float Height() {
        return 120 * Settings.scale;
    }

    @Override
    public Color setupBarOrginColor() {
        return Color.WHITE.cpy();
    }

    @Override
    public int maxBarAmount() {
        return MAX_Semen;
    }
}