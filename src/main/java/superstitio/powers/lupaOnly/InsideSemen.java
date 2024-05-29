package superstitio.powers.lupaOnly;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.SuperstitioImg;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.patchAndInterface.barIndepend.HasBarRenderOnCreature_Power;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleTips;
import superstitio.utils.PowerUtility;

import static superstitio.cards.general.FuckJob_Card.InsideSemenRate;

@SuperstitioImg.NoNeedImg
public class InsideSemen extends AbstractSuperstitioPower implements
        SemenPower,
        InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount, HasBarRenderOnCreature_Power {
    public static final String POWER_ID = DataManager.MakeTextID(InsideSemen.class);
    public static final int MAX_Semen_Origin = 10;

    public static final int SEMEN_VALUE = 3;
    private static final int ToOutSideSemenRate = 1;
    public int maxSemen;

    @Override
    public int getSemenValue() {
        return SEMEN_VALUE;
    }

    public InsideSemen(final AbstractCreature owner, final int amount) {
        super(POWER_ID, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
        maxSemen = MAX_Semen_Origin;
        updateDescription();
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount * InsideSemenRate, maxBarAmount(), ToOutSideSemenRate);
    }

    @Override
    public void stackPower(final int stackAmount) {
        if (this.amount < 0)
            this.amount = 0;
        super.stackPower(stackAmount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
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
                PowerUtility.BubbleMessageHigher(power, true, powerStrings.getDESCRIPTIONS()[1]));
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
        return maxSemen;
    }

    /**
     * 扩张到最接近的整十数
     */
    public void expand(int tryExpandValue) {
        int newMaxSemen = tryExpandValue - (tryExpandValue % MAX_Semen_Origin);
        if (newMaxSemen >= this.maxSemen)
            PowerUtility.BubbleMessageHigher(this, false, powerStrings.getDESCRIPTIONS()[2]);
        this.maxSemen = newMaxSemen;
        updateDescription();
    }
}