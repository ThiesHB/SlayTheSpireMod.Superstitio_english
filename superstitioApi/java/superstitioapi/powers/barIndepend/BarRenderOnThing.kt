package superstitioapi.powers.barIndepend

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.*
import superstitioapi.powers.barIndepend.BarRenderOnThing.BarAmountChunk.OrderType
import superstitioapi.powers.barIndepend.BarRenderUpdateMessage.ToolTip
import java.util.function.Consumer
import java.util.function.Supplier

open class BarRenderOnThing(hitbox: Supplier<Hitbox>, power: HasBarRenderOnCreature) : RenderOnThing(hitbox, power) {
    //    public static final float HITBOX_HEIGHT = ;
    //    protected static final float BG_OFFSET_Y = -31.0f * Settings.scale;
    var barBgColor: Color
    var barShadowColor: Color
    var barLength: Float
    private var isChunkHovered = false

    init {
        this.hitbox = Hitbox(hitboxBondTo.get().width + BAR_DIAMETER * 3f, BAR_DIAMETER * 1.5f)
        updateHitBoxPlace(this.hitbox)
        this.barLength = hitboxBondTo.get().width

        this.barBgColor = Color(0f, 0f, 0f, 0.3f)
        this.barShadowColor = Color(0f, 0f, 0f, 0.3f)
    }

    protected open fun drawBarMaxEnd(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float) {
        sb.draw(ImageMaster.HEALTH_BAR_R, x + startLength + length, y, BAR_DIAMETER, BAR_DIAMETER)
    }

    protected open fun drawBarMiddle(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float) {
        sb.draw(ImageMaster.HEALTH_BAR_B, x + startLength, y, length, BAR_DIAMETER)
    }

    protected open fun drawBarMinEnd(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float) {
        sb.draw(ImageMaster.HEALTH_BAR_L, x + startLength - BAR_DIAMETER, y, BAR_DIAMETER, BAR_DIAMETER)
    }

    protected fun chunkLength(amount: Int): Float {
        var v = (this.barLength * (amount % maxBarAmount)) / maxBarAmount.toFloat()
        if (amount % maxBarAmount == 0 && amount != 0) v = this.barLength
        return v
    }

    protected open fun renderBar(sb: SpriteBatch) {
        this.renderAmountBarBackGround(sb, xDrawStart, yDrawStart)
        sortedChunkList.forEach(Consumer { amountChunk: AmountChunk -> amountChunk.render(sb) })
    }

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    protected fun renderAmountBarBackGround(sb: SpriteBatch, x: Float, y: Float) {
        sb.color = barShadowColor
        this.drawBarShadow(sb, x, y, 0f, this.barLength)
        sb.color = barBgColor
        this.drawBar(sb, x, y, 0f, this.barLength)
    }

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    protected open fun drawBarShadow(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float) {
        sb.draw(ImageMaster.HB_SHADOW_L, x + startLength - BAR_DIAMETER, y, BAR_DIAMETER, BAR_DIAMETER)
        sb.draw(ImageMaster.HB_SHADOW_B, x + startLength, y, length, BAR_DIAMETER)
        sb.draw(ImageMaster.HB_SHADOW_R, x + startLength + length, y, BAR_DIAMETER, BAR_DIAMETER)
    }

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    protected fun drawBar(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float) {
        drawBarMinEnd(sb, x, y, startLength, length)
        drawBarMiddle(sb, x, y, startLength, length)
        drawBarMaxEnd(sb, x, y, startLength, length)
    }

    protected fun drawBarChunk(sb: SpriteBatch, chunk: BarAmountChunk) {
        when (chunk.orderType) {
            OrderType.Min -> {
                drawBarMinEnd(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length)
                drawBarMiddle(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length)
            }

            OrderType.Middle -> drawBarMiddle(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length)
            OrderType.Max -> {
                drawBarMiddle(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length)
                drawBarMaxEnd(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length)
            }

            OrderType.OnlyOne -> {
                drawBarMinEnd(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length)
                drawBarMiddle(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length)
                drawBarMaxEnd(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length)
            }
        }
    }

    protected fun barAmountChunkMoveByIndex(barAmountChunk: BarAmountChunk, totalAmountInFront: Int) {
        barAmountChunk.teleport(xDrawStart, yDrawStart)
        barAmountChunk.targetNewLength(
            chunkLength(totalAmountInFront),
            chunkLength(barAmountChunk.nowAmount)
        )
    }

    protected open fun chunkHitBoxReSize(amountChunk: BarAmountChunk) {
        when (amountChunk.orderType) {
            OrderType.Min -> {
                amountChunk.hitbox.width = amountChunk.length + BAR_DIAMETER / 2
                amountChunk.hitbox.x = amountChunk.drawX + amountChunk.startLength - BAR_DIAMETER / 2
            }

            OrderType.Middle -> {
                amountChunk.hitbox.width = amountChunk.length
                amountChunk.hitbox.x = amountChunk.drawX + amountChunk.startLength
            }

            OrderType.Max -> {
                amountChunk.hitbox.width = amountChunk.length + BAR_DIAMETER / 2
                amountChunk.hitbox.x = amountChunk.drawX + amountChunk.startLength
            }

            OrderType.OnlyOne -> {
                amountChunk.hitbox.width = amountChunk.length + BAR_DIAMETER
                amountChunk.hitbox.x = amountChunk.drawX + amountChunk.startLength - BAR_DIAMETER / 2
            }
        }
        amountChunk.hitbox.height = BAR_DIAMETER
        amountChunk.hitbox.moveY(hitbox.cY)
    }

    private fun calculateAllPosition() {
        reMakeSortedChunkList()
        for (i in sortedChunkList.indices) {
            val chunk = sortedChunkList[i] as? BarAmountChunk ?: continue
            val barAmountChunk = chunk
            barAmountChunkMoveByIndex(barAmountChunk, getTotalAmount_InFrontOf(i))

            if (i == 0) barAmountChunk.orderType = OrderType.Min
            else if (i == sortedChunkList.size - 1) barAmountChunk.orderType = OrderType.Max
            else barAmountChunk.orderType = OrderType.Middle
            if (sortedChunkList.size == 1) barAmountChunk.orderType = OrderType.OnlyOne
        }
    }

    override fun render(sb: SpriteBatch) {
        renderBar(sb)
        renderBarTextWithColorAlphaChange(sb, xDrawStart, yDrawStart)
        if (Settings.isDebug || Settings.isInfo) {
            renderDebug(sb)
        }
    }

    override fun update() {
        updateHitBoxPlace(this.hitbox)
        hitbox.update()
        val chunkList = this.sortedChunkList
        for (i in chunkList.indices.reversed()) {
            val amountChunk = chunkList[i]
            amountChunk.update()
            if (amountChunk is BarAmountChunk) {
                amountChunk.teleport(xDrawStart, yDrawStart)
                amountChunk.updateHitBox()
            }
        }
        update_showTips(this.hitbox)
        updateHbHoverFade()
    }

    override fun tryApplyMessage(message: BarRenderUpdateMessage) {
        super.tryApplyMessage(message)
        calculateAllPosition()
    }

    override fun removeChunk(hasBarRenderOnCreature: HasBarRenderOnCreature) {
        super.removeChunk(hasBarRenderOnCreature)
        calculateAllPosition()
    }

    override fun renderDebug(sb: SpriteBatch?) {
        hitbox.render(sb)
        for (amountChunk in amountChunkWithUuid.values) {
            if (amountChunk is BarAmountChunk) amountChunk.hitbox.render(sb)
        }
    }

    override fun update_showTips(hitbox: Hitbox?) {
        if (hitbox!!.hovered && !isChunkHovered) {
            renderTip(AllTips())
        }
        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7f)
    }

    override val yDrawStart: Float
        get() = hitbox.cY - BAR_DIAMETER / 2.0f

    override val xDrawStart: Float
        get() = hitbox.cX - this.barLength / 2.0f

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    override fun renderBarTextWithColorAlphaChange(sb: SpriteBatch?, x: Float, y: Float) {
        val tmp = barTextColor.a
        barTextColor.a *= this.healthHideTimer
        renderBarText(sb, x, y)
        barTextColor.a = tmp
    }

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    override fun renderBarText(sb: SpriteBatch?, x: Float, y: Float) {
        FontHelper.renderFontCentered(
            sb, FontHelper.healthInfoFont, makeBarText(), hitbox.cX, y + TEXT_OFFSET_Y,
            this.barTextColor
        )
    }

    override fun updateHbHoverFade() {
        if (hitbox.hovered) {
            this.healthHideTimer -= Gdx.graphics.deltaTime * HIDE_SPEED
            if (this.healthHideTimer < HEALTH_HIDE_TIMER_MIN) {
                this.healthHideTimer = HEALTH_HIDE_TIMER_MIN
            }
        } else {
            this.healthHideTimer += Gdx.graphics.deltaTime * HIDE_SPEED
            if (this.healthHideTimer > 1.0f) {
                this.healthHideTimer = 1.0f
            }
        }
    }

    override fun makeNewAmountChunk(message: BarRenderUpdateMessage): AmountChunk? {
        return BarAmountChunk(
            xDrawStart,
            yDrawStart,
            chunkLength(totalAmount),
            chunkLength(message.newAmount),
            nextOrder, this
        )
    }

    protected class BarAmountChunk(
        var drawX: Float,
        var drawY: Float,
        var startLength: Float,
        length: Float,
        order: Int,
        protected val bar: BarRenderOnThing
    ) : AmountChunk(order) {
        var chunkColor: Color? = Color(0f, 0f, 0f, 1f)

        var hitbox: Hitbox
        protected var drawXTarget: Float
        protected var drawYTarget: Float
        var length: Float = 0f
        protected var lengthTarget: Float
        protected var animTimer: Float = 0f
        protected var healthHideTimer: Float = 1.0f
        protected var startLengthTarget: Float = 0f
        var orderType: OrderType = OrderType.OnlyOne

        init {
            this.drawXTarget = drawX
            this.drawYTarget = drawY
            this.lengthTarget = length
            this.hitbox = Hitbox(0f, 0f)
            //            hitboxMove();
        }

        //        private void hitboxMove() {
        //            this.hitbox.move(drawX, drawY);
        //        }
        fun updateHitBox() {
            bar.chunkHitBoxReSize(this)
            hitbox.update()
            bar.isChunkHovered = hitbox.hovered
            updateHbHoverFade()
            if (hitbox.hovered)
                bar.renderTip(ArrayList(listOfNotNull(tip)))
        }

        fun teleport(drawX: Float, drawY: Float): BarAmountChunk {
            this.drawX = drawX
            this.drawY = drawY
            this.drawXTarget = drawX
            this.drawYTarget = drawY
            return this
        }

        fun move(drawXTarget: Float, drawYTarget: Float) {
            this.drawXTarget = drawXTarget
            this.drawYTarget = drawYTarget
        }

        fun targetNewLength(startLengthTarget: Float, lengthTarget: Float) {
            this.startLengthTarget = startLengthTarget
            this.lengthTarget = lengthTarget
        }

        private fun updateHbHoverFade() {
            if (hitbox.hovered) {
                this.healthHideTimer -= Gdx.graphics.deltaTime * HIDE_SPEED
                if (this.healthHideTimer < HEALTH_HIDE_TIMER_MIN_CHUNK) {
                    this.healthHideTimer = HEALTH_HIDE_TIMER_MIN_CHUNK
                }
            } else {
                this.healthHideTimer += Gdx.graphics.deltaTime * HIDE_SPEED
                if (this.healthHideTimer > 1.0f) {
                    this.healthHideTimer = 1.0f
                }
            }
        }

        private fun renderAmountBar(sb: SpriteBatch) {
            if (this.nowAmount == 0) return

            val tmp = chunkColor!!.a
            chunkColor!!.a *= this.healthHideTimer
            sb.color = chunkColor
            bar.drawBarChunk(sb, this)
            chunkColor!!.a = tmp
        }

        private fun updateHbDamageAnimation() {
            if (this.animTimer > 0.0f) this.animTimer -= Gdx.graphics.deltaTime
            //            if (this.animTimer > 0.0F) return;
            if (this.startLengthTarget != this.startLength) this.startLength =
                MathHelper.uiLerpSnap(this.startLength, this.startLengthTarget)
            if (this.lengthTarget != this.length) this.length = MathHelper.uiLerpSnap(this.length, this.lengthTarget)
            if (this.drawXTarget != this.drawX) this.drawX = MathHelper.uiLerpSnap(this.drawX, this.drawXTarget)
            if (this.drawYTarget != this.drawY) this.drawY = MathHelper.uiLerpSnap(this.drawY, this.drawYTarget)
        }

        override fun render(sb: SpriteBatch) {
            renderAmountBar(sb)
        }

        override fun update() {
            updateHbDamageAnimation()
        }

        override fun setTip(tip: ToolTip): BarAmountChunk {
            this.tip = PowerTip(tip.name, tip.description)
            return this
        }

        override fun setMaxAmount(maxAmount: Int): BarAmountChunk {
            this.maxAmount = maxAmount
            return this
        }

        override fun setNowAmount(nowAmount: Int): BarAmountChunk {
            this.nowAmount = nowAmount
            return this
        }

        override fun applyNoPositionMessage(message: BarRenderUpdateMessage): BarAmountChunk {
            if (message.newAmount != this.nowAmount) {
                this.animTimer = TIME_RESET
                this.nowAmount = message.newAmount
            }
            if (message.maxAmount != 0) this.maxAmount = message.maxAmount
            if (message.chunkColor != null) this.chunkColor = message.chunkColor
            if (message.toolTip != null) this.tip = PowerTip(message.toolTip!!.name, message.toolTip!!.description)
            return this
        }

        sealed class OrderType {
            data object Min : OrderType()
            data object Middle : OrderType()
            data object Max : OrderType()
            data object OnlyOne : OrderType()
            companion object {
                fun values(): Array<OrderType> {
                    return arrayOf(Min, Middle, Max, OnlyOne)
                }

                fun valueOf(value: String): OrderType {
                    return when (value) {
                        "Min" -> Min
                        "Middle" -> Middle
                        "Max" -> Max
                        "OnlyOne" -> OnlyOne
                        else -> throw IllegalArgumentException("No object superstitioapi.powers.barIndepend.BarRenderOnThing.BarAmountChunk.OrderType.$value")
                    }
                }
            }
        }

        companion object {
            const val HEALTH_HIDE_TIMER_MIN_CHUNK: Float = 0.7f
            protected const val TIME_RESET: Float = 1.2f
        }
    }

    companion object {
        const val HIDE_SPEED: Float = 4.0f
        const val HEALTH_HIDE_TIMER_MIN: Float = 0.2f
         val BAR_OFFSET_Y: Float = 28.0f * Settings.scale
         val TEXT_OFFSET_Y: Float = 11.0f * Settings.scale

        //绘制相关
         val BAR_DIAMETER: Float = 20.0f * Settings.scale
    }
}