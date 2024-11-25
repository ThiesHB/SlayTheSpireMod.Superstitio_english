package superstitioapi.powers.barIndepend

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import java.util.function.Supplier

/**
 * 文本有hitbox，还有一个额外文本
 */
class BarRenderOnThing_Ring_Text(hitbox: Supplier<Hitbox>, power: HasBarRenderOnCreature) :
    BarRenderOnThing_Ring(hitbox, power) {
    //        centerHitBox = new Hitbox(this.barSize / 2, this.barSize / 2);
//        updateHitBoxPlace(this.centerHitBox);
    protected var barTextHitBox: Hitbox = Hitbox(this.barSize, RenderOnThing.TEXT_OFFSET_Y * 2.5f)
    protected var centerHitBox: Hitbox? = null

    init {
        moveBarTextHitBox()
    }

    private fun moveBarTextHitBox() {
        barTextHitBox.move(
            hitboxBondTo.get().cX,
            HeightOffset + hitboxBondTo.get().cY - this.barSize / 2 + TEXT_OFFSET_Y * 3 + hitboxBondTo.get().height / 2
        )
    }

    override fun update() {
        moveBarTextHitBox()
        barTextHitBox.update()
        update_showTips(this.barTextHitBox)
        super.update()
    }

    override fun render(sb: SpriteBatch) {
        renderBar(sb)
        renderBarTextWithColorAlphaChange(sb, xDrawStart, yDrawStart)
        if (Settings.isDebug || Settings.isInfo) {
            renderDebug(sb)
        }
    }

    override fun updateHbHoverFade() {
        if (barTextHitBox.hovered) {
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

    override fun renderDebug(sb: SpriteBatch?) {
        super.renderDebug(sb)
        barTextHitBox.render(sb)
    }

    override fun renderBarText(sb: SpriteBatch?, x: Float, y: Float) {
        FontHelper.renderFontCentered(
            sb, FontHelper.healthInfoFont, makeBarText(),
            barTextHitBox.cX, barTextHitBox.cY + TEXT_OFFSET_Y,
            this.barTextColor
        )
    }
}
