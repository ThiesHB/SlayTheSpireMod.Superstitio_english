package superstitio.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.powers.interfaces.OnPostApplyThisPower;
import superstitio.utils.PowerUtility;

public class InsideSemen extends AbstractWithBarPower implements OnPostApplyThisPower {
    public static final String POWER_ID = DataManager.MakeTextID(InsideSemen.class.getSimpleName());
    public static final int MAX_Semen = 10;
    private static final int ToDrySemenRate = 1;
    //绘制相关
    private static final Color BarTextColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);

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
        setDescriptionArgs(maxBarAmount(), ToDrySemenRate);
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
                PowerUtility.BubbleMessageHigher(power, false, powerStrings.getRightVersion().DESCRIPTIONS[1]));
        this.addToBot_applyPowerToOwner(new OutsideSemen(this.owner, flowAmount * ToDrySemenRate));
    }

//    @Override
//    public void atEndOfTurn(boolean isPlayer) {
//        if (isPlayer)
//            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
//    }

    @Override
    protected float Height() {
        return 40 * Settings.scale;
    }

    @Override
    protected Color setupBarBgColor() {
        return new Color(0f, 0f, 0f, 0.3f);
    }

    @Override
    protected Color setupBarShadowColor() {
        return new Color(0f, 0f, 0f, 0.3f);
    }

    @Override
    protected Color setupBarTextColor() {
        return BarTextColor;
    }

    @Override
    protected Color setupBarOrginColor() {
        return Color.WHITE.cpy();
    }

    @Override
    protected int maxBarAmount() {
        return MAX_Semen;
    }
}