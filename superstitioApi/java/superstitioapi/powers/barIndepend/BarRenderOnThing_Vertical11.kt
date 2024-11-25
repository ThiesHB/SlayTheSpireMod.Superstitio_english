package superstitioapi.powers.barIndepend

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.ImageMaster
import superstitioapi.powers.barIndepend.BarRenderOnThing.BarAmountChunk.OrderType
import superstitioapi.utils.ImgUtility
import java.util.function.Supplier

class BarRenderOnThing_Vertical(hitbox: Supplier<Hitbox>, power: HasBarRenderOnCreature) :
    BarRenderOnThing(hitbox, power) {
    init {
        this.barLength = hitboxBondTo.get().height
        this.hitbox = Hitbox(
            BAR_WIDTH * 1.5f,
            hitboxBondTo.get().height + BAR_WIDTH - BAR_OFFSET_Y * 2
        )
        updateHitBoxPlace(this.hitbox)
    }

    override fun updateHitBoxPlace(hitbox: Hitbox) {
        hitbox.move(
            hitboxBondTo.get().cX - HeightOffset - hitboxBondTo.get().width / 2,
            hitboxBondTo.get().y + barLength / 2 + BAR_OFFSET_Y * 2
        )
    }

    override val yDrawStart: Float
        get() = hitbox.cY - this.barLength / 2.0f

    override val xDrawStart: Float
        get() = hitbox.cX + BAR_WIDTH / 2.0f //绘制后会旋转，所以实际上的绘制位置更靠左

    override fun drawBarShadow(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float) {
        ImgUtility.draw(
            sb,
            ImageMaster.HB_SHADOW_L,
            x,
            y + startLength - BAR_DIAMETER,
            BAR_DIAMETER,
            BAR_BG_WIDTH,
            ROTATION.toFloat()
        )
        ImgUtility.draw(sb, ImageMaster.HB_SHADOW_B, x, y + startLength, length, BAR_BG_WIDTH, ROTATION.toFloat())
        ImgUtility.draw(
            sb,
            ImageMaster.HB_SHADOW_R,
            x,
            y + startLength + length,
            BAR_DIAMETER,
            BAR_BG_WIDTH,
            ROTATION.toFloat()
        )
    }

    override fun drawBarMaxEnd(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float) {
        ImgUtility.draw(
            sb,
            ImageMaster.HEALTH_BAR_R,
            x,
            y + startLength + length,
            BAR_DIAMETER,
            BAR_WIDTH,
            ROTATION.toFloat()
        )
    }

    override fun drawBarMiddle(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float) {
        ImgUtility.draw(sb, ImageMaster.HEALTH_BAR_B, x, y + startLength, length, BAR_WIDTH, ROTATION.toFloat())
    }

    override fun drawBarMinEnd(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float) {
        ImgUtility.draw(
            sb,
            ImageMaster.HEALTH_BAR_L,
            x,
            y + startLength - BAR_DIAMETER,
            BAR_DIAMETER,
            BAR_WIDTH,
            ROTATION.toFloat()
        )
    }

    override fun renderBarText(sb: SpriteBatch?, x: Float, y: Float) {
        FontHelper.renderFontCentered(
            sb, FontHelper.healthInfoFont, makeBarText(),
            hitbox.cX,
            y + BAR_DIAMETER + chunkLength(this.totalAmount) + TEXT_OFFSET_Y,
            this.barTextColor,
            0.8f
        )
    }

    override fun chunkHitBoxReSize(amountChunk: BarAmountChunk) {
        when (amountChunk.orderType) {
            OrderType.Min -> {
                amountChunk.hitbox.height = amountChunk.length + BAR_DIAMETER / 2
                amountChunk.hitbox.y =
                    amountChunk.drawY + amountChunk.startLength - BAR_DIAMETER / 2
            }

            OrderType.Middle -> {
                amountChunk.hitbox.height = amountChunk.length
                amountChunk.hitbox.y = amountChunk.drawY + amountChunk.startLength
            }

            OrderType.Max -> {
                amountChunk.hitbox.height = amountChunk.length + BAR_DIAMETER / 2
                amountChunk.hitbox.y = amountChunk.drawY + amountChunk.startLength
            }

            OrderType.OnlyOne -> {
                amountChunk.hitbox.height = amountChunk.length + BAR_DIAMETER
                amountChunk.hitbox.y =
                    amountChunk.drawY + amountChunk.startLength - BAR_DIAMETER / 2
            }
        }
        amountChunk.hitbox.width = BAR_WIDTH
        amountChunk.hitbox.moveX(hitbox.cX)
    }

    companion object {
        const val ROTATION: Int = 90
        val BAR_WIDTH: Float = BAR_DIAMETER * 2.1f
        val BAR_BG_WIDTH: Float = BAR_WIDTH * 0.87f
    }
}
