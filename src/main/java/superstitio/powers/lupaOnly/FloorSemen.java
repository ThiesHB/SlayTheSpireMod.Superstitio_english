package superstitio.powers.lupaOnly;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.SuperstitioImg;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.patchAndInterface.barIndepend.HasBarRenderOnCreature_Power;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleTips;

import static superstitio.cards.general.FuckJob_Card.FloorSemenRate;

@SuperstitioImg.NoNeedImg
public class FloorSemen extends AbstractSuperstitioPower implements
        SemenPower,
        InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount, HasBarRenderOnCreature_Power {
    public static final String POWER_ID = DataManager.MakeTextID(FloorSemen.class);
    public static final int SEMEN_VALUE = 1;

    public FloorSemen(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
        updateDescription();
    }

    @Override
    public int getSemenValue() {
        return SEMEN_VALUE;
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount * FloorSemenRate);
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
        return 160 * Settings.scale;
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
