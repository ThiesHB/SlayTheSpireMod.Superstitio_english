package superstitioapi.powers.barIndepend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class BarRenderOnThing extends RenderOnThing {
    public static final float HIDE_SPEED = 4.0F;
    public static final float HEALTH_HIDE_TIMER_MIN = 0.2F;
    protected static final float BAR_OFFSET_Y = 28.0f * Settings.scale;
    protected static final float TEXT_OFFSET_Y = 11.0f * Settings.scale;
    //绘制相关
    protected static final float BAR_DIAMETER = 20.0f * Settings.scale;
    //    public static final float HITBOX_HEIGHT = ;
//    protected static final float BG_OFFSET_Y = -31.0f * Settings.scale;
    public Color barBgColor;
    public Color barShadowColor;
    public float barLength;
    private boolean isChunkHovered = false;

    public BarRenderOnThing(final Supplier<Hitbox> hitbox, HasBarRenderOnCreature power) {
        super(hitbox, power);
        this.hitbox = new Hitbox(hitboxBondTo.get().width + BAR_DIAMETER * 3f, BAR_DIAMETER * 1.5f);
        updateHitBoxPlace(this.hitbox);
        this.barLength = hitboxBondTo.get().width;

        this.barBgColor = new Color(0f, 0f, 0f, 0.3f);
        this.barShadowColor = new Color(0f, 0f, 0f, 0.3f);
    }

    protected void drawBarMaxEnd(SpriteBatch sb, float x, float y, float startLength, float length) {
        sb.draw(ImageMaster.HEALTH_BAR_R, x + startLength + length, y, BAR_DIAMETER, BAR_DIAMETER);
    }

    protected void drawBarMiddle(SpriteBatch sb, float x, float y, float startLength, float length) {
        sb.draw(ImageMaster.HEALTH_BAR_B, x + startLength, y, length, BAR_DIAMETER);
    }

    protected void drawBarMinEnd(SpriteBatch sb, float x, float y, float startLength, float length) {
        sb.draw(ImageMaster.HEALTH_BAR_L, x + startLength - BAR_DIAMETER, y, BAR_DIAMETER, BAR_DIAMETER);
    }

    protected float chunkLength(int amount) {
        float v = (this.barLength * (amount % getMaxBarAmount())) / (float) getMaxBarAmount();
        if (amount % getMaxBarAmount() == 0 && amount != 0) v = this.barLength;
        return v;
    }

    public void render(SpriteBatch sb) {
        renderBar(sb);
        renderBarTextWithColorAlphaChange(sb, getXDrawStart(), getYDrawStart());
        if (Settings.isDebug || Settings.isInfo) {
            renderDebug(sb);
        }
    }

    protected void renderDebug(SpriteBatch sb) {
        this.hitbox.render(sb);
        for (AmountChunk amountChunk : this.amountChunkWithUuid.values()) {
            if (amountChunk instanceof BarAmountChunk)
                ((BarAmountChunk) amountChunk).hitbox.render(sb);
        }
    }

    public void update() {
        updateHitBoxPlace(this.hitbox);
        this.hitbox.update();
        List<AmountChunk> chunkList = this.sortedChunkList;
        for (int i = chunkList.size() - 1; i >= 0; i--) {
            AmountChunk amountChunk = chunkList.get(i);
            amountChunk.update();
            if (amountChunk instanceof BarAmountChunk) {
                ((BarAmountChunk) amountChunk).teleport(getXDrawStart(), getYDrawStart());
                ((BarAmountChunk) amountChunk).updateHitBox();
            }
        }
        update_showTips(this.hitbox);
        updateHbHoverFade();
    }

    protected final void update_showTips(Hitbox hitbox) {
        if (hitbox.hovered && !isChunkHovered) {
            renderTip(AllTips());
        }
        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
    }

    protected void renderBar(SpriteBatch sb) {
        this.renderAmountBarBackGround(sb, getXDrawStart(), getYDrawStart());
        this.sortedChunkList.forEach(amountChunk -> amountChunk.render(sb));
    }

    protected float getYDrawStart() {
        return this.hitbox.cY - BAR_DIAMETER / 2.0f;
    }

    protected float getXDrawStart() {
        return this.hitbox.cX - this.barLength / 2.0f;
    }

    @Override
    public void tryApplyMessage(BarRenderUpdateMessage message) {
        super.tryApplyMessage(message);
        calculateAllPosition();
    }

    @Override
    public void removeChunk(HasBarRenderOnCreature hasBarRenderOnCreature) {
        super.removeChunk(hasBarRenderOnCreature);
        calculateAllPosition();
    }

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    protected void renderAmountBarBackGround(final SpriteBatch sb, final float x, final float y) {
        sb.setColor(this.barShadowColor);
        this.drawBarShadow(sb, x, y, 0, this.barLength);
        sb.setColor(this.barBgColor);
        this.drawBar(sb, x, y, 0, this.barLength);
    }

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    protected void drawBarShadow(SpriteBatch sb, float x, float y, float startLength, float length) {
        sb.draw(ImageMaster.HB_SHADOW_L, x + startLength - BAR_DIAMETER, y, BAR_DIAMETER, BAR_DIAMETER);
        sb.draw(ImageMaster.HB_SHADOW_B, x + startLength, y, length, BAR_DIAMETER);
        sb.draw(ImageMaster.HB_SHADOW_R, x + startLength + length, y, BAR_DIAMETER, BAR_DIAMETER);
    }

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    protected void drawBar(SpriteBatch sb, float x, float y, float startLength, float length) {
        drawBarMinEnd(sb, x, y, startLength, length);
        drawBarMiddle(sb, x, y, startLength, length);
        drawBarMaxEnd(sb, x, y, startLength, length);
    }

    protected void drawBarChunk(SpriteBatch sb, BarAmountChunk chunk) {
        switch (chunk.orderType) {
            case Min:
                drawBarMinEnd(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length);
                drawBarMiddle(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length);
                break;
            case Middle:
                drawBarMiddle(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length);
                break;
            case Max:
                drawBarMiddle(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length);
                drawBarMaxEnd(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length);
                break;
            case OnlyOne:
                drawBarMinEnd(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length);
                drawBarMiddle(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length);
                drawBarMaxEnd(sb, chunk.drawX, chunk.drawY, chunk.startLength, chunk.length);
                break;
        }
    }

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    protected final void renderBarTextWithColorAlphaChange(final SpriteBatch sb, final float x, final float y) {
        final float tmp = this.barTextColor.a;
        this.barTextColor.a *= this.healthHideTimer;
        renderBarText(sb, x, y);
        this.barTextColor.a = tmp;
    }

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    protected void renderBarText(SpriteBatch sb, final float x, final float y) {
        FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, makeBarText(), this.hitbox.cX, y + TEXT_OFFSET_Y,
                this.barTextColor);
    }


    protected void updateHbHoverFade() {
        if (this.hitbox.hovered) {
            this.healthHideTimer -= Gdx.graphics.getDeltaTime() * HIDE_SPEED;
            if (this.healthHideTimer < HEALTH_HIDE_TIMER_MIN) {
                this.healthHideTimer = HEALTH_HIDE_TIMER_MIN;
            }
        }
        else {
            this.healthHideTimer += Gdx.graphics.getDeltaTime() * HIDE_SPEED;
            if (this.healthHideTimer > 1.0F) {
                this.healthHideTimer = 1.0F;
            }
        }
    }

    private void calculateAllPosition() {
        reMakeSortedChunkList();
        for (int i = 0; i < sortedChunkList.size(); i++) {
            AmountChunk chunk = sortedChunkList.get(i);
            if (!(chunk instanceof BarAmountChunk)) continue;
            BarAmountChunk barAmountChunk = (BarAmountChunk) chunk;
            barAmountChunkMoveByIndex(barAmountChunk, getTotalAmount_InFrontOf(i));

            if (i == 0)
                barAmountChunk.orderType = BarAmountChunk.OrderType.Min;
            else if (i == sortedChunkList.size() - 1)
                barAmountChunk.orderType = BarAmountChunk.OrderType.Max;
            else
                barAmountChunk.orderType = BarAmountChunk.OrderType.Middle;
            if (sortedChunkList.size() == 1)
                barAmountChunk.orderType = BarAmountChunk.OrderType.OnlyOne;
        }
    }

    protected void barAmountChunkMoveByIndex(BarAmountChunk barAmountChunk, int totalAmountInFront) {
        barAmountChunk.teleport(getXDrawStart(), getYDrawStart());
        barAmountChunk.targetNewLength(
                chunkLength(totalAmountInFront),
                chunkLength(barAmountChunk.nowAmount));
    }

    protected AmountChunk makeNewAmountChunk(BarRenderUpdateMessage message) {
        return new BarAmountChunk(
                getXDrawStart(),
                getYDrawStart(),
                chunkLength(getTotalAmount()),
                chunkLength(message.newAmount),
                getNextOrder(), this);
    }

    protected void chunkHitBoxReSize(BarAmountChunk amountChunk) {
        switch (amountChunk.orderType) {
            case Min:
                amountChunk.hitbox.width = amountChunk.length + BAR_DIAMETER / 2;
                amountChunk.hitbox.x = amountChunk.drawX + amountChunk.startLength - BAR_DIAMETER / 2;
                break;
            case Middle:
                amountChunk.hitbox.width = amountChunk.length;
                amountChunk.hitbox.x = amountChunk.drawX + amountChunk.startLength;
                break;
            case Max:
                amountChunk.hitbox.width = amountChunk.length + BAR_DIAMETER / 2;
                amountChunk.hitbox.x = amountChunk.drawX + amountChunk.startLength;
                break;
            case OnlyOne:
                amountChunk.hitbox.width = amountChunk.length + BAR_DIAMETER;
                amountChunk.hitbox.x = amountChunk.drawX + amountChunk.startLength - BAR_DIAMETER / 2;
                break;
        }

        amountChunk.hitbox.height = BAR_DIAMETER;
        amountChunk.hitbox.moveY(this.hitbox.cY);
    }

    protected static class BarAmountChunk extends RenderOnThing.AmountChunk {
        public static final float HEALTH_HIDE_TIMER_MIN_CHUNK = 0.7f;
        protected static final float TIME_RESET = 1.2f;
        protected final BarRenderOnThing bar;
        public Color chunkColor = new Color(0f, 0f, 0f, 1f);

        protected Hitbox hitbox;
        protected float drawX;
        protected float drawY;
        protected float drawXTarget;
        protected float drawYTarget;
        protected float startLength;
        protected float length;
        protected float lengthTarget;
        protected float animTimer;
        protected float healthHideTimer = 1.0f;
        protected float startLengthTarget;
        protected OrderType orderType = OrderType.OnlyOne;

        public BarAmountChunk(float drawX, float drawY, float startLength, float length, int order, BarRenderOnThing bar) {
            super(order);
            this.bar = bar;
            this.drawX = drawX;
            this.drawY = drawY;
            this.startLength = startLength;
            this.length = 0;
            this.drawXTarget = drawX;
            this.drawYTarget = drawY;
            this.lengthTarget = length;
            this.hitbox = new Hitbox(0, 0);
//            hitboxMove();
        }

//        private void hitboxMove() {
//            this.hitbox.move(drawX, drawY);
//        }

        private void updateHbHoverFade() {
            if (this.hitbox.hovered) {
                this.healthHideTimer -= Gdx.graphics.getDeltaTime() * HIDE_SPEED;
                if (this.healthHideTimer < HEALTH_HIDE_TIMER_MIN_CHUNK) {
                    this.healthHideTimer = HEALTH_HIDE_TIMER_MIN_CHUNK;
                }
            }
            else {
                this.healthHideTimer += Gdx.graphics.getDeltaTime() * HIDE_SPEED;
                if (this.healthHideTimer > 1.0F) {
                    this.healthHideTimer = 1.0F;
                }
            }
        }

        public void render(final SpriteBatch sb) {
            renderAmountBar(sb);
        }

        public void update() {
            updateHbDamageAnimation();
        }

        public void updateHitBox() {
            bar.chunkHitBoxReSize(this);
            this.hitbox.update();
            bar.isChunkHovered = this.hitbox.hovered;
            updateHbHoverFade();
            if (this.hitbox.hovered)
                bar.renderTip(new ArrayList<>(Collections.singletonList(tip)));
        }

        private void renderAmountBar(final SpriteBatch sb) {
            if (this.nowAmount == 0) return;

            final float tmp = this.chunkColor.a;
            this.chunkColor.a *= this.healthHideTimer;
            sb.setColor(this.chunkColor);
            bar.drawBarChunk(sb, this);
            this.chunkColor.a = tmp;
        }

        public BarAmountChunk teleport(float drawX, float drawY) {
            this.drawX = drawX;
            this.drawY = drawY;
            this.drawXTarget = drawX;
            this.drawYTarget = drawY;
            return this;
        }

        public void move(float drawXTarget, float drawYTarget) {
            this.drawXTarget = drawXTarget;
            this.drawYTarget = drawYTarget;
        }

        public void targetNewLength(float startLengthTarget, float lengthTarget) {
            this.startLengthTarget = startLengthTarget;
            this.lengthTarget = lengthTarget;
        }

        private void updateHbDamageAnimation() {
            if (this.animTimer > 0.0F) this.animTimer -= Gdx.graphics.getDeltaTime();
//            if (this.animTimer > 0.0F) return;
            if (this.startLengthTarget != this.startLength)
                this.startLength = MathHelper.uiLerpSnap(this.startLength, this.startLengthTarget);
            if (this.lengthTarget != this.length) this.length = MathHelper.uiLerpSnap(this.length, this.lengthTarget);
            if (this.drawXTarget != this.drawX) this.drawX = MathHelper.uiLerpSnap(this.drawX, this.drawXTarget);
            if (this.drawYTarget != this.drawY) this.drawY = MathHelper.uiLerpSnap(this.drawY, this.drawYTarget);
        }

        public BarAmountChunk setTip(BarRenderUpdateMessage.ToolTip tip) {
            this.tip = new PowerTip(tip.name, tip.description);
            return this;
        }

        public BarAmountChunk setMaxAmount(int maxAmount) {
            this.maxAmount = maxAmount;
            return this;
        }

        public BarAmountChunk setNowAmount(int nowAmount) {
            this.nowAmount = nowAmount;
            return this;
        }

        public BarAmountChunk applyNoPositionMessage(BarRenderUpdateMessage message) {
            if (message.newAmount != 0 && message.newAmount != this.nowAmount) {
                this.animTimer = TIME_RESET;
                this.nowAmount = message.newAmount;
            }
            if (message.maxAmount != 0)
                this.maxAmount = message.maxAmount;
            if (message.chunkColor != null)
                this.chunkColor = message.chunkColor;
            if (message.toolTip != null)
                this.tip = new PowerTip(message.toolTip.name, message.toolTip.description);
            return this;
        }

        protected enum OrderType {
            Min,
            Middle,
            Max,
            OnlyOne
        }
    }

}