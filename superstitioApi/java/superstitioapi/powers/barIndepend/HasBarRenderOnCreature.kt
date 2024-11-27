package superstitioapi.powers.barIndepend

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.helpers.Hitbox
import superstitioapi.powers.barIndepend.BarRenderUpdateMessage.ToolTip
import java.util.function.BiFunction
import java.util.function.Supplier

interface HasBarRenderOnCreature {
    /**
     * 根据uuid来识别目标是否相同。如果不相同，会分批次渲染在进度条上。
     */
    fun uuidOfSelf(): String

    /**
     * 目标进度条。如果需要修改进度条相关的参数，也可以在这里修改
     */
    fun makeNewBarRenderOnCreature(): BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, out RenderOnThing>

    /**
     * 根据uuid来识别进度条是否相同。如果不相同，会新建一个进度条。
     */
    fun uuidPointTo(): String {
        return this.uuidOfSelf()
    }

    fun makeMessage(): BarRenderUpdateMessage {
        return BarRenderUpdateMessage(this.uuidOfSelf(), this.uuidPointTo())
            .setNewAmount(getAmountForDraw())
            .setMaxAmount(maxBarAmount())
            .setRawTextOnBar(makeBarText())
            .setTip(ToolTip(this.name, this.description))
            .setChunkColor(setupBarOriginColor())
    }

    fun getBarRenderHitBox(): Hitbox

    fun Height(): Float

    fun setupBarBgColor(): Color? {
        return Color(0f, 0f, 0f, 0.3f)
    }

    fun setupBarShadowColor(): Color? {
        return Color(0f, 0f, 0f, 0.3f)
    }

    fun setupBarTextColor(): Color? {
        return Color(1.0f, 1.0f, 1.0f, 1.0f)
    }

    fun setupBarOriginColor(): Color

    fun getAmountForDraw(): Int

    fun maxBarAmount(): Int

    val name: String

    val description: String

    fun makeBarText(): String? {
        return "%d/%d"
    }
}
