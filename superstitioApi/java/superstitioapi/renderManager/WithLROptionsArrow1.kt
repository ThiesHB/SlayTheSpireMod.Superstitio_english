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

interface WithLROptionsArrow {
    /**
     * 向左箭头点击时调用
     */
    fun pageDown() {
        if (isLoop) {
            if (this.selectIndex > 0) {
                this.selectIndex = this.selectIndex - 1
            } else {
                this.selectIndex = this.maxSelectIndex
            }
        } else {
            if (this.selectIndex > 0) {
                this.selectIndex = this.selectIndex - 1
            }
        }
        refreshAfterPageChange()
    }

    /**
     * 向右箭头点击时调用
     */
    fun pageUp() {
        if (isLoop) {
            if (this.selectIndex < this.maxSelectIndex) {
                this.selectIndex = this.selectIndex + 1
            } else {
                this.selectIndex = 0
            }
        } else {
            if (this.selectIndex < this.maxSelectIndex) {
                this.selectIndex = this.selectIndex + 1
            }
        }
        refreshAfterPageChange()
    }

    val isLoop: Boolean
        get() = false

    fun refreshAfterPageChange()

    fun render(sb: SpriteBatch)

    fun update()

    var selectIndex: Int

    val maxSelectIndex: Int

    fun renderArrow(sb: SpriteBatch) {
        if (this.selectIndex != 0 || this.isLoop) {
            leftArrowButton.render(sb)
        }
        if (this.selectIndex < this.maxSelectIndex || this.isLoop) {
            rightArrowButton.render(sb)
        }
    }

    fun updateArrow() {
        if (this.selectIndex != 0 || this.isLoop) {
            leftArrowButton.update()
        }
        if (this.selectIndex < this.maxSelectIndex || this.isLoop) {
            rightArrowButton.update()
        }
    }

    val leftArrowButton: LeftArrowButton

    val rightArrowButton: RightArrowButton

    /**
     * 点击时调用pageDown()
     */
    class LeftArrowButton(private var x: Float, private var y: Float, private val owner: WithLROptionsArrow) :
        IUIElement {
        val width: Float
        val height: Float
        private val arrow: Texture = ImageMaster.CF_LEFT_ARROW
        private val hitbox: Hitbox

        init {
            this.width = Settings.scale * arrow.width
            this.height = Settings.scale * arrow.height
            this.hitbox = Hitbox(x, y, this.width, this.height)
        }

        fun move(newX: Float, newY: Float) {
            this.x = newX - this.width / 2.0f
            this.y = newY - this.height / 2.0f
            hitbox.move(newX, newY)
        }

        override fun render(sb: SpriteBatch) {
            if (hitbox.hovered) {
                sb.color = Color.WHITE
            } else {
                sb.color = Color.LIGHT_GRAY
            }
            sb.draw(this.arrow, this.x, this.y, this.width, this.height)
            hitbox.render(sb)
        }

        override fun update() {
            hitbox.update()
            if (hitbox.hovered && InputHelper.justClickedLeft) {
                CardCrawlGame.sound.play("UI_CLICK_1")
                owner.pageDown()
            }
        }

        override fun renderLayer(): Int {
            return 0
        }

        override fun updateOrder(): Int {
            return 0
        }
    }

    /**
     * 点击时调用pageUp()
     */
    class RightArrowButton(private var x: Float, private var y: Float, private val owner: WithLROptionsArrow) :
        IUIElement {
        val width: Float
        val height: Float
        private val arrow: Texture = ImageMaster.CF_RIGHT_ARROW
        private val hitbox: Hitbox

        init {
            this.width = Settings.scale * arrow.width
            this.height = Settings.scale * arrow.height
            this.hitbox = Hitbox(x, y, this.width, this.height)
        }

        fun move(newX: Float, newY: Float) {
            this.x = newX - this.width / 2.0f
            this.y = newY - this.height / 2.0f
            hitbox.move(newX, newY)
        }

        override fun render(sb: SpriteBatch) {
            if (hitbox.hovered) {
                sb.color = Color.WHITE
            } else {
                sb.color = Color.LIGHT_GRAY
            }
            sb.draw(this.arrow, this.x, this.y, width, height)
            hitbox.render(sb)
        }

        override fun update() {
            hitbox.update()
            if (hitbox.hovered && InputHelper.justClickedLeft) {
                CardCrawlGame.sound.play("UI_CLICK_1")
                owner.pageUp()
            }
        }

        override fun renderLayer(): Int {
            return 0
        }

        override fun updateOrder(): Int {
            return 0
        }
    }
}
