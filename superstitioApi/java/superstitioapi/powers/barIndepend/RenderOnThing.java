package superstitioapi.powers.barIndepend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class RenderOnThing {
    public static final float HIDE_SPEED = 4.0F;
    public static final float HEALTH_HIDE_TIMER_MIN = 0.2F;
    public static final float BAR_OFFSET_Y = 28.0f * Settings.scale;
    protected static final float TEXT_OFFSET_Y = 11.0f * Settings.scale;
    //绘制相关
    protected static final float BAR_DIAMETER = 20.0f * Settings.scale;
    //    public static final float HITBOX_HEIGHT = ;
//    protected static final float BG_OFFSET_Y = -31.0f * Settings.scale;
    protected final Map<String, AmountChunk> amountChunkWithUuid = new HashMap<>();
    protected final Supplier<Hitbox> hitboxBondTo;
    protected final float HeightOffset;
    public Color barTextColor;
    public Hitbox hitbox;
    public String uuid_self;
    protected float fontScale;
    protected float healthHideTimer = 1.0f;
    protected List<AmountChunk> sortedChunkList = new ArrayList<>();
    private String rawBarText = "%d/%d";

    public RenderOnThing(final Supplier<Hitbox> hitbox, HasBarRenderOnCreature power) {
        this.uuid_self = power.uuidPointTo();
        this.hitboxBondTo = hitbox;
        this.hitbox = new Hitbox(hitboxBondTo.get().width + BAR_DIAMETER * 3f, BAR_DIAMETER * 1.5f);
        updateHitBoxPlace(this.hitbox);

        this.HeightOffset = power.Height();
        this.barTextColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void removeChunk(HasBarRenderOnCreature hasBarRenderOnCreature) {
        amountChunkWithUuid.remove(hasBarRenderOnCreature.uuidOfSelf());
        reMakeSortedChunkList();
    }

    public void updateHitBoxPlace(Hitbox hitbox) {
        hitbox.move(hitboxBondTo.get().cX,
                HeightOffset + hitboxBondTo.get().cY + hitboxBondTo.get().height / 2);
    }

    public boolean isUuidInThis(String uuid_chunk) {
        return amountChunkWithUuid.containsKey(uuid_chunk);
    }

    protected int getMaxBarAmount() {
        return amountChunkWithUuid.values().stream().mapToInt(chunk -> chunk.maxAmount).max().orElse(10);
    }

    public void render(SpriteBatch sb) {
        renderBarTextWithColorAlphaChange(sb, getXDrawStart(), getYDrawStart());
        if (Settings.isDebug || Settings.isInfo) {
            renderDebug(sb);
        }
    }

    protected void renderDebug(SpriteBatch sb) {
        this.hitbox.render(sb);
    }

    public void update() {
        updateHitBoxPlace(this.hitbox);
        this.hitbox.update();
        for (AmountChunk amountChunk : this.amountChunkWithUuid.values()) {
            amountChunk.update();
        }
        update_showTips(this.hitbox);
        updateHbHoverFade();
    }

    protected final ArrayList<PowerTip> AllTips() {
        return this.amountChunkWithUuid.values().stream().filter(amountChunk -> amountChunk.tip != null)
                .map(amountChunk -> amountChunk.tip).collect(Collectors.toCollection(ArrayList::new));
    }

    protected void update_showTips(Hitbox hitbox) {
        if (hitbox.hovered) {
            renderTip(AllTips());
        }
        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
    }

    public void renderTip(ArrayList<PowerTip> tips) {
        if (!((float) InputHelper.mX < 1400.0F * Settings.scale)) {
            TipHelper.queuePowerTips((float) InputHelper.mX - 350.0F * Settings.scale,
                    (float) InputHelper.mY - 50.0F * Settings.scale, tips);
            return;
        }
        TipHelper.queuePowerTips((float) InputHelper.mX + 50.0F * Settings.scale,
                (float) InputHelper.mY + 50.0F * Settings.scale, tips);
    }

    protected abstract float getYDrawStart();

    protected abstract float getXDrawStart();

    /**
     * @param x getXDrawStart
     * @param y getYDrawStart
     */
    protected void renderBarTextWithColorAlphaChange(final SpriteBatch sb, final float x, final float y) {
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

    protected final String makeBarText() {
        boolean hasTwoDs = rawBarText.contains("%d") && rawBarText.indexOf("%d") != rawBarText.lastIndexOf("%d");
        boolean hasD = rawBarText.contains("%d");
        if (hasTwoDs)
            return String.format(rawBarText, this.getTotalAmount(), this.getMaxBarAmount());
        if (hasD)
            return String.format(rawBarText, this.getTotalAmount());
        return rawBarText;
    }

    protected void updateHbHoverFade() {
        if (this.hitbox.hovered) {
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
        this.rawBarText = message.rawTextOnBar;
        messageTargetChunk.applyNoPositionMessage(message);
        if (message.detail != null)
            message.detail.accept(this);
    }

    private void addNewAmountChunk(BarRenderUpdateMessage message) {
        this.amountChunkWithUuid.put(message.uuidOfPower,
                makeNewAmountChunk(message).applyNoPositionMessage(message));
    }

    protected final int getTotalAmount_InFrontOf(int sumToIndex_InFrontOf) {
        if (sumToIndex_InFrontOf <= 0)
            return 0;
        return getChunkOrdered(sumToIndex_InFrontOf - 1).stream().mapToInt(chunk -> chunk.nowAmount).sum();
    }

    protected final List<AmountChunk> getChunkOrdered(int index) {
        reMakeSortedChunkList();
        if (sortedChunkList.size() <= 1)
            return sortedChunkList;
        if (index <= 0)
            return Collections.singletonList(sortedChunkList.get(0));
        return sortedChunkList.subList(0, index);
    }

    protected void reMakeSortedChunkList() {
        List<AmountChunk> orderedList = new ArrayList<>();
        amountChunkWithUuid.values().stream().sorted(AmountChunk::compareTo).forEachOrdered(orderedList::add);
        this.sortedChunkList = orderedList;
    }

    protected final int getTotalAmount() {
        return amountChunkWithUuid.values().stream().mapToInt(value -> value.nowAmount).sum();
    }

    protected abstract AmountChunk makeNewAmountChunk(BarRenderUpdateMessage message);

    protected int getNextOrder() {
        return amountChunkWithUuid.values().stream().mapToInt(value -> value.order).max().orElse(-1) + 1;
    }

    protected abstract static class AmountChunk implements Comparable<AmountChunk> {
        public final int order;
        public PowerTip tip;
        public int maxAmount = 0;
        public int nowAmount = 0;

        public AmountChunk(int order) {
            this.order = order;
        }


        public abstract void render(final SpriteBatch sb);

        public abstract void update();

        public AmountChunk setTip(BarRenderUpdateMessage.ToolTip tip) {
            this.tip = new PowerTip(tip.name, tip.description);
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

        public abstract AmountChunk applyNoPositionMessage(BarRenderUpdateMessage message);
//            if (message.newAmount != 0 && message.newAmount != this.nowAmount) {
//                this.nowAmount = message.newAmount;
//            }
//            if (message.maxAmount != 0)
//                this.maxAmount = message.maxAmount;
//            if (message.toolTip != null)
//                this.tip = new PowerTip(message.toolTip.name, message.toolTip.description);
//            return this;


        @Override
        public int compareTo(AmountChunk other) {
            return Integer.compare(this.order, other.order);
        }

        public int getOrder() {
            return this.order;
        }
    }

}