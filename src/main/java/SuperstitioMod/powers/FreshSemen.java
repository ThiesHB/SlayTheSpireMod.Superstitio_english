package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.actions.AbstractAutoDoneAction;
import SuperstitioMod.powers.interFace.OnPostApplyThisPower;
import SuperstitioMod.utils.PowerUtility;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FreshSemen extends AbstractWithBarPower implements OnPostApplyThisPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(FreshSemen.class.getSimpleName() +
            "Power");
    public static final int MAX_Semen = 10;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int ToDrySemenRate = 1;
    //绘制相关
    private static final Color BarTextColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);

    public FreshSemen(final AbstractCreature owner, final int amount) {
        super(POWER_ID, powerStrings.NAME, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
    }

    @Override
    public void InitializePostApplyThisPower() {
        CheckOverflow();
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(FreshSemen.powerStrings.DESCRIPTIONS[0], maxBarAmount(), ToDrySemenRate);
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
        this.addToBot(new AbstractAutoDoneAction() {
            @Override
            public void autoDoneUpdate() {
                PowerUtility.BubbleMessageHigher(power, false, powerStrings.DESCRIPTIONS[1]);
            }
        });
        this.addToBot_applyPowerToOwner(new DrySemen(this.owner, flowAmount * ToDrySemenRate));
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