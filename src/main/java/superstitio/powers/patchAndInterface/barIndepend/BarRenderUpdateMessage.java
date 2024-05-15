package superstitio.powers.patchAndInterface.barIndepend;

import com.badlogic.gdx.graphics.Color;

import java.util.function.Consumer;

public class BarRenderUpdateMessage {
    Color chunkColor;
    String uuidOfPower;
    String uuidOfBar;
    int newAmount;
    Consumer<BarRenderOnCreature> detail;
    int maxAmount;
    String rawTextOnBar;
    ToolTip toolTip;

    public BarRenderUpdateMessage(String uuidOfPower,String uuidOfBar) {
        this.uuidOfPower = uuidOfPower;
        this.uuidOfBar = uuidOfBar;
    }

    public BarRenderUpdateMessage setRawTextOnBar(String rawTextOnBar) {
        this.rawTextOnBar = rawTextOnBar;
        return this;
    }

    public BarRenderUpdateMessage setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
        return this;
    }

    public BarRenderUpdateMessage setNewAmount(int newAmount) {
        this.newAmount = newAmount;
        return this;
    }

    public BarRenderUpdateMessage setDetail(Consumer<BarRenderOnCreature> detail) {
        this.detail = detail;
        return this;
    }

    public BarRenderUpdateMessage setTip(ToolTip toolTip) {
        this.toolTip = new ToolTip(toolTip);
        return this;
    }

    public BarRenderUpdateMessage setChunkColor(Color newColor) {
        this.chunkColor = newColor.cpy();
        return this;
    }

    public static class ToolTip {
        String name;
        String description;

        public ToolTip(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public ToolTip(ToolTip toolTip) {
            this.name = toolTip.name;
            this.description = toolTip.description;
        }

    }
}
