package superstitioapi.powers.barIndepend

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.helpers.PowerTip
import java.util.function.Consumer

class BarRenderUpdateMessage(var uuidOfPower: String, var uuidOfBar: String)
{
    var chunkColor: Color? = null
    var newAmount: Int = 0
    var detail: Consumer<RenderOnThing>? = null
    var maxAmount: Int = 0
    var rawTextOnBar: String? = null
    var toolTip: ToolTip? = null

    fun toPowerTip(toolTip: ToolTip): PowerTip
    {
        return PowerTip(toolTip.name, toolTip.description)
    }

    fun setRawTextOnBar(rawTextOnBar: String?): BarRenderUpdateMessage
    {
        this.rawTextOnBar = rawTextOnBar
        return this
    }

    fun setMaxAmount(maxAmount: Int): BarRenderUpdateMessage
    {
        this.maxAmount = maxAmount
        return this
    }

    fun setNewAmount(newAmount: Int): BarRenderUpdateMessage
    {
        this.newAmount = newAmount
        return this
    }

    fun setDetail(detail: Consumer<RenderOnThing>?): BarRenderUpdateMessage
    {
        this.detail = detail
        return this
    }

    fun setTip(toolTip: ToolTip): BarRenderUpdateMessage
    {
        this.toolTip = ToolTip(toolTip)
        return this
    }

    fun setChunkColor(newColor: Color): BarRenderUpdateMessage
    {
        this.chunkColor = newColor.cpy()
        return this
    }

    class ToolTip
    {
        var name: String
        var description: String

        constructor(name: String, description: String)
        {
            this.name = name
            this.description = description
        }

        constructor(toolTip: ToolTip)
        {
            this.name = toolTip.name
            this.description = toolTip.description
        }
    }
}
