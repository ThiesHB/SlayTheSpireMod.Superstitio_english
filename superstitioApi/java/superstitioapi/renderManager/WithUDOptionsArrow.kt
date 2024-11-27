package superstitioapi.renderManager

import basemod.IUIElement
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.input.InputHelper

interface WithUDOptionsArrow
{
    /**
     * 向左箭头点击时调用
     */
    fun pageDown()
    {
        if (this.selectIndex > 0)
        {
            this.selectIndex = this.selectIndex - 1
        }
        refreshAfterPageChange()
    }

    /**
     * 向右箭头点击时调用
     */
    fun pageUp()
    {
        if (this.selectIndex < this.maxSelectIndex)
        {
            this.selectIndex = this.selectIndex + 1
        }
        refreshAfterPageChange()
    }

    fun refreshAfterPageChange()

    fun render(sb: SpriteBatch?)

    fun update()

    var selectIndex: Int

    val maxSelectIndex: Int

    fun renderArrow(sb: SpriteBatch)
    {
        if (this.selectIndex != 0)
        {
            upArrowButton.render(sb)
        }
        if (this.selectIndex < this.maxSelectIndex)
        {
            downArrowButton.render(sb)
        }
    }

    fun updateArrow()
    {
        if (this.selectIndex != 0)
        {
            upArrowButton.update()
        }
        if (this.selectIndex < this.maxSelectIndex)
        {
            downArrowButton.update()
        }
    }

    val upArrowButton: UpArrowButton

    val downArrowButton: DownArrowButton

    /**
     * 点击时调用pageDown()
     */
    class UpArrowButton(private var x: Float, private var y: Float, private val owner: WithUDOptionsArrow) :
        IUIElement
    {
        private val arrow: Texture = ImageMaster.CF_LEFT_ARROW
        private val width = Settings.scale * arrow.width / 2.0f
        private val height = Settings.scale * arrow.height / 2.0f
        private val hitbox = Hitbox(x, y, this.width, this.height)

        fun move(newX: Float, newY: Float)
        {
            this.x = newX - this.width / 2.0f
            this.y = newY - this.height / 2.0f
            hitbox.move(newX, newY)
        }

        override fun render(sb: SpriteBatch)
        {
            if (hitbox.hovered)
            {
                sb.color = Color.WHITE
            }
            else
            {
                sb.color = Color.LIGHT_GRAY
            }
            val halfW = arrow.width / 2.0f
            val halfH = arrow.height / 2.0f
            sb.draw(
                this.arrow, this.x + 10.0f * Settings.xScale - halfW + halfW * 0.5f * Settings.scale,
                this.y + 10.0f * Settings.yScale - halfH + halfH * 0.5f * Settings.scale, halfW, halfH,
                arrow.width.toFloat(),
                arrow.height.toFloat(), 0.75f * Settings.scale, 0.75f * Settings.scale, -90.0f, 0, 0,
                arrow.width,
                arrow.height, false, false
            )
            hitbox.render(sb)
        }

        override fun update()
        {
            hitbox.update()
            if (hitbox.hovered && InputHelper.justClickedLeft)
            {
                CardCrawlGame.sound.play("UI_CLICK_1")
                owner.pageDown()
            }
        }

        override fun renderLayer(): Int
        {
            return 0
        }

        override fun updateOrder(): Int
        {
            return 0
        }
    }

    /**
     * 点击时调用pageUp()
     */
    class DownArrowButton(private var x: Float, private var y: Float, private val owner: WithUDOptionsArrow) :
        IUIElement
    {
        private val arrow: Texture = ImageMaster.CF_RIGHT_ARROW
        private val width = Settings.scale * arrow.width / 2.0f
        private val height = Settings.scale * arrow.height / 2.0f
        private val hitbox = Hitbox(x, y, this.width, this.height)

        fun move(newX: Float, newY: Float)
        {
            this.x = newX - this.width / 2.0f
            this.y = newY - this.height / 2.0f
            hitbox.move(newX, newY)
        }

        override fun render(sb: SpriteBatch)
        {
            if (hitbox.hovered)
            {
                sb.color = Color.WHITE
            }
            else
            {
                sb.color = Color.LIGHT_GRAY
            }
            val halfW = arrow.width / 2.0f
            val halfH = arrow.height / 2.0f
            sb.draw(
                this.arrow, this.x + 10.0f * Settings.xScale - halfW + halfW * 0.5f * Settings.scale,
                this.y + 10.0f * Settings.yScale - halfH + halfH * 0.5f * Settings.scale, halfW, halfH,
                arrow.width.toFloat(),
                arrow.height.toFloat(), 0.75f * Settings.scale, 0.75f * Settings.scale, -90.0f, 0, 0,
                arrow.width,
                arrow.height, false, false
            )
            hitbox.render(sb)
        }

        override fun update()
        {
            hitbox.update()
            if (hitbox.hovered && InputHelper.justClickedLeft)
            {
                CardCrawlGame.sound.play("UI_CLICK_1")
                owner.pageUp()
            }
        }

        override fun renderLayer(): Int
        {
            return 0
        }

        override fun updateOrder(): Int
        {
            return 0
        }
    }
}
