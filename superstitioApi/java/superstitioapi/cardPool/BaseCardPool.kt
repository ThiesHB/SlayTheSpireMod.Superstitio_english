package superstitioapi.cardPool

import basemod.IUIElement
import basemod.ReflectionHacks
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.math.Interpolation
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardType
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.input.InputHelper
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import java.util.function.Predicate

abstract class BaseCardPool(
    protected val poolCover: AbstractCard,
    var target_x: Float,
    var target_y: Float,
    private val addedCard: Predicate<AbstractCard>?,
    private val banedCard: Predicate<AbstractCard>?
) : IUIElement
{
    private val glowList = ArrayList<SafeCardGlowBorder>()
    var isSelect: Boolean = true
    private var glowTimer = 0.0f
    private var isGlowing = false

    constructor(poolCover: IsCardPoolCover, x: Float, y: Float) :
            this(
                poolCover.self,
                x,
                y,
                poolCover.getAddedCard(),
                poolCover.getBanedCard()
            )

    constructor(
        poolCover: AbstractCard,
        x: Float,
        y: Float,
        addedCardList: List<AbstractCard>,
        banedCardList: List<AbstractCard>
    ) : this(
        poolCover,
        x,
        y,
        Predicate<AbstractCard>(addedCardList::contains),
        Predicate<AbstractCard>(banedCardList::contains)
    )

    init
    {
        poolCover.drawScale = COVER_DRAW_SCALE
        poolCover.transparency = 1.0f
        poolCover.current_x = this.target_x
        poolCover.current_y = this.target_y
        poolCover.target_x = this.target_x
        poolCover.target_y = this.target_y

        poolCover.targetDrawScale = COVER_DRAW_SCALE
        poolCover.isCostModified = false
    }

    val id: String
        get() = poolCover.cardID

    fun transportTo(x: Float, y: Float)
    {
        this.target_x = x
        this.target_y = y
        poolCover.current_x = this.target_x
        poolCover.current_y = this.target_y
        poolCover.target_x = this.target_x
        poolCover.target_y = this.target_y
    }

    fun moveTo(x: Float, y: Float)
    {
        this.target_x = x
        this.target_y = y
    }

    fun getAddedCard(): Predicate<AbstractCard?>
    {
        if (addedCard == null || !isSelect)
            return Predicate { false }
        return Predicate {
            if (it == null) return@Predicate false
            else
                return@Predicate addedCard.test(it)
        }
    }

    fun getBanedCard(): Predicate<AbstractCard?>
    {
        if (banedCard == null || !isSelect)
            return Predicate { false }
        return Predicate {
            if (it == null)
                return@Predicate false
            else
                return@Predicate banedCard.test(it)
        }
    }

    //    public HashSet<>
    protected fun clickThisPool()
    {
        this.isSelect = !this.isSelect
    }

    private fun updateGlow()
    {
        if (this.isGlowing)
        {
            this.glowTimer -= Gdx.graphics.deltaTime
            if (this.glowTimer < 0.0f)
            {
                glowList.add(SafeCardGlowBorder(this.poolCover, poolCover.glowColor))
                this.glowTimer = 0.3f
            }
        }

        val i = glowList.iterator()
        while (i.hasNext())
        {
            val e = i.next()
            e.update()
            if (e.isDone)
            {
                i.remove()
            }
        }
    }

    private fun renderGlow(sb: SpriteBatch)
    {
        if (!Settings.hideCards)
        {
            ReflectionHacks.privateMethod(AbstractCard::class.java, "renderMainBorder", SpriteBatch::class.java)
                .invoke<Any>(
                    this.poolCover, sb
                )

            //            this.poolCover.renderMainBorder(sb);
            for (safeCardGlowBorder in this.glowList)
            {
                (safeCardGlowBorder as AbstractGameEffect).render(sb)
            }

            sb.setBlendFunction(770, 771)
        }
    }

    override fun render(sb: SpriteBatch)
    {
        if (poolCover.hb.hovered)
        {
            sb.color = Color.WHITE
        }
        else
        {
            sb.color = Color.LIGHT_GRAY
        }
        updateGlow()
        renderGlow(sb)
        poolCover.render(sb)
        poolCover.hb.render(sb)

        FontHelper.cardDescFont_N.data.setScale(1.0f)
        FontHelper.cardDescFont_L.data.setScale(1.0f)
        FontHelper.cardTitleFont.data.setScale(1.0f)
    }

    override fun update()
    {
        poolCover.target_x = this.target_x
        poolCover.target_y = this.target_y

        poolCover.update()
        poolCover.updateHoverLogic()
        if (poolCover.hb.hovered)
        {
            poolCover.targetDrawScale = COVER_DRAW_SCALE * COVER_DRAW_HOVER_SCALE_RATE
        }
        else
        {
            poolCover.targetDrawScale = COVER_DRAW_SCALE
        }

        //        this.poolCover.drawScale = COVER_DRAW_SCALE;
//        this.poolCover.targetDrawScale = COVER_DRAW_SCALE;
        if (poolCover.hb.hovered && InputHelper.justClickedLeft)
        {
            CardCrawlGame.sound.play("UI_CLICK_1")
            clickThisPool()
        }
        if (isSelect)
        {
            this.isGlowing = true
        }
        else
        {
            this.isGlowing = false
            for (safeCardGlowBorder in this.glowList)
            {
                safeCardGlowBorder.duration /= 5.0f
            }
        }
        FontHelper.cardDescFont_N.data.setScale(1.0f)
        FontHelper.cardDescFont_L.data.setScale(1.0f)
        FontHelper.cardTitleFont.data.setScale(1.0f)
    }

    override fun renderLayer(): Int
    {
        return 0
    }

    override fun updateOrder(): Int
    {
        return 0
    }

    interface IsCardPoolCover
    {
        val self: AbstractCard
            get() = this as AbstractCard

        fun getAddedCard(): Predicate<AbstractCard>?

        fun getBanedCard(): Predicate<AbstractCard>?
    }

    class SafeCardGlowBorder @JvmOverloads constructor(
        private val card: AbstractCard,
        gColor: Color = Color.valueOf("30c8dcff")
    ) : AbstractGameEffect()
    {
        private var img: AtlasRegion? = null
        var scale = 0f

        init
        {
            when (card.type)
            {
                CardType.POWER  -> this.img = ImageMaster.CARD_POWER_BG_SILHOUETTE
                CardType.ATTACK -> this.img = ImageMaster.CARD_ATTACK_BG_SILHOUETTE
                else            -> this.img = ImageMaster.CARD_SKILL_BG_SILHOUETTE
            }
            this.duration = 1.2f
            //            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.color = gColor.cpy()

            //            } else {
//                this.color = Color.GREEN.cpy();
//            }
        }

        override fun update()
        {
            this.scale = (1.0f + Interpolation.pow2Out.apply(
                0.03f,
                0.11f,
                1.0f - this.duration
            )) * card.drawScale * Settings.scale
            color.a = this.duration / 2.0f
            this.duration -= Gdx.graphics.deltaTime
            if (this.duration < 0.0f)
            {
                this.isDone = true
                this.duration = 0.0f
            }
        }

        override fun render(sb: SpriteBatch)
        {
            sb.color = color
            sb.draw(
                this.img,
                card.current_x + img!!.offsetX - img!!.originalWidth.toFloat() / 2.0f,
                card.current_y + img!!.offsetY - img!!.originalHeight.toFloat() / 2.0f,
                img!!.originalWidth.toFloat() / 2.0f - img!!.offsetX,
                img!!.originalHeight.toFloat() / 2.0f - img!!.offsetY,
                img!!.packedWidth.toFloat(),
                img!!.packedHeight.toFloat(), this.scale, this.scale,
                card.angle
            )
        }

        override fun dispose()
        {
        }
    }

    companion object
    {
        const val COVER_DRAW_SCALE: Float = 0.5f
        const val COVER_DRAW_HOVER_SCALE_RATE: Float = 1.25f
        val HB_W_CARD: Float = 300.0f * Settings.scale
    }
}
