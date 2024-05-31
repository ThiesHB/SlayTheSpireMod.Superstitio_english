package superstitioapi.powers.barIndepend;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.Hitbox;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface HasBarRenderOnCreature {

    /**
     * 根据uuid来识别目标是否相同。如果不相同，会分批次渲染在进度条上。
     */
    String uuidOfSelf();

    /**
     * 目标进度条。如果需要修改进度条相关的参数，也可以在这里修改
     */
    BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, ? extends RenderOnThing> makeNewBarRenderOnCreature();

    /**
     * 根据uuid来识别进度条是否相同。如果不相同，会新建一个进度条。
     */
    default String uuidPointTo() {
        return this.uuidOfSelf();
    }

    default BarRenderUpdateMessage makeMessage() {
        return new BarRenderUpdateMessage(this.uuidOfSelf(), this.uuidPointTo())
                .setNewAmount(getAmountForDraw())
                .setMaxAmount(maxBarAmount())
                .setRawTextOnBar(makeBarText())
                .setTip(new BarRenderUpdateMessage.ToolTip(this.getName(), this.getDescription()))
                .setChunkColor(setupBarOrginColor());
    }

    Hitbox getBarRenderHitBox();

    float Height();

    default Color setupBarBgColor() {
        return new Color(0f, 0f, 0f, 0.3f);
    }

    default Color setupBarShadowColor() {
        return new Color(0f, 0f, 0f, 0.3f);
    }

    default Color setupBarTextColor() {
        return new Color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    Color setupBarOrginColor();

    int getAmountForDraw();

    int maxBarAmount();

    String getName();

    String getDescription();

    default String makeBarText() {
        return "%d/%d";
    }
}
