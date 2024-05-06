package superstitio.powers.barIndepend;

import com.badlogic.gdx.graphics.Color;

import java.util.function.Function;

public interface HasBarRenderOnCreature {

    BarRenderOnCreature_Power getAmountBar();

    String uuidForBarRender();

    void setupAmountBar(BarRenderOnCreature_Power amountBar);

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

    Function<Object[], String> makeBarText();
}
