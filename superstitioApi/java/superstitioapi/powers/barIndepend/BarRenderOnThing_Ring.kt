package superstitioapi.powers.barIndepend

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.input.InputHelper
import superstitioapi.powers.barIndepend.BarRenderOnThing.BarAmountChunk.OrderType
import superstitioapi.shader.RingShader
import superstitioapi.shader.ShaderUtility
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.math.atan2
import kotlin.math.sqrt

open class BarRenderOnThing_Ring(hitbox: Supplier<Hitbox>, power: HasBarRenderOnCreature) :
    BarRenderOnThing(hitbox, power)
{
    var initDegree: Float
    var barAverageRadius_renormalization: Float
    var barHalfThick_renormalization: Float
    var barSize: Float
    var barEndDegree: Float

    //    protected ShaderProgram shader;
    init
    {
        this.barLength = BAR_DEGREE
        this.initDegree = INIT_DEGREE
        this.barSize = BAR_SIZE
        this.barHalfThick_renormalization = BAR_HALF_THICK_RENORMALIZATION
        this.barAverageRadius_renormalization = BAR_AVERAGE_RADIUS_RENORMALIZATION
        this.barEndDegree = BAR_TOP_DEGREE
        val ringHitBox = RingHitBox(this)
        ringHitBox.halfThick *= 1.1f
        ringHitBox.averageRadius *= 1.1f
        this.hitbox = ringHitBox
        this.hitbox.width = barSize
        this.hitbox.height = barSize
        updateHitBoxPlace(this.hitbox)
        //        shader = RingShader.ringShader_useHalfPic;
    }

    private fun setUpShader(sb: SpriteBatch, startLength: Float, length: Float)
    {
        RingShader.setUp_ringShader_useHalfPic(
            sb,
            barAverageRadius_renormalization,
            barHalfThick_renormalization,
            startLength,
            length
        )
    }

    override fun render(sb: SpriteBatch)
    {
        renderBar(sb)
        renderBarText(sb, xDrawStart, yDrawStart)
        if (Settings.isDebug || Settings.isInfo)
        {
            renderDebug(sb)
        }
    }

    override val yDrawStart: Float
        get() = hitbox.cY - barSize / 2.0f

    override val xDrawStart: Float
        get() = hitbox.cX - barSize / 2.0f

    override fun renderBar(sb: SpriteBatch)
    {
        ShaderUtility.originShader = sb.shader
        this.renderAmountBarBackGround(sb, xDrawStart, yDrawStart)
        amountChunkWithUuid.values.forEach(Consumer { amountChunk: AmountChunk -> amountChunk.render(sb) })
        sb.shader = ShaderUtility.originShader
    }

    override fun drawBarMinEnd(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float)
    {
        if (length >= 360.0f) return
        setUpShader(
            sb,
            initDegree + startLength - barEndDegree,
            barEndDegree
        )
        sb.draw(ImageMaster.HEALTH_BAR_L, x, y, barSize, barSize)
    }

    override fun drawBarMiddle(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float)
    {
        setUpShader(
            sb,
            initDegree + startLength,
            length
        )
        sb.draw(ImageMaster.HEALTH_BAR_B, x, y, barSize, barSize)
    }

    override fun drawBarMaxEnd(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float)
    {
        if (length >= 360.0f) return
        setUpShader(
            sb,
            initDegree + startLength + length,
            barEndDegree
        )
        sb.draw(ImageMaster.HEALTH_BAR_R, x, y, barSize, barSize)
    }

    override fun drawBarShadow(sb: SpriteBatch, x: Float, y: Float, startLength: Float, length: Float)
    {
        setUpShader(
            sb,
            initDegree + startLength - barEndDegree,
            barEndDegree
        )
        sb.draw(ImageMaster.HB_SHADOW_L, x, y, barSize, barSize)
        setUpShader(
            sb,
            initDegree + startLength,
            length
        )
        sb.draw(ImageMaster.HB_SHADOW_B, x, y, barSize, barSize)
        setUpShader(
            sb,
            initDegree + startLength + length,
            barEndDegree
        )
        sb.draw(ImageMaster.HB_SHADOW_R, x, y, barSize, barSize)
    }

    override fun makeNewAmountChunk(message: BarRenderUpdateMessage): AmountChunk?
    {
        val amountChunk = super.makeNewAmountChunk(message)
        if (amountChunk !is BarAmountChunk) return amountChunk
        val ringHitBox = RingHitBox(this)
        ringHitBox.startDegree = initDegree + amountChunk.startLength - this.barEndDegree
        ringHitBox.lengthDegree = this.barEndDegree * 2 + amountChunk.length
        ringHitBox.averageRadius = this.barSize * barAverageRadius_renormalization
        ringHitBox.halfThick = this.barSize * barHalfThick_renormalization
        amountChunk.hitbox = ringHitBox
        return amountChunk
    }

    override fun chunkHitBoxReSize(amountChunk: BarAmountChunk)
    {
        if (amountChunk.hitbox !is RingHitBox)
        {
            super.chunkHitBoxReSize(amountChunk)
            return
        }
        val ringHitBox = amountChunk.hitbox as RingHitBox

        when (amountChunk.orderType)
        {
            OrderType.Min     ->
            {
                ringHitBox.startDegree = initDegree + amountChunk.startLength - this.barEndDegree
                ringHitBox.lengthDegree = this.barEndDegree + amountChunk.length
            }

            OrderType.Middle  ->
            {
                ringHitBox.startDegree = initDegree + amountChunk.startLength
                ringHitBox.lengthDegree = amountChunk.length
            }

            OrderType.Max     ->
            {
                ringHitBox.startDegree = initDegree + amountChunk.startLength
                ringHitBox.lengthDegree = this.barEndDegree + amountChunk.length
            }

            OrderType.OnlyOne ->
            {
                ringHitBox.startDegree = initDegree + amountChunk.startLength - this.barEndDegree
                ringHitBox.lengthDegree = this.barEndDegree * 2 + amountChunk.length
            }
        }
        ringHitBox.averageRadius = this.barSize * barAverageRadius_renormalization
        ringHitBox.halfThick = this.barSize * barHalfThick_renormalization
        ringHitBox.width = this.barSize
        ringHitBox.height = this.barSize
        ringHitBox.move(hitbox.cX, hitbox.cY)
    }

    class RingHitBox(var averageRadius: Float, var halfThick: Float, var startDegree: Float, var lengthDegree: Float) :
        Hitbox(2 * (averageRadius + halfThick), 2 * (averageRadius + halfThick))
    {
        constructor(ring: BarRenderOnThing_Ring) : this(
            ring.barSize * ring.barAverageRadius_renormalization,
            ring.barSize * ring.barHalfThick_renormalization,
            ring.initDegree - ring.barEndDegree,
            ring.barEndDegree * 2 + ring.barLength
        )

        private fun isHovered(x: Float, y: Float): Boolean
        {
            val mx = InputHelper.mX - this.cX
            val my = InputHelper.mY - this.cY
            var theta = (MathUtils.radiansToDegrees * atan2(my.toDouble(), -mx.toDouble())).toFloat()
            val radius = sqrt((mx * mx + my * my).toDouble()).toFloat()
            theta += 180.0f
            theta = Math.floorMod(theta.toLong(), 360.0.toLong()).toFloat()
            theta -= 180.0f

            return (((startDegree < theta && theta < startDegree + lengthDegree)
                    || (startDegree < theta - 360 && theta - 360 < startDegree + lengthDegree)
                    || (startDegree < theta + 360 && theta + 360 < startDegree + lengthDegree))
                    && (averageRadius - halfThick < radius) && (radius < averageRadius + halfThick))
        }

        override fun update(x: Float, y: Float)
        {
            if (AbstractDungeon.isFadingOut) return
            this.translate(x, y)
            if (this.justHovered)
            {
                this.justHovered = false
            }

            if (this.hovered)
            {
                this.hovered = isHovered(x, y)
            }
            else
            {
                this.hovered = isHovered(x, y)
                if (this.hovered)
                {
                    this.justHovered = true
                }
            }
        }

        override fun render(sb: SpriteBatch)
        {
            if (!Settings.isDebug && !Settings.isInfo) return
            if (this.clickStarted)
            {
                sb.color = Color.CHARTREUSE
            }
            else
            {
                sb.color = Color.RED
            }

            ShaderUtility.originShader = sb.shader
            RingShader.setUp_ringShader_useHalfPic(
                sb,
                startDegree,
                lengthDegree,
                averageRadius / width,
                halfThick / width
            )
            //            sb.setShader(RingShader.ringShader);
//            sb.getShader().setUniformf("u_degreeStart", startDegree);
//            sb.getShader().setUniformf("u_degreeLength", lengthDegree);
//            sb.getShader().setUniformf("u_radius", averageRadius / width);
//            sb.getShader().setUniformf("u_halfThick", halfThick / width);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, this.x, this.y, this.width, this.height)
            sb.shader = ShaderUtility.originShader
        }
    }

    companion object
    {
        protected val BAR_SIZE: Float = BAR_DIAMETER * 6.0f
        protected val BAR_THICK: Float = BAR_DIAMETER * 1.2f
        protected val BAR_HALF_THICK_RENORMALIZATION: Float = BAR_THICK / 2.0f / BAR_SIZE

        //实际上是内径和外径的平均值
        protected val BAR_AVERAGE_RADIUS_RENORMALIZATION: Float = 0.5f - BAR_HALF_THICK_RENORMALIZATION
        protected val BAR_TOP_DEGREE: Float =
            BAR_HALF_THICK_RENORMALIZATION * 2 / (BAR_AVERAGE_RADIUS_RENORMALIZATION * 3.14f / 180.0f)
        protected const val BAR_DEGREE: Float = 240f
        protected const val INIT_DEGREE: Float = -30f
        protected const val DEGREE_OFFSET: Float = 90f
        private val BAR_SHADOW_L = TextureRegion(ImageMaster.HB_SHADOW_L)
        private val BAR_SHADOW_B = TextureRegion(ImageMaster.HB_SHADOW_B)
        private val BAR_SHADOW_R = TextureRegion(ImageMaster.HB_SHADOW_R)
        private val BAR_L = TextureRegion(ImageMaster.HEALTH_BAR_L)
        private val BAR_B = TextureRegion(ImageMaster.HEALTH_BAR_B)
        private val BAR_R = TextureRegion(ImageMaster.HEALTH_BAR_R)
    }
}
