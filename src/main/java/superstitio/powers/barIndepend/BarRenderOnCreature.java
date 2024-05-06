package superstitio.powers.barIndepend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class BarRenderOnCreature {
    protected static final float BAR_OFFSET_Y = -28.0f * Settings.scale;
    protected static final float TEXT_OFFSET_Y = 11.0f * Settings.scale;
    //绘制相关
    private static final float BAR_DIAMETER = 20.0f * Settings.scale;
    private static final float BG_OFFSET_X = 31.0f * Settings.scale;
    protected final Map<String, Integer> UuidAmountMap_UuidOfOwners = new HashMap<>();
    protected final float renderWidth;
    private final ArrayList<AmountBar> amountBars = new ArrayList<>();
    public Color barBgColor;
    public Color barShadowColor;
    public Color barTextColor;
    public Hitbox hitbox;
    public String uuid_self;
    private float fontScale;
    private float healthHideTimer = 0.0f;

    public BarRenderOnCreature(final AbstractCreature renderCreature, String uuid) {
        this.uuid_self = uuid;
        this.renderWidth = renderCreature.hb.width;
        this.hitbox = new Hitbox(renderCreature.hb.width + BAR_DIAMETER * 3f, BAR_DIAMETER * 1.5f);
        this.hitbox.move(renderCreature.hb.cX, owner.Height() + renderCreature.hb.cY + renderCreature.hb.height / 2 + BG_OFFSET_X * 2);

        this.barBgColor = owner.setupBarBgColor();
        this.barShadowColor = owner.setupBarShadowColor();
        this.barTextColor = owner.setupBarTextColor();
    }

    protected int getMaxBarAmount() {
        return activeOwner().mapToInt(HasBarRenderOnCreature::maxBarAmount).max().orElse(1);
    }

    protected abstract Predicate<HasBarRenderOnCreature> isActive();

    protected int getTotalAmount() {
        return activeOwner().mapToInt(HasBarRenderOnCreature::getAmountForDraw).sum();
    }

    protected float barWidth(int amount) {
        float v = this.renderWidth * (amount % getMaxBarAmount()) / getMaxBarAmount();
        if (amount % getMaxBarAmount() == 0 && amount != 0)
            v = this.renderWidth;
        return v;
    }

    private Stream<HasBarRenderOnCreature> activeOwner() {
        return this.owners.stream().filter(isActive());
    }

    public void render(SpriteBatch sb) {
        if (!activeOwner().findAny().isPresent()) return;
        renderBar(sb);
    }

    public void update() {
        this.owners.removeIf(owner -> !isActive().test(owner));
        if (!activeOwner().findAny().isPresent()) return;
        update_showTips(this.hitbox);
    }

    Map<String, String> AllTips() {
        Map<String, String> NameDescMap = new HashMap<>();
        this.owners.forEach(owner -> NameDescMap.put(owner.getName(), owner.getDescription()));
        return NameDescMap;
    }

    protected void update_showTips(Hitbox hitbox) {
        hitbox.update();
        if (hitbox.hovered) {
            AllTips().forEach((key, value) -> TipHelper.renderGenericTip(hitbox.cX + 96.0F * Settings.scale, hitbox.cY + 64.0F * Settings.scale,
                    key, value));
        }

        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
    }

    protected void renderBar(SpriteBatch sb) {
        float OwnerX = getXDrawStart();
        float OwnerY = getYDrawStart();
        this.renderAmountBarBackGround(sb, OwnerX, OwnerY);
        this.amountBars.forEach(amountBar -> amountBar.render(sb));
        this.renderBarText(sb, OwnerY);
    }

    private float getYDrawStart() {
        return this.hitbox.cY + this.hitbox.height / 2.0f;
    }

    private float getXDrawStart() {
        return this.hitbox.cX - this.renderWidth / 2.0f;
    }

    private void renderAmountBarBackGround(final SpriteBatch sb, final float x, final float y) {
        sb.setColor(this.barShadowColor);
        sb.draw(ImageMaster.HB_SHADOW_L, x - BAR_DIAMETER, y - BG_OFFSET_X + 3.0f * Settings.scale, BAR_DIAMETER, BAR_DIAMETER);
        sb.draw(ImageMaster.HB_SHADOW_B, x, y - BG_OFFSET_X + 3.0f * Settings.scale, this.renderWidth, BAR_DIAMETER);
        sb.draw(ImageMaster.HB_SHADOW_R, x + this.renderWidth, y - BG_OFFSET_X + 3.0f * Settings.scale, BAR_DIAMETER, BAR_DIAMETER);
        sb.setColor(this.barBgColor);
        sb.draw(ImageMaster.HEALTH_BAR_L, x - BAR_DIAMETER, y + BAR_OFFSET_Y, BAR_DIAMETER, BAR_DIAMETER);
        sb.draw(ImageMaster.HEALTH_BAR_B, x, y + BAR_OFFSET_Y, this.renderWidth, BAR_DIAMETER);
        sb.draw(ImageMaster.HEALTH_BAR_R, x + this.renderWidth, y + BAR_OFFSET_Y, BAR_DIAMETER, BAR_DIAMETER);
    }

    private void renderBarText(final SpriteBatch sb, final float y) {
        if (!activeOwner().findAny().isPresent()) return;
        final float tmp = this.barTextColor.a;
        this.barTextColor.a *= this.healthHideTimer;
        FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont,
                activeOwner().findAny().get().makeBarText().apply(new Object[]{this.getTotalAmount(), this.getMaxBarAmount()}), this.hitbox.cX,
                y + BAR_OFFSET_Y + TEXT_OFFSET_Y, this.barTextColor);
        this.barTextColor.a = tmp;
    }

    protected void updateHealthBar() {
        this.updateHbHoverFade();
        this.updateBlockAnimations();
        this.updateHbPopInAnimation();
        this.updateHbDamageAnimation();
        this.updateHbAlpha();
    }

    private void updateHbHoverFade() {
        if (this.hitbox.hovered) {
            this.healthHideTimer -= Gdx.graphics.getDeltaTime() * 4.0F;
            if (this.healthHideTimer < 0.2F) {
                this.healthHideTimer = 0.2F;
            }
        } else {
            this.healthHideTimer += Gdx.graphics.getDeltaTime() * 4.0F;
            if (this.healthHideTimer > 1.0F) {
                this.healthHideTimer = 1.0F;
            }
        }

    }

    //    private void updateHbPopInAnimation() {
//        if (this.hbShowTimer > 0.0F) {
//            this.hbShowTimer -= Gdx.graphics.getDeltaTime();
//            if (this.hbShowTimer < 0.0F) {
//                this.hbShowTimer = 0.0F;
//            }
//
//            this.hbAlpha = Interpolation.fade.apply(0.0F, 1.0F, 1.0F - this.hbShowTimer / 0.7F);
//            this.hbYOffset = Interpolation.exp10Out.apply(HB_Y_OFFSET_DIST * 5.0F, 0.0F, 1.0F - this.hbShowTimer / 0.7F);
//        }
//
//    }
//
    private void updateHbDamageAnimation() {
        if (this.healthBarAnimTimer > 0.0F) {
            this.healthBarAnimTimer -= Gdx.graphics.getDeltaTime();
        }

        if (this.healthBarWidth != this.targetHealthBarWidth && this.healthBarAnimTimer <= 0.0F && this.targetHealthBarWidth < this.healthBarWidth) {
            this.healthBarWidth = MathHelper.uiLerpSnap(this.healthBarWidth, this.targetHealthBarWidth);
        }
    }

    /**
     * 在初始化后，如果想要修改，请使用这个函数
     *
     * @param message
     */
    public void applyMessage(BarRenderUpdateMessage message) {
        if (!UuidAmountMap_UuidOfOwners.containsKey(message.uuid)) {
            applyNewAmountBar(message);
            return;
        }
        amountBars.stream().filter(bar->bar.)
    }


    public int getTotalLength() {
        return UuidAmountMap_UuidOfOwners.values().stream().mapToInt(value -> value).sum();
    }

    private void applyNewAmountBar(BarRenderUpdateMessage message) {
        this.amountBars.add(new AmountBar(
                getXDrawStart() + barWidth(getTotalAmount()),
                getYDrawStart(),
                barWidth(message.newAmount),
                message.barColor));
    }

//    private void renderHealthText(SpriteBatch sb, float y) {
//        if (this.targetHealthBarWidth != 0.0F) {
//            float tmp = this.hbTextColor.a;
//            Color var10000 = this.hbTextColor;
//            var10000.a *= this.healthHideTimer;
//            FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, this.currentHealth + "/" + this.maxHealth, this.hb.cX,
//                    y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0F * Settings.scale, this.hbTextColor);
//            this.hbTextColor.a = tmp;
//        } else {
//            FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, TEXT[0], this.hb.cX,
//                    y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y - 1.0F * Settings.scale, this.hbTextColor);
//        }
//
//    }

//    private void updateHbAlpha() {
//        if (this instanceof AbstractMonster && ((AbstractMonster) this).isEscaping) {
//            this.hbAlpha = MathHelper.fadeLerpSnap(this.hbAlpha, 0.0F);
//            this.targetHealthBarWidth = 0.0F;
//            this.hbBgColor.a = this.hbAlpha * 0.75F;
//            this.hbShadowColor.a = this.hbAlpha * 0.5F;
//            this.hbTextColor.a = this.hbAlpha;
//            this.orangeHbBarColor.a = this.hbAlpha;
//            this.redHbBarColor.a = this.hbAlpha;
//            this.greenHbBarColor.a = this.hbAlpha;
//            this.blueHbBarColor.a = this.hbAlpha;
//            this.blockOutlineColor.a = this.hbAlpha;
//        } else if (this.targetHealthBarWidth == 0.0F && this.healthBarAnimTimer <= 0.0F) {
//            this.hbShadowColor.a = MathHelper.fadeLerpSnap(this.hbShadowColor.a, 0.0F);
//            this.hbBgColor.a = MathHelper.fadeLerpSnap(this.hbBgColor.a, 0.0F);
//            this.hbTextColor.a = MathHelper.fadeLerpSnap(this.hbTextColor.a, 0.0F);
//            this.blockOutlineColor.a = MathHelper.fadeLerpSnap(this.blockOutlineColor.a, 0.0F);
//        } else {
//            this.hbBgColor.a = this.hbAlpha * 0.5F;
//            this.hbShadowColor.a = this.hbAlpha * 0.2F;
//            this.hbTextColor.a = this.hbAlpha;
//            this.orangeHbBarColor.a = this.hbAlpha;
//            this.redHbBarColor.a = this.hbAlpha;
//            this.greenHbBarColor.a = this.hbAlpha;
//            this.blueHbBarColor.a = this.hbAlpha;
//            this.blockOutlineColor.a = this.hbAlpha;
//        }
//
//    }

//    protected void gainBlockAnimation() {
//        this.blockAnimTimer = 0.7F;
//        this.blockTextColor.a = 0.0F;
//        this.blockColor.a = 0.0F;
//    }

//    private void updateBlockAnimations() {
//        if (this.currentBlock > 0) {
//            if (this.blockAnimTimer > 0.0F) {
//                this.blockAnimTimer -= Gdx.graphics.getDeltaTime();
//                if (this.blockAnimTimer < 0.0F) {
//                    this.blockAnimTimer = 0.0F;
//                }
//
//                this.blockOffset = Interpolation.swingOut.apply(BLOCK_OFFSET_DIST * 3.0F, 0.0F, 1.0F - this.blockAnimTimer / 0.7F);
//                this.blockScale = Interpolation.pow3In.apply(3.0F, 1.0F, 1.0F - this.blockAnimTimer / 0.7F);
//                this.blockColor.a = Interpolation.pow2Out.apply(0.0F, 1.0F, 1.0F - this.blockAnimTimer / 0.7F);
//                this.blockTextColor.a = Interpolation.pow5In.apply(0.0F, 1.0F, 1.0F - this.blockAnimTimer / 0.7F);
//            } else if (this.blockScale != 1.0F) {
//                this.blockScale = MathHelper.scaleLerpSnap(this.blockScale, 1.0F);
//            }
//
//            if (this.blockTextColor.r != 1.0F) {
//                this.blockTextColor.r = MathHelper.slowColorLerpSnap(this.blockTextColor.r, 1.0F);
//            }
//
//            if (this.blockTextColor.g != 1.0F) {
//                this.blockTextColor.g = MathHelper.slowColorLerpSnap(this.blockTextColor.g, 1.0F);
//            }
//
//            if (this.blockTextColor.b != 1.0F) {
//                this.blockTextColor.b = MathHelper.slowColorLerpSnap(this.blockTextColor.b, 1.0F);
//            }
//        }
//
//    }

    public static class AmountBar {
        public Color barOrginColor;
        private float drawX;
        private float drawY;
        private float drawXTarget;
        private float drawYTarget;
        private float width;
        private float widthTarget;
        private float animTimer;

        public AmountBar(float drawX, float drawY, float width, Color barOrginColor) {
            this.drawX = drawX;
            this.drawY = drawY;
            this.width = width;
            this.barOrginColor = barOrginColor.cpy();
        }

        public void render(final SpriteBatch sb) {
            renderAmountBar(sb);
        }

        public void update() {
            updateHbDamageAnimation();
        }

        private void renderAmountBar(final SpriteBatch sb) {
            if (this.width == 0) return;
            sb.setColor(this.barOrginColor);
            sb.draw(ImageMaster.HEALTH_BAR_L, drawX - BAR_DIAMETER, drawY + BAR_OFFSET_Y, BAR_DIAMETER, BAR_DIAMETER);
            sb.draw(ImageMaster.HEALTH_BAR_B, drawX, drawY + BAR_OFFSET_Y, width, BAR_DIAMETER);
            sb.draw(ImageMaster.HEALTH_BAR_R, drawX + width, drawY + BAR_OFFSET_Y, BAR_DIAMETER, BAR_DIAMETER);
        }

        public void move(int drawXTarget, int drawYTarget) {
            this.drawXTarget = drawXTarget;
            this.drawYTarget = drawYTarget;
        }

        public void changeAmount(int widthTarget) {
            this.widthTarget = widthTarget;
        }

        private void updateHbDamageAnimation() {
            if (this.animTimer > 0.0F)
                this.animTimer -= Gdx.graphics.getDeltaTime();
            if (this.animTimer > 0.0F)
                return;
            if (this.widthTarget < this.width)
                this.width = MathHelper.uiLerpSnap(this.width, this.widthTarget);
            if (this.drawXTarget < this.drawX)
                this.drawX = MathHelper.uiLerpSnap(this.drawX, this.drawXTarget);
            if (this.drawYTarget < this.drawY)
                this.drawY = MathHelper.uiLerpSnap(this.drawY, this.drawYTarget);
        }
    }

}