package superstitioapi.cardPool;

import basemod.IUIElement;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public abstract class BaseCardPool implements IUIElement {
    public static final float COVER_DRAW_SCALE = 0.5f;
    public static final float COVER_DRAW_HOVER_SCALE_RATE = 1.25f;
    public final static float HB_W_CARD = 300.0F * Settings.scale;
    protected final AbstractCard poolCover;
    private final Predicate<AbstractCard> addedCard;
    private final Predicate<AbstractCard> banedCard;
    private final ArrayList<SafeCardGlowBorder> glowList = new ArrayList<>();
    public float target_x;
    public float target_y;
    private boolean isSelect = true;
    private float glowTimer = 0.0f;
    private boolean isGlowing;

    public BaseCardPool(IsCardPoolCover poolCover, final float x, final float y) {
        this(poolCover.getThis(), x, y, poolCover.getAddedCard(), poolCover.getBanedCard());
    }

    public BaseCardPool(AbstractCard poolCover, final float x, final float y, List<AbstractCard> addedCardList, List<AbstractCard> banedCardList) {
        this(poolCover, x, y, addedCardList::contains, banedCardList::contains);
    }

    public BaseCardPool(AbstractCard poolCover, final float x, final float y, Predicate<AbstractCard> addedCard, Predicate<AbstractCard> banedCard) {
        this.poolCover = poolCover;
        this.target_x = x;
        this.target_y = y;
        this.addedCard = addedCard;
        this.banedCard = banedCard;

        this.poolCover.drawScale = COVER_DRAW_SCALE;
        this.poolCover.transparency = 1.0f;
        this.poolCover.current_x = this.target_x;
        this.poolCover.current_y = this.target_y;
        this.poolCover.target_x = this.target_x;
        this.poolCover.target_y = this.target_y;

        this.poolCover.targetDrawScale = COVER_DRAW_SCALE;
        this.poolCover.isCostModified = false;
    }

    public boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getId() {
        return poolCover.cardID;
    }

    public void transportTo(float x, float y) {
        this.target_x = x;
        this.target_y = y;
        this.poolCover.current_x = this.target_x;
        this.poolCover.current_y = this.target_y;
        this.poolCover.target_x = this.target_x;
        this.poolCover.target_y = this.target_y;
    }

    public void moveTo(float x, float y) {
        this.target_x = x;
        this.target_y = y;
    }

    public Predicate<AbstractCard> getAddedCard() {
        if (addedCard == null || !isSelect)
            return card -> false;
        return addedCard;
    }

    public Predicate<AbstractCard> getBanedCard() {
        if (banedCard == null || !isSelect)
            return card -> false;
        return banedCard;
    }

    protected void clickThisPool() {
        this.isSelect = !this.isSelect;
    }

    private void updateGlow() {
        if (this.isGlowing) {
            this.glowTimer -= Gdx.graphics.getDeltaTime();
            if (this.glowTimer < 0.0F) {
                this.glowList.add(new SafeCardGlowBorder(this.poolCover, this.poolCover.glowColor));
                this.glowTimer = 0.3F;
            }
        }

        Iterator<SafeCardGlowBorder> i = this.glowList.iterator();
        while (i.hasNext()) {
            SafeCardGlowBorder e = i.next();
            e.update();
            if (e.isDone) {
                i.remove();
            }
        }

    }

    private void renderGlow(SpriteBatch sb) {
        if (!Settings.hideCards) {
            ReflectionHacks.privateMethod(AbstractCard.class, "renderMainBorder", SpriteBatch.class).invoke(this.poolCover, sb);
//            this.poolCover.renderMainBorder(sb);

            for (SafeCardGlowBorder safeCardGlowBorder : this.glowList) {
                ((AbstractGameEffect) safeCardGlowBorder).render(sb);
            }

            sb.setBlendFunction(770, 771);
        }

    }

    @Override
    public void render(final SpriteBatch sb) {
        if (this.poolCover.hb.hovered) {
            sb.setColor(Color.WHITE);
        } else {
            sb.setColor(Color.LIGHT_GRAY);
        }
        updateGlow();
        renderGlow(sb);
        this.poolCover.render(sb);
        this.poolCover.hb.render(sb);

        FontHelper.cardDescFont_N.getData().setScale(1.0f);
        FontHelper.cardDescFont_L.getData().setScale(1.0f);
        FontHelper.cardTitleFont.getData().setScale(1.0f);
    }

    @Override
    public void update() {
        this.poolCover.target_x = this.target_x;
        this.poolCover.target_y = this.target_y;

        this.poolCover.update();
        this.poolCover.updateHoverLogic();
        if (this.poolCover.hb.hovered) {
            this.poolCover.targetDrawScale = COVER_DRAW_SCALE * COVER_DRAW_HOVER_SCALE_RATE;
        } else {
            this.poolCover.targetDrawScale = COVER_DRAW_SCALE;
        }

//        this.poolCover.drawScale = COVER_DRAW_SCALE;
//        this.poolCover.targetDrawScale = COVER_DRAW_SCALE;
        if (this.poolCover.hb.hovered && InputHelper.justClickedLeft) {
            CardCrawlGame.sound.play("UI_CLICK_1");
            clickThisPool();
        }
        if (isSelect) {
            this.isGlowing = true;
        } else {
            this.isGlowing = false;
            for (SafeCardGlowBorder safeCardGlowBorder : this.glowList) {
                safeCardGlowBorder.duration /= 5.0F;
            }
        }
        FontHelper.cardDescFont_N.getData().setScale(1.0f);
        FontHelper.cardDescFont_L.getData().setScale(1.0f);
        FontHelper.cardTitleFont.getData().setScale(1.0f);
    }

    @Override
    public int renderLayer() {
        return 0;
    }

    @Override
    public int updateOrder() {
        return 0;
    }

    public interface IsCardPoolCover {
        AbstractCard getThis();

        Predicate<AbstractCard> getAddedCard();

        Predicate<AbstractCard> getBanedCard();
    }

    public static class SafeCardGlowBorder extends AbstractGameEffect {
        private final AbstractCard card;
        private final TextureAtlas.AtlasRegion img;
        private float scale;

        public SafeCardGlowBorder(AbstractCard card) {
            this(card, Color.valueOf("30c8dcff"));
        }

        public SafeCardGlowBorder(AbstractCard card, Color gColor) {
            this.card = card;
            switch (card.type) {
                case POWER:
                    this.img = ImageMaster.CARD_POWER_BG_SILHOUETTE;
                    break;
                case ATTACK:
                    this.img = ImageMaster.CARD_ATTACK_BG_SILHOUETTE;
                    break;
                default:
                    this.img = ImageMaster.CARD_SKILL_BG_SILHOUETTE;
            }

            this.duration = 1.2F;
//            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.color = gColor.cpy();
//            } else {
//                this.color = Color.GREEN.cpy();
//            }

        }

        public void update() {
            this.scale = (1.0F + Interpolation.pow2Out.apply(0.03F, 0.11F, 1.0F - this.duration)) * this.card.drawScale * Settings.scale;
            this.color.a = this.duration / 2.0F;
            this.duration -= Gdx.graphics.getDeltaTime();
            if (this.duration < 0.0F) {
                this.isDone = true;
                this.duration = 0.0F;
            }

        }

        public void render(SpriteBatch sb) {
            sb.setColor(this.color);
            sb.draw(this.img, this.card.current_x + this.img.offsetX - (float) this.img.originalWidth / 2.0F,
                    this.card.current_y + this.img.offsetY - (float) this.img.originalHeight / 2.0F,
                    (float) this.img.originalWidth / 2.0F - this.img.offsetX, (float) this.img.originalHeight / 2.0F - this.img.offsetY,
                    (float) this.img.packedWidth, (float) this.img.packedHeight, this.scale, this.scale, this.card.angle);
        }

        public void dispose() {
        }
    }

}
