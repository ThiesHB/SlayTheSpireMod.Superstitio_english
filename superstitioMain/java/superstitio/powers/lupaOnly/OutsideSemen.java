package superstitio.powers.lupaOnly;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.SuperstitioImg;
import superstitio.powers.AbstractSuperstitioPower;
import superstitioapi.powers.barIndepend.HasBarRenderOnCreature;
import superstitioapi.powers.barIndepend.RenderOnThing;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleTips;
import superstitioapi.utils.ImgUtility;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static superstitio.powers.lupaOnly.HasBarRenderOnCreature_SemenPower.semenColor;
import static superstitioapi.InBattleDataManager.getBarRenderManager;

@SuperstitioImg.NoNeedImg
public class OutsideSemen extends AbstractSuperstitioPower implements
        SemenPower,
        InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount, HasBarRenderOnCreature_SemenPower {
    public static final String POWER_ID = DataManager.MakeTextID(OutsideSemen.class);
    public static final int SEMEN_VALUE = 2;

    public OutsideSemen(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
        updateDescription();
    }

    @Override
    public int getSemenValue() {
        return SEMEN_VALUE;
    }

    @Override
    public void onRemove() {
        getBarRenderManager().ifPresent(barRenderManager -> barRenderManager.removeChunk(this));
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount, getTotalValue());
    }

    @Override
    public AbstractPower getSelf() {
        return this;
    }

    @Override
    public Color setupBarOrginColor() {
        return ImgUtility.mixColor(semenColor(), Color.GRAY, 0.1f, 0.9f);
    }

    @Override
    public int maxBarAmount() {
        return Integer.max((int) (this.amount * 1.5f), this.owner.maxHealth / 2);
    }

    @Override
    public String makeBarText() {
        return "%d";
    }

    @Override
    public BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, ? extends RenderOnThing> makeNewBarRenderOnCreature() {
        return HasBarRenderOnCreature_SemenPower::makeNewBar_BodySemen;
    }

}
