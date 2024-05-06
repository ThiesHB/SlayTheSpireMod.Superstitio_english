package superstitio.powers.barIndepend;

import com.badlogic.gdx.graphics.Color;

import java.util.function.Consumer;

public class BarRenderUpdateMessage {
    Color barColor;
    String uuid;
    int newAmount;
    Consumer<BarRenderOnCreature> detail;

    public BarRenderUpdateMessage(String uuid, int newAmount) {
        this.uuid = uuid;
        this.newAmount = newAmount;
    }

    public BarRenderUpdateMessage setDetail(Consumer<BarRenderOnCreature> detail) {
        this.detail = detail;
        return this;
    }

    public BarRenderUpdateMessage setBarColor(Color newColor) {
        this.barColor = newColor.cpy();
        return this;
    }
}
