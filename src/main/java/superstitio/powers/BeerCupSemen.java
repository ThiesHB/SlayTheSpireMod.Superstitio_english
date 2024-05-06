package superstitio.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.powers.barIndepend.BarRenderOnCreature_Power;
import superstitio.powers.barIndepend.HasBarRenderOnCreature_Power;
import superstitio.powers.interfaces.InvisiblePower_StillRenderAmount;
import superstitio.powers.interfaces.OnPostApplyThisPower;
import superstitio.utils.PowerUtility;

public class BeerCupSemen extends AbstractLupaPower implements
        InvisiblePower_StillRenderAmount, HasBarRenderOnCreature_Power<AbstractPower>, OnPostApplyThisPower, BetterOnApplyPowerPower {
    public static final String POWER_ID = DataManager.MakeTextID(BeerCupSemen.class.getSimpleName());
    //绘制相关
    private BarRenderOnCreature_Power AmountBar;
    private int maxAmount;
    private int semenAmount = 0;

    public BeerCupSemen(final AbstractCreature owner, final int maxAmount) {
        super(POWER_ID, owner, -1, PowerType.BUFF, false);
        this.maxAmount = maxAmount;
        BarRenderOnCreature_Power.RegisterToBarRenderOnCreature(this, this.ID);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power instanceof BeerCupSemen) {
            this.maxAmount += ((BeerCupSemen) power).maxAmount;
        }
    }

    @Override
    public void InitializePostApplyThisPower() {
        CheckFull();
        updateDescription();
    }

    @Override
    public int getAmountForDraw() {
        return this.semenAmount;
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(maxBarAmount());
    }


    public void CheckFull() {
        if (semenAmount >= maxBarAmount()) {
            this.Full();
        }
    }

    @Override
    public void onVictory() {
        final AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth > 0) {
            p.heal(this.semenAmount);
        }
    }

    private void Full() {
        AbstractPower power = this;
        AutoDoneInstantAction.addToBotAbstract(() ->
                PowerUtility.BubbleMessageHigher(power, false, powerStrings.getDESCRIPTIONS()[2]));
        this.semenAmount = 0;
        addToBot(new HealAction(this.owner, this.owner, maxBarAmount()));
        AutoDoneInstantAction.addToBotAbstract(() ->
                PowerUtility.BubbleMessageHigher(power, false, powerStrings.getDESCRIPTIONS()[3]));
        addToBot_removeSpecificPower(this);
    }

    @Override
    public BarRenderOnCreature_Power getAmountBar() {
        return this.AmountBar;
    }

    @Override
    public void setupAmountBar(BarRenderOnCreature_Power amountBar) {
        this.AmountBar = amountBar;
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
        return Color.WHITE.cpy();
    }

    @Override
    public int maxBarAmount() {
        return maxAmount;
    }

    @Override
    public boolean betterOnApplyPower(AbstractPower power, AbstractCreature creature, AbstractCreature creature1) {
        if ((power instanceof InsideSemen || power instanceof OutsideSemen) && power.amount > 0) {
            AutoDoneInstantAction.addToBotAbstract(() ->
                    PowerUtility.BubbleMessageHigher(power, false, powerStrings.getDESCRIPTIONS()[1]));
            this.semenAmount += power.amount;
            CheckFull();
            return false;
        }
        return true;
    }
}