package superstitio.powers.barIndepend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;

import java.util.*;
import java.util.stream.Stream;

import static superstitio.powers.barIndepend.BarRenderUpdateMessage.ToolTip;

public class BarRenderOnCreature {
    protected static final float BAR_OFFSET_Y = -28.0f * Settings.scale;
    protected static final float TEXT_OFFSET_Y = 11.0f * Settings.scale;
    //绘制相关
    private static final float BAR_DIAMETER = 20.0f * Settings.scale;
    private static final float BG_OFFSET_X = 31.0f * Settings.scale;
    protected final float barWidth;
    private final Map<String, AmountChunk> amountChunkWithUuid = new HashMap<>();
    private final Hitbox hitboxBondTo;
    private final float HeightOffset;
    public Color barBgColor;
    public Color barShadowColor;
    public Color barTextColor;
    public Hitbox hitbox;
    public String uuid_self;
    private float fontScale;
    private float healthHideTimer = 0.0f;
    private String rawBarText = "%d/%d";

    public BarRenderOnCreature(final Hitbox hitbox, HasBarRenderOnCreature power) {
        this.hitboxBondTo = hitbox;
        this.uuid_self = power.uuidPointTo();
        this.barWidth = hitboxBondTo.width;
        this.HeightOffset = power.Height();
        this.hitbox = new Hitbox(hitboxBondTo.width + BAR_DIAMETER * 3f, BAR_DIAMETER * 1.5f);

        this.barBgColor = new Color(0f, 0f, 0f, 0.3f);
        this.barShadowColor = new Color(0f, 0f, 0f, 0.3f);
        this.barTextColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void updateHitBox(Hitbox hitbox) {
        hitbox.move(hitboxBondTo.cX, HeightOffset + hitboxBondTo.cY + hitboxBondTo.height / 2 + BG_OFFSET_X * 2);
    }

    public boolean isUuidInThis(String uuid_chunk) {
        return amountChunkWithUuid.containsKey(uuid_chunk);
    }

    protected int getMaxBarAmount() {
        return amountChunkWithUuid.values().stream().mapToInt(chunk -> chunk.maxAmount).max().orElse(10);
    }

    protected float chunkWidth(int amount) {
        float v = (this.barWidth * (amount % getMaxBarAmount())) / (float) getMaxBarAmount();
        if (amount % getMaxBarAmount() == 0 && amount != 0) v = this.barWidth;
        return v;
    }

    public void render(SpriteBatch sb) {
        renderBar(sb);
    }

    public void update() {
        updateHitBox(this.hitbox);
        update_showTips(this.hitbox);
        updateHbHoverFade();
        this.amountChunkWithUuid.values().forEach(AmountChunk::update);
    }

    protected Stream<ToolTip> AllTips() {
        return this.amountChunkWithUuid.values().stream().filter(amountChunk -> amountChunk.toolTip != null).map(amountChunk -> amountChunk.toolTip);
    }

    protected void update_showTips(Hitbox hitbox) {
        hitbox.update();
        if (hitbox.hovered) {
            AllTips().forEach((toolTip) -> TipHelper.renderGenericTip(hitbox.cX + 96.0F * Settings.scale, hitbox.cY + 64.0F * Settings.scale,
                    toolTip.name, toolTip.description));
        }
        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
    }

    protected void renderBar(SpriteBatch sb) {
        float OwnerX = getXDrawStart();
        float OwnerY = getYDrawStart();
        this.renderAmountBarBackGround(sb, OwnerX, OwnerY);
        this.amountChunkWithUuid.values().forEach(amountChunk -> amountChunk.render(sb));
        this.renderBarText(sb, OwnerY);
    }

    private float getYDrawStart() {
        return this.hitbox.cY + this.hitbox.height / 2.0f;
    }

    private float getXDrawStart() {
        return this.hitbox.cX - this.barWidth / 2.0f;
    }

    private void renderAmountBarBackGround(final SpriteBatch sb, final float x, final float y) {
        sb.setColor(this.barShadowColor);
        sb.draw(ImageMaster.HB_SHADOW_L, x - BAR_DIAMETER, y - BG_OFFSET_X + 3.0f * Settings.scale, BAR_DIAMETER, BAR_DIAMETER);
        sb.draw(ImageMaster.HB_SHADOW_B, x, y - BG_OFFSET_X + 3.0f * Settings.scale, this.barWidth, BAR_DIAMETER);
        sb.draw(ImageMaster.HB_SHADOW_R, x + this.barWidth, y - BG_OFFSET_X + 3.0f * Settings.scale, BAR_DIAMETER, BAR_DIAMETER);
        sb.setColor(this.barBgColor);
        sb.draw(ImageMaster.HEALTH_BAR_L, x - BAR_DIAMETER, y + BAR_OFFSET_Y, BAR_DIAMETER, BAR_DIAMETER);
        sb.draw(ImageMaster.HEALTH_BAR_B, x, y + BAR_OFFSET_Y, this.barWidth, BAR_DIAMETER);
        sb.draw(ImageMaster.HEALTH_BAR_R, x + this.barWidth, y + BAR_OFFSET_Y, BAR_DIAMETER, BAR_DIAMETER);
    }

    private void renderBarText(final SpriteBatch sb, final float y) {
        final float tmp = this.barTextColor.a;
        this.barTextColor.a *= this.healthHideTimer;
        FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, makeBarText(), this.hitbox.cX, y + BAR_OFFSET_Y + TEXT_OFFSET_Y,
                this.barTextColor);
        this.barTextColor.a = tmp;
    }

    protected String makeBarText() {
        boolean hasTwoDs = rawBarText.contains("%d") && rawBarText.indexOf("%d") != rawBarText.lastIndexOf("%d");
        boolean hasD = rawBarText.contains("%d");
        if (hasTwoDs)
            return String.format(rawBarText, this.getTotalAmount(), this.getMaxBarAmount());
        if (hasD)
            return String.format(rawBarText, this.getTotalAmount());
        return rawBarText;
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

    /**
     * 在初始化后，如果想要修改，请使用这个函数
     *
     * @param message 消息
     */
    public void tryApplyMessage(BarRenderUpdateMessage message) {
        if (!Objects.equals(this.uuid_self, message.uuidOfBar)) return;
        if (!amountChunkWithUuid.containsKey(message.uuidOfPower)) {
            addNewAmountChunk(message);
            return;
        }
        AmountChunk messageTargetChunk = amountChunkWithUuid.get(message.uuidOfPower);
        rawBarText = message.rawTextOnBar;
        messageTargetChunk.applyNoPositionMessage(message);
        this.calculateAllPosition();
        if (message.detail != null)
            message.detail.accept(this);
    }

    private void calculateAllPosition() {
        for (int i = 0; i < getSortedChunkList().size(); i++) {
            AmountChunk chunk = getSortedChunkList().get(i);
            chunk.move(getXDrawStart() + chunkWidth(getTotalAmount(i)), getYDrawStart(), chunkWidth(chunk.nowAmount));
        }
    }


    protected int getTotalAmount(int sumToIndex_InFrontOf) {
        return getMinOrdered(sumToIndex_InFrontOf).stream().mapToInt(chunk -> chunk.nowAmount).sum();
    }

    protected List<AmountChunk> getMinOrdered(int index) {
        if (getSortedChunkList().size() == 1)
            return new ArrayList<>();
        return getSortedChunkList().subList(0, index - 1);
    }

    private List<AmountChunk> getSortedChunkList() {
        List<AmountChunk> orderedList = new ArrayList<>();
        amountChunkWithUuid.values().stream().sorted(AmountChunk::compareTo).forEachOrdered(orderedList::add);
        return orderedList;
    }

    protected int getTotalAmount() {
        return amountChunkWithUuid.values().stream().mapToInt(value -> value.nowAmount).sum();
    }

    private void addNewAmountChunk(BarRenderUpdateMessage message) {
        this.amountChunkWithUuid.put(message.uuidOfPower, new AmountChunk(getXDrawStart() + chunkWidth(getTotalAmount()), getYDrawStart(),
                chunkWidth(message.newAmount), getNextOrder()).applyNoPositionMessage(message));
    }

    private int getNextOrder() {
        return amountChunkWithUuid.values().stream().mapToInt(value -> value.order).max().orElse(0) + 1;
    }

    protected static class AmountChunk implements Comparable<AmountChunk> {
        private static final float TIME_RESET = 1.2f;
        public final int order;
        public Color chunkColor = new Color(0f, 0f, 0f, 1f);
        public ToolTip toolTip;
        public int maxAmount = 0;
        public int nowAmount = 0;
        private float drawX;
        private float drawY;
        private float drawXTarget;
        private float drawYTarget;
        private float width;
        private float widthTarget;
        private float animTimer;

        public AmountChunk(float drawX, float drawY, float width, int order) {
            this.drawX = drawX;
            this.drawY = drawY;
            this.width = 0;
            this.drawXTarget = drawX;
            this.drawYTarget = drawY;
            this.widthTarget = width;
            this.order = order;
        }

        public void render(final SpriteBatch sb) {
            renderAmountBar(sb);
        }

        public void update() {
            updateHbDamageAnimation();
        }

        private void renderAmountBar(final SpriteBatch sb) {
            if (this.nowAmount == 0) return;
            sb.setColor(this.chunkColor);
            sb.draw(ImageMaster.HEALTH_BAR_L, drawX - BAR_DIAMETER, drawY + BAR_OFFSET_Y, BAR_DIAMETER, BAR_DIAMETER);
            sb.draw(ImageMaster.HEALTH_BAR_B, drawX, drawY + BAR_OFFSET_Y, width, BAR_DIAMETER);
            sb.draw(ImageMaster.HEALTH_BAR_R, drawX + width, drawY + BAR_OFFSET_Y, BAR_DIAMETER, BAR_DIAMETER);
        }

        public void move(float drawXTarget, float drawYTarget, float widthTarget) {
            this.drawXTarget = drawXTarget;
            this.drawYTarget = drawYTarget;
            this.widthTarget = widthTarget;
        }

        private void updateHbDamageAnimation() {
            if (this.animTimer > 0.0F) this.animTimer -= Gdx.graphics.getDeltaTime();
//            if (this.animTimer > 0.0F) return;
            if (this.widthTarget != this.width) this.width = MathHelper.uiLerpSnap(this.width, this.widthTarget);
            if (this.drawXTarget != this.drawX) this.drawX = MathHelper.uiLerpSnap(this.drawX, this.drawXTarget);
            if (this.drawYTarget != this.drawY) this.drawY = MathHelper.uiLerpSnap(this.drawY, this.drawYTarget);
        }

        public AmountChunk setToolTip(ToolTip toolTip) {
            this.toolTip = toolTip;
            return this;
        }

        public AmountChunk setMaxAmount(int maxAmount) {
            this.maxAmount = maxAmount;
            return this;
        }

        public AmountChunk setNowAmount(int nowAmount) {
            this.nowAmount = nowAmount;
            return this;
        }

        public AmountChunk applyNoPositionMessage(BarRenderUpdateMessage message) {
            if (message.newAmount != 0 && message.newAmount != this.nowAmount) {
                this.animTimer = TIME_RESET;
                this.nowAmount = message.newAmount;
            }
            if (message.maxAmount != 0)
                this.maxAmount = message.maxAmount;
            if (message.chunkColor != null)
                this.chunkColor = message.chunkColor;
            if (message.toolTip != null)
                this.toolTip = message.toolTip;
            return this;
        }

        @Override
        public int compareTo(AmountChunk other) {
            return Integer.compare(this.order, other.order);
        }

        public int getOrder() {
            return this.order;
        }
    }

}