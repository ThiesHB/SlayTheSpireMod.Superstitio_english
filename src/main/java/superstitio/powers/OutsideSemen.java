package superstitio.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cards.modifiers.block.DrySemenBlock;
import superstitio.powers.barIndepend.HasBarRenderOnCreature_Power;
import superstitio.powers.interfaces.InvisiblePower_StillRenderApplyAndRemove;

public class OutsideSemen extends AbstractLupaPower implements InvisiblePower_StillRenderApplyAndRemove, HasBarRenderOnCreature_Power {
    public static final String POWER_ID = DataManager.MakeTextID(OutsideSemen.class.getSimpleName());


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
    public AbstractPower getSelf() {
        return this;
    }

    @Override
    public String uuidOfSelf() {
        return this.ID;
    }

    @Override
    public float Height() {
        return 140 * Settings.scale;
    }

    @Override
    public Color setupBarOrginColor() {
        return Color.WHITE.cpy();
    }

    @Override
    public int maxBarAmount() {
        return Integer.max((int) (this.amount * 1.5f), this.owner.maxHealth);
    }

    @Override
    public String makeBarText() {
        return "%d";
    }
}
