package superstitioapi.renderManager.characterSelectScreenRender

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.helpers.input.InputHelper
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.relics.Circlet
import superstitioapi.renderManager.WithLROptionsArrow
import superstitioapi.renderManager.WithLROptionsArrow.LeftArrowButton
import superstitioapi.renderManager.WithLROptionsArrow.RightArrowButton
import superstitioapi.utils.TipsUtility
import java.util.function.Consumer
import kotlin.math.max

class RelicSelectionUI(
    private val cX: Float,
    private val cY: Float, //    private static UIStringsSet uiStrings = CardCrawlGame.languagePack.getUIString(DataUtility.makeID("SkinSystemUI"));
    private val relics: List<AbstractRelic>,
    private val optionTitle: String,
    private val optionTip: PowerTip
) : WithLROptionsArrow
{
    private val textWidth: Float
    override val leftArrowButton: LeftArrowButton
    override val rightArrowButton: RightArrowButton
    private val relicHitbox: Hitbox
    private val textHitbox: Hitbox
    override var selectIndex: Int
    private var refreshAfterSelect: Consumer<AbstractRelic>? = null

    constructor(cX: Float, cY: Float, relics: List<AbstractRelic>, optionTip: PowerTip) : this(
        cX,
        cY,
        relics,
        optionTip.header,
        optionTip
    )

    init
    {
        this.textWidth = max(
            FontHelper.getWidth(titleFont, optionTitle, 1.0f),
            RELIC_HITBOX_SIZE
        )

        this.relicHitbox = Hitbox(RELIC_HITBOX_SIZE, RELIC_HITBOX_SIZE)
        relicHitbox.move(this.cX, this.cY + RELIC_Y_OFFSET)

        this.leftArrowButton = LeftArrowButton(cX, cY, this)
        leftArrowButton.move(cX - this.textWidth / 2 - leftArrowButton.width / 2, cY)

        this.rightArrowButton = RightArrowButton((cX + textWidth).toFloat(), cY, this)
        rightArrowButton.move(cX + textWidth / 2 + rightArrowButton.width / 2, cY)

        this.textHitbox = Hitbox(
            textWidth, max(
                rightArrowButton.height,
                leftArrowButton.height
            )
        )
        textHitbox.move(this.cX, this.cY)

        this.selectIndex = 0
    }

    fun setRefreshAfterSelect(refreshAfterSelect: Consumer<AbstractRelic>?): RelicSelectionUI
    {
        this.refreshAfterSelect = refreshAfterSelect
        return this
    }

    val totalWidth: Float
        get() = textWidth + leftArrowButton.width + rightArrowButton.width

    val selectRelic: AbstractRelic
        get()
        {
            if (selectIndex >= 0 && selectIndex < relics.size) return relics[selectIndex]
            return Circlet()
        }

    fun setSelectRelic(selcetRelicId: String)
    {
        val abstractRelics = this.relics
        for (i in abstractRelics.indices)
        {
            val relic = abstractRelics[i]
            if (selcetRelicId == relic.relicId)
            {
                this.selectIndex = i
                break
            }
        }
    }

    override fun refreshAfterPageChange()
    {
        if (refreshAfterSelect == null) return
        refreshAfterSelect!!.accept(selectRelic)
    }

    override val isLoop: Boolean
        get() = true

    override fun render(sb: SpriteBatch)
    {
        val relic = selectRelic

        if (relicHitbox.hovered)
        {
            TipsUtility.renderTipsWithMouse(relic.tips)
        }
        if (textHitbox.hovered)
        {
            TipsUtility.renderTipsWithMouse(ArrayList(listOf(optionTip)))
        }

        FontHelper.renderFontCentered(sb, titleFont, optionTitle, this.cX, this.cY, TEXT_COLOR)
        sb.color = Color(0.0f, 0.0f, 0.0f, 0.25f)
        sb.draw(
            relic.outlineImg,
            this.cX - RelicSize,
            this.cY + RELIC_Y_OFFSET - RelicSize,
            RelicImgSize * Settings.xScale,
            RelicImgSize * Settings.yScale
        )
        sb.color = Color.WHITE
        sb.draw(
            relic.img,
            this.cX - RelicSize,
            this.cY + RELIC_Y_OFFSET - RelicSize,
            RelicImgSize * Settings.xScale,
            RelicImgSize * Settings.yScale
        )

        super.renderArrow(sb)
        relicHitbox.render(sb)
        textHitbox.render(sb)
    }

    override fun update()
    {
        if (relicHitbox.hovered || textHitbox.hovered)
        {
            if (InputHelper.justClickedLeft)
            {
                CardCrawlGame.sound.play("UI_CLICK_1")
                this.pageUp()
            }
            else if (InputHelper.justClickedRight)
            {
                CardCrawlGame.sound.play("UI_CLICK_1")
                this.pageDown()
            }
        }
        relicHitbox.update()
        textHitbox.update()
        super.updateArrow()
    }

    override val maxSelectIndex: Int
        get() = relics.size - 1

    companion object
    {
        val RELIC_Y_OFFSET: Float = -60.0f * Settings.scale
        val RELIC_HITBOX_SIZE: Float = 80.0f * Settings.scale
        val TEXT_COLOR: Color = Color.GOLDENROD.cpy()
        private const val RelicImgSize = 128.0f
        private const val RelicSize = 64.0f
        private val titleFont: BitmapFont = FontHelper.cardTitleFont
    }
}
