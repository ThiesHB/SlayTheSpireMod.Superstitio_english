package superstitio.powers.lupaOnly;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.patchAndInterface.barIndepend.HasBarRenderOnCreature_Power;
import superstitio.powers.patchAndInterface.interfaces.OnPostApplyThisPower;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleTips;
import superstitio.utils.PowerUtility;

import static superstitio.cards.general.FuckJob_Card.InsideSemenRate;
import static superstitio.cards.general.FuckJob_Card.OutsideSemenRate;

public class InsideSemen extends AbstractSuperstitioPower implements OnPostApplyThisPower,
        InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount, HasBarRenderOnCreature_Power {
    public static final String POWER_ID = DataManager.MakeTextID(InsideSemen.class);
    private static final int ToOutSideSemenRate = InsideSemenRate / OutsideSemenRate;
    public static final int MAX_Semen = 10 * ToOutSideSemenRate;

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
        setDescriptionArgs(maxBarAmount(), 1 / ToOutSideSemenRate);
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
        this.addToBot_applyPower(new OutsideSemen(this.owner, flowAmount / ToOutSideSemenRate));
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