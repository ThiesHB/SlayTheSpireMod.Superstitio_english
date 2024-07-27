package superstitio.powers.lupaOnly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import static superstitioapi.renderManager.inBattleManager.InBattleDataManager.getBarRenderManager;

@SuperstitioImg.NoNeedImg
public class InsideSemen extends AbstractSuperstitioPower implements
        SemenPower,
        InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount, HasBarRenderOnCreature_SemenPower {
    public static final String POWER_ID = DataManager.MakeTextID(InsideSemen.class);
    //    public static final int MAX_Semen_Origin = 10;
    public static final int SEMEN_VALUE = 3;
    private static final int ToOutSideSemenRate = 1;
//    public int maxSemen;
    //TODO 改装成不同怪物获得不同精液名称
//    public String semenSource;

    public InsideSemen(final AbstractCreature owner, final int amount) {
        super(POWER_ID, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
        updateDescription();
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
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
        return ImgUtility.mixColor(semenColor(), Color.PINK, 0.3f, 0.9f);
    }

    @Override
    public BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, ? extends RenderOnThing> makeNewBarRenderOnCreature() {
        return HasBarRenderOnCreature_SemenPower::makeNewBar_BodySemen;
    }
}