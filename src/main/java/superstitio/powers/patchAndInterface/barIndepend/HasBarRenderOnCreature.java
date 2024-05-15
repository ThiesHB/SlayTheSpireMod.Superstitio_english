package superstitio.powers.patchAndInterface.barIndepend;

import com.badlogic.gdx.graphics.Color;

public interface HasBarRenderOnCreature {

    String uuidOfSelf();

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
