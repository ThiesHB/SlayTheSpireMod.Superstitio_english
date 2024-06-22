package superstitioapi.powers.barIndepend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;

import java.util.function.Supplier;

/**
 * 文本有hitbox，还有一个额外文本
 */
public class BarRenderOnThing_Ring_Text extends BarRenderOnThing_Ring {
    protected Hitbox barTextHitBox;
    protected Hitbox centerHitBox;

    public BarRenderOnThing_Ring_Text(Supplier<Hitbox> hitbox, HasBarRenderOnCreature power) {
        super(hitbox, power);
//        centerHitBox = new Hitbox(this.barSize / 2, this.barSize / 2);
//        updateHitBoxPlace(this.centerHitBox);
        barTextHitBox = new Hitbox(this.barSize, RenderOnThing.TEXT_OFFSET_Y * 2.5f);
        moveBarTextHitBox();
    }

    private void moveBarTextHitBox() {
        barTextHitBox.move(
                hitboxBondTo.get().cX,
                HeightOffset + hitboxBondTo.get().cY - this.barSize / 2 + TEXT_OFFSET_Y * 3 + hitboxBondTo.get().height / 2);
    }

    @Override
    public void update() {
        moveBarTextHitBox();
        barTextHitBox.update();
        update_showTips(this.barTextHitBox);
        super.update();
    }

    @Override
    protected void updateHbHoverFade() {
        if (this.barTextHitBox.hovered) {
            this.healthHideTimer -= Gdx.graphics.getDeltaTime() * HIDE_SPEED;
            if (this.healthHideTimer < HEALTH_HIDE_TIMER_MIN) {
                this.healthHideTimer = HEALTH_HIDE_TIMER_MIN;
            }
        } else {
            this.healthHideTimer += Gdx.graphics.getDeltaTime() * HIDE_SPEED;
            if (this.healthHideTimer > 1.0F) {
                this.healthHideTimer = 1.0F;
            }
        }
    }

    @Override
    protected void renderDebug(SpriteBatch sb) {
        super.renderDebug(sb);
        barTextHitBox.render(sb);
    }

    @Override
    public void render(SpriteBatch sb) {
        renderBar(sb);
        renderBarTextWithColorAlphaChange(sb, getXDrawStart(), getYDrawStart());
        if (Settings.isDebug || Settings.isInfo) {
            renderDebug(sb);
        }
    }

    @Override
    protected void renderBarText(SpriteBatch sb, float x, float y) {
        FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, makeBarText(),
                this.barTextHitBox.cX, this.barTextHitBox.cY + TEXT_OFFSET_Y,
                this.barTextColor);
    }
}
