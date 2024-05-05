package superstitio.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import superstitio.DataManager;
import superstitio.cards.modifiers.block.DrySemenBlock;

public class OutsideSemen extends AbstractWithBarPower {
    public static final String POWER_ID = DataManager.MakeTextID(OutsideSemen.class.getSimpleName());
    private static final Color BarTextColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);

    public OutsideSemen(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
    }

    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.addToBot(new GainCustomBlockAction(new BlockModContainer(this, new DrySemenBlock()), this.owner, this.amount));
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    protected float Height() {
        return 80 * Settings.scale;
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
        return Integer.max((int) (this.amount * 1.5f), this.owner.maxHealth);
    }
}
