package superstitioapi.powers.barIndepend

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.MathHelper
import com.megacrit.cardcrawl.helpers.PowerTip
import superstitioapi.powers.barIndepend.BarRenderUpdateMessage.ToolTip
import superstitioapi.utils.TipsUtility
import java.util.function.Supplier

abstract class RenderOnThing(protected val hitboxBondTo: Supplier<Hitbox>, power: HasBarRenderOnCreature)
{
    //    public static final float HITBOX_HEIGHT = ;
    //    protected static final float BG_OFFSET_Y = -31.0f * Settings.scale;
    protected val amountChunkWithUuid: MutableMap<String, AmountChunk> = HashMap()
    protected val HeightOffset: Float
    var barTextColor: Color
    var hitbox: Hitbox = Hitbox(hitboxBondTo.get().width + BAR_DIAMETER * 3f, BAR_DIAMETER * 1.5f)
    var uuid_self: String = power.uuidPointTo()
    protected var fontScale: Float = 0f
    protected var healthHideTimer: Float = 1.0f
    protected var sortedChunkList: List<AmountChunk> = java.util.ArrayList()
    private var rawBarText: String? = "%d/%d"

    init
    {
        updateHitBoxPlace(this.hitbox)

        this.HeightOffset = power.Height()
        this.barTextColor = Color(1.0f, 1.0f, 1.0f, 1.0f)
    }

    open fun removeChunk(hasBarRenderOnCreature: HasBarRenderOnCreature)
    {
        amountChunkWithUuid.remove(hasBarRenderOnCreature.uuidOfSelf())
        reMakeSortedChunkList()
    }

    open fun updateHitBoxPlace(hitbox: Hitbox)
    {
        hitbox.move(
            hitboxBondTo.get().cX,
            HeightOffset + hitboxBondTo.get().cY + hitboxBondTo.get().height / 2
        )
    }

    fun isUuidInThis(uuid_chunk: String?): Boolean
    {
        return amountChunkWithUuid.containsKey(uuid_chunk)
    }

    open fun render(sb: SpriteBatch)
    {
        renderBarTextWithColorAlphaChange(sb, xDrawStart, yDrawStart)
        if (Settings.isDebug || Settings.isInfo)
        {
            renderDebug(sb)
        }
    }

    open fun update()
    {
        updateHitBoxPlace(this.hitbox)
        hitbox.update()
        for (amountChunk in amountChunkWithUuid.values)
        {
            amountChunk.update()
        }
        update_showTips(this.hitbox)
        updateHbHoverFade()
    }

    fun renderTip(tips: MutableList<PowerTip>)
    {
        TipsUtility.renderTipsWithMouse(tips)
    }

    /**
     * 在初始化后，如果想要修改，请使用这个函数
     *
     * @param message 消息
     */
    open fun tryApplyMessage(message: BarRenderUpdateMessage)
    {
        if (uuid_self != message.uuidOfBar) return
        if (!amountChunkWithUuid.containsKey(message.uuidOfPower))
        {
            addNewAmountChunk(message)
            return
        }

        val messageTargetChunk = amountChunkWithUuid[message.uuidOfPower]
        this.rawBarText = message.rawTextOnBar
        messageTargetChunk!!.applyNoPositionMessage(message)
        if (message.detail != null) message.detail!!.accept(this)
    }

    protected val maxBarAmount: Int
        get() = amountChunkWithUuid.values.maxOfOrNull(AmountChunk::maxAmount) ?: 10

    protected open fun renderDebug(sb: SpriteBatch?)
    {
        hitbox.render(sb)
    }

    protected open fun update_showTips(hitbox: Hitbox?)
    {
        if (hitbox!!.hovered)
        {
            renderTip(AllTips())
        }
        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7f)
    }

    protected abstract val yDrawStart: Float

    protected abstract val xDrawStart: Float

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    protected open fun renderBarTextWithColorAlphaChange(sb: SpriteBatch?, x: Float, y: Float)
    {
        val tmp = barTextColor.a
        barTextColor.a *= this.healthHideTimer
        renderBarText(sb, x, y)
        barTextColor.a = tmp
    }

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    protected open fun renderBarText(sb: SpriteBatch?, x: Float, y: Float)
    {
        FontHelper.renderFontCentered(
            sb, FontHelper.healthInfoFont, makeBarText(), hitbox.cX, y + TEXT_OFFSET_Y,
            this.barTextColor
        )
    }

    protected open fun updateHbHoverFade()
    {
        if (hitbox.hovered)
        {
            this.healthHideTimer -= Gdx.graphics.deltaTime * HIDE_SPEED
            if (this.healthHideTimer < HEALTH_HIDE_TIMER_MIN)
            {
                this.healthHideTimer = HEALTH_HIDE_TIMER_MIN
            }
        }
        else
        {
            this.healthHideTimer += Gdx.graphics.deltaTime * HIDE_SPEED
            if (this.healthHideTimer > 1.0f)
            {
                this.healthHideTimer = 1.0f
            }
        }
    }

    protected fun reMakeSortedChunkList()
    {
        val orderedList: MutableList<AmountChunk> = java.util.ArrayList()
        amountChunkWithUuid.values.stream().sorted(AmountChunk::compareTo)
            .forEachOrdered(orderedList::add)
        this.sortedChunkList = orderedList
    }

    protected abstract fun makeNewAmountChunk(message: BarRenderUpdateMessage): AmountChunk?

    protected val nextOrder: Int
        get() = (amountChunkWithUuid.values.maxOfOrNull(AmountChunk::order) ?: -1) + 1

    private fun addNewAmountChunk(message: BarRenderUpdateMessage)
    {
        amountChunkWithUuid[message.uuidOfPower] = makeNewAmountChunk(message)!!.applyNoPositionMessage(message)
    }

    protected fun AllTips(): MutableList<PowerTip>
    {
        return amountChunkWithUuid.values.filter { amountChunk: AmountChunk -> amountChunk.tip != null }
            .mapNotNull(AmountChunk::tip).toMutableList()
    }

    protected fun makeBarText(): String?
    {
        val hasTwoDs = rawBarText!!.contains("%d") && rawBarText!!.indexOf("%d") != rawBarText!!.lastIndexOf("%d")
        val hasD = rawBarText!!.contains("%d")
        if (hasTwoDs) return String.format(
            rawBarText!!,
            totalAmount,
            maxBarAmount
        )
        if (hasD) return String.format(rawBarText!!, this.totalAmount)
        return rawBarText
    }

    protected fun getTotalAmount_InFrontOf(sumToIndex_InFrontOf: Int): Int
    {
        if (sumToIndex_InFrontOf <= 0) return 0
        return getChunkOrdered(sumToIndex_InFrontOf - 1).stream().mapToInt(AmountChunk::nowAmount)
            .sum()
    }

    protected fun getChunkOrdered(index: Int): List<AmountChunk>
    {
        reMakeSortedChunkList()
        if (sortedChunkList.size <= 1) return sortedChunkList
        if (index <= 0) return listOf(sortedChunkList[0])
        return sortedChunkList.subList(0, index + 1)
    }

    protected val totalAmount: Int
        get() = amountChunkWithUuid.values.stream().mapToInt(AmountChunk::nowAmount).sum()

    protected abstract class AmountChunk(val order: Int) : Comparable<AmountChunk>
    {
        var tip: PowerTip? = null
        var maxAmount: Int = 0
        var nowAmount: Int = 0


        abstract fun render(sb: SpriteBatch)

        abstract fun update()

        open fun setTip(tip: ToolTip): AmountChunk
        {
            this.tip = PowerTip(tip.name, tip.description)
            return this
        }

        open fun setMaxAmount(maxAmount: Int): AmountChunk
        {
            this.maxAmount = maxAmount
            return this
        }

        open fun setNowAmount(nowAmount: Int): AmountChunk
        {
            this.nowAmount = nowAmount
            return this
        }

        abstract fun applyNoPositionMessage(message: BarRenderUpdateMessage): AmountChunk

        override fun compareTo(other: AmountChunk): Int
        {
            return this.order.compareTo(other.order)
        }
    }

    companion object
    {
        const val HIDE_SPEED: Float = 4.0f
        const val HEALTH_HIDE_TIMER_MIN: Float = 0.2f
        val BAR_OFFSET_Y: Float = 28.0f * Settings.scale
        val TEXT_OFFSET_Y: Float = 11.0f * Settings.scale

        //绘制相关
        val BAR_DIAMETER: Float = 20.0f * Settings.scale
    }
}