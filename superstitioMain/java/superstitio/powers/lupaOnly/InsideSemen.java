package superstitio.powers.lupaOnly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.SuperstitioImg;
import superstitio.powers.AbstractSuperstitioPower;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.powers.barIndepend.BarRenderOnThing_Vertical;
import superstitioapi.powers.barIndepend.HasBarRenderOnCreature;
import superstitioapi.powers.barIndepend.HasBarRenderOnCreature_Power;
import superstitioapi.powers.barIndepend.RenderOnThing;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleTips;
import superstitioapi.utils.ImgUtility;
import superstitioapi.utils.PowerUtility;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static superstitio.powers.lupaOnly.HasBarRenderOnCreature_SemenPower.semenColor;
import static superstitioapi.powers.barIndepend.BarRenderOnThing_Vertical.BAR_WIDTH;
import static superstitioapi.powers.barIndepend.RenderOnThing.BAR_OFFSET_Y;

@SuperstitioImg.NoNeedImg
public class InsideSemen extends AbstractSuperstitioPower implements
        SemenPower,
        InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount, HasBarRenderOnCreature_Power {
    public static final String POWER_ID = DataManager.MakeTextID(InsideSemen.class);
    public static final int MAX_Semen_Origin = 10;
    public static final int SEMEN_VALUE = 3;
    private static final int ToOutSideSemenRate = 1;
    public int maxSemen;
    //TODO 改装成不同怪物获得不同精液名称
    public String semenSource;

    public InsideSemen(final AbstractCreature owner, final int amount) {
        super(POWER_ID, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
        maxSemen = MAX_Semen_Origin;
        updateDescription();
    }

    protected static BarRenderOnThing_Vertical makeNewBar_InsideSemen(Supplier<Hitbox> hitbox, HasBarRenderOnCreature power) {
        BarRenderOnThing_Vertical bar = new BarRenderOnThing_Vertical(hitbox, power) {
            @Override
            protected float chunkLength(int amount) {
                return (this.barLength * (amount)) / (float) getMaxBarAmount();
            }
        };
        bar.barTextColor = semenColor();
        bar.barLength = hitbox.get().height / 2;
        bar.hitbox.height = bar.barLength + BAR_WIDTH - BAR_OFFSET_Y * 2;
        return bar;
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
    }

//    public InsideSemen(final AbstractCreature owner, final int amount) {
//        super(POWER_ID, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
//        maxSemen = MAX_Semen_Origin;
//        updateDescription();
//    }

    @Override
    public int getSemenValue() {
        return SEMEN_VALUE;
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount, getTotalValue(), maxBarAmount(), ToOutSideSemenRate);
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
    public float Height() {
        return 120 * Settings.scale;
    }

    @Override
    public Color setupBarOrginColor() {
        return ImgUtility.mixColor(semenColor(), Color.PINK, 0.3f, 0.9f);
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

    @Override
    public String makeBarText() {
        return "%d%n/%d";
    }

    @Override
    public BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, ? extends RenderOnThing> makeNewBarRenderOnCreature() {
        return InsideSemen::makeNewBar_InsideSemen;
    }
}