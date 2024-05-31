package superstitioapi.powers.barIndepend;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import superstitioapi.utils.ImgUtility;

import java.util.function.Supplier;

public class BarRenderOnThing_Vertical extends BarRenderOnThing {

    public static final int ROTATION = 90;
    public static final float BAR_WIDTH = BAR_DIAMETER * 2.1f;
    public static final float BAR_BG_WIDTH = BAR_WIDTH * 0.87f;

    public BarRenderOnThing_Vertical(Supplier<Hitbox> hitbox, HasBarRenderOnCreature power) {
        super(hitbox, power);
        this.barLength = this.hitboxBondTo.get().height;
        this.hitbox = new Hitbox(BAR_WIDTH * 1.5f, hitboxBondTo.get().height + BAR_WIDTH - BAR_OFFSET_Y * 2);
        updateHitBoxPlace(this.hitbox);
    }

    @Override
    public void updateHitBoxPlace(Hitbox hitbox) {
        hitbox.move(
                hitboxBondTo.get().cX - HeightOffset - hitboxBondTo.get().width / 2,
                hitboxBondTo.get().cY + BAR_OFFSET_Y * 2);
    }

    @Override
    protected float getYDrawStart() {
        return this.hitbox.cY - this.barLength / 2.0f;
    }

    @Override
    protected float getXDrawStart() {
        return this.hitbox.cX + BAR_WIDTH / 2.0f;//绘制后会旋转，所以实际上的绘制位置更靠左
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void drawBarShadow(SpriteBatch sb, float x, float y, float startLength, float length) {
        ImgUtility.draw(sb, ImageMaster.HB_SHADOW_L, x, y + startLength - BAR_DIAMETER, BAR_DIAMETER, BAR_BG_WIDTH, ROTATION);
        ImgUtility.draw(sb, ImageMaster.HB_SHADOW_B, x, y + startLength, length, BAR_BG_WIDTH, ROTATION);
        ImgUtility.draw(sb, ImageMaster.HB_SHADOW_R, x, y + startLength + length, BAR_DIAMETER, BAR_BG_WIDTH, ROTATION);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void drawBar(SpriteBatch sb, float x, float y, float startLength, float length) {
        ImgUtility.draw(sb, ImageMaster.HEALTH_BAR_L, x, y + startLength - BAR_DIAMETER, BAR_DIAMETER, BAR_WIDTH, ROTATION);
        ImgUtility.draw(sb, ImageMaster.HEALTH_BAR_B, x, y + startLength, length, BAR_WIDTH, ROTATION);
        ImgUtility.draw(sb, ImageMaster.HEALTH_BAR_R, x, y + startLength + length, BAR_DIAMETER, BAR_WIDTH, ROTATION);
    }

    @Override
    protected BarAmountChunk makeNewAmountChunk(BarRenderUpdateMessage message) {
        return new BarAmountChunk(
                getXDrawStart(),
                getYDrawStart(),
                chunkLength(getTotalAmount()),
                chunkLength(message.newAmount), getNextOrder(), this);
    }

    @Override
    protected void renderBarText(SpriteBatch sb, float x, float y) {
        FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, makeBarText(),
                this.hitbox.cX,
                y + BAR_DIAMETER + chunkLength(this.getTotalAmount()) + TEXT_OFFSET_Y,
                this.barTextColor,
                0.8f
        );
    }

    @Override
    protected void chunkHitBoxReSize(BarAmountChunk amountChunk) {
        amountChunk.hitbox.width = BAR_WIDTH;
        amountChunk.hitbox.height = amountChunk.length + BAR_DIAMETER;
        amountChunk.hitbox.y = amountChunk.drawY - BAR_DIAMETER / 2;
        amountChunk.hitbox.moveX(this.hitbox.cX);
    }
}
