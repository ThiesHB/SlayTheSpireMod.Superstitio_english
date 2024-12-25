package superstitioapi.pet

import basemod.ReflectionHacks
import basemod.abstracts.CustomMonster
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.input.InputHelper
import superstitioapi.pet.animationSize.AnimationSize

abstract class Minion protected constructor(petCore: AbstractCreature, drawScale: Float) : CustomMonster(
    petCore.name, petCore.id, petCore.maxHealth,
    petCore.hb_x, petCore.hb_y, petCore.hb_w / drawScale, petCore.hb_h / drawScale, null, 0f, 0f, true
)
{
    protected val drawScale: Float
    val petCore: AbstractCreature
    protected var petCoreHitbox: Hitbox? = null
    private var oldMX = 0f
    private var oldMY = 0f
    private var simpleAnim = 0.0f

    init
    {
        this.animX = 0.0f
        this.animY = 0.0f
        this.drawScale = drawScale
        this.petCore = petCore
        if (ReflectionHacks.getPrivate<Any?>(petCore, AbstractCreature::class.java, "atlas") != null) try
        {
            AnimationSize.reloadAnimation(petCore, drawScale)
        }
        catch (e: Exception)
        {
            this.hb_w *= drawScale
            this.hb_h *= drawScale
        }
        this.dialogX = this.petCore.dialogX
        this.dialogY = this.petCore.dialogY

        ReflectionHacks.privateMethod(AbstractCreature::class.java, "refreshHitboxLocation").invoke<Any>(petCore)

        this.maxHealth = petCore.maxHealth / HEALTH_DIV
        this.currentHealth = maxHealth
        this.flipHorizontal = true
        this.petCore.maxHealth = petCore.maxHealth / HEALTH_DIV
        this.petCore.currentHealth = maxHealth
        this.petCore.flipHorizontal = true
        this.tips = ReflectionHacks.getPrivate(petCore, AbstractCreature::class.java, "tips")

        this.img = setupImg()
    }

    constructor(petCore: AbstractCreature) : this(petCore, DEFAULT_DRAW_SCALE)

    open val isHovered: Boolean
        get() = isCreatureHovered(this) || isCreatureHovered(this.petCore)

    open fun updateHitBox()
    {
        petCore.hb.update()
        petCore.healthHb.update()
    }

    protected abstract fun setupImg(): Texture

    protected abstract fun updatePetCore()

    protected fun Drag_Press()
    {
        this.oldMX = InputHelper.mX.toFloat()
        this.oldMY = InputHelper.mY.toFloat()
    }

    protected fun Drag_Release()
    {
        this.refreshHitboxLocation()
        ReflectionHacks.privateMethod(AbstractCreature::class.java, "refreshHitboxLocation").invoke<Any>(petCore)
        this.oldMX = 0.0f
        this.oldMY = 0.0f
    }

    protected fun Drag_Hold()
    {
        if (this.oldMX != 0.0f && this.oldMY != 0.0f)
        {
            val xDiff = InputHelper.mX - this.oldMX
            val yDiff = InputHelper.mY - this.oldMY
            this.drawX += xDiff
            petCore.drawX += xDiff
            this.drawY += yDiff
            petCore.drawY += yDiff
        }
        this.oldMX = InputHelper.mX.toFloat()
        this.oldMY = InputHelper.mY.toFloat()
    }

    private fun refreshHitBox()
    {
        petCore.hb_x = this.hb_x
        petCore.hb_y = this.hb_y
        petCore.hb = Hitbox(this.hb_w, this.hb_h)
        petCore.hb_w = petCore.hb.width
        petCore.hb_h = petCore.hb.height
        petCore.hb.move(petCore.drawX, petCore.drawY)
        this.petCoreHitbox = petCore.hb
    }

    private fun drawImg(sb: SpriteBatch)
    {
        simpleAnim += Gdx.graphics.deltaTime
        if (simpleAnim >= 1.0f) simpleAnim = 0f
        val scaleX = 1.0f
        val v = 0.005f * MathUtils.sinDeg(MathUtils.sinDeg(simpleAnim * 360) * 15)
        val scaleY = 1.0f + v
        val rotation = 0f
        sb.draw(
            this.img, this.drawX - img.width.toFloat() / drawScale * Settings.scale / 2.0f + this.animX,
            this.drawY + hb.height * v,
            0f, 0f,
            img.width.toFloat() / drawScale * Settings.scale,
            img.height.toFloat() / drawScale * Settings.scale,
            scaleX, scaleY, rotation,
            0, 0, img.width, img.height, this.flipHorizontal, this.flipVertical
        )
    }

    abstract override fun createIntent()

    override fun init()
    {
        petCore.drawX = drawX
        petCore.drawY = drawY
        refreshHitBox()
        petCore.healthHb = Hitbox(this.hb_w, 72.0f * Settings.scale)
        ReflectionHacks.privateMethod(AbstractCreature::class.java, "refreshHitboxLocation").invoke<Any>(
            petCore
        )
    }

    override fun showHealthBar()
    {
        petCore.showHealthBar()
    }

    override fun damage(info: DamageInfo?)
    {
        super.damage(info)
        petCore.damage(info)
    }

    override fun render(sb: SpriteBatch)
    {
        if (this.img != null) petCore.tint.color.a = 0f
        petCore.render(sb)
        if (this.isDead || this.escaped) return
        if (this.atlas != null)
        {
            return
        }
        sb.color = tint.color
        if (this.img != null)
        {
            drawImg(sb)
        }
        //        if (this == AbstractDungeon.getCurrRoom().monsters.hoveredMonster) {
//            sb.setBlendFunction(770, 1);
//            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.1F));
//            if (this.img != null) {
//                drawImg(sb);
//                sb.setBlendFunction(770, 771);
//            }
//        }
    }

    abstract override fun applyPowers()

    override fun update()
    {
        if (petCore.hb !== petCoreHitbox) refreshHitBox()
        super.update()
        updatePetCore()

        if (!AbstractDungeon.player.isDraggingCard && AbstractDungeon.player.hoveredCard == null && isHovered)
        {
            if (InputHelper.justClickedLeft)
            {
                this.Drag_Press()
            }
            else if (InputHelper.isMouseDown)
            {
                this.Drag_Hold()
            }
            else if (InputHelper.justReleasedClickLeft)
            {
                this.Drag_Release()
            }
        }
    }

    abstract override fun renderTip(sb: SpriteBatch)

    abstract override fun takeTurn()

    override fun rollMove()
    {
        this.getMove(AbstractDungeon.aiRng.random(99))
    }

    abstract override fun updatePowers()

    abstract override fun usePreBattleAction()

    abstract override fun useUniversalPreBattleAction()

    abstract override fun getMove(i: Int)

    companion object
    {
        const val HEALTH_DIV: Int = 10
        const val DEFAULT_DRAW_SCALE: Float = 3f
        fun isCreatureHovered(creature: AbstractCreature): Boolean
        {
            return creature.hb.hovered || creature.healthHb.hovered
        }
    }
}
