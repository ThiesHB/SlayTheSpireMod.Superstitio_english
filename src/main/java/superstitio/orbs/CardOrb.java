package superstitio.orbs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import superstitio.DataManager;

import java.util.Arrays;
import java.util.function.Predicate;

public abstract class CardOrb extends AbstractLupaOrb {
    public static final String ORB_ID = DataManager.MakeTextID(SexMarkEmptySlot.class.getSimpleName());
    public static final float ANIMATION_Y_SCALE = 1.0f;
    public static final float DRAW_SCALE_BIG = 1.0f;

    public static final float DRAW_SCALE_MIDDLE = 0.5f;
    public static final float DRAW_SCALE_SMALL = 0.25f;
    private static final float TIMER_ANIMATION = 2.0f;
    public final CardGroup thisCardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public AbstractCard card;
    public AbstractCard.CardTarget targetType = AbstractCard.CardTarget.NONE;
    //        protected float animationTimer = TIMER_SPEED;
//    private BiFunction<Float, AbstractCard, Vector2> onUseAnimation;
//    private AbstractCard cardInUse;
    public CardOrbMovingType movingType;
    public DrawOrder drawOrder = CardOrb.DrawOrder.bottom;
    protected boolean canInterrupt = true;
    protected boolean movingIsStop = true;
    protected CardGroup originGroupOfCard = null;
    protected Predicate<AbstractCard> cardMatcher = (card) -> true;

    public CardOrb(AbstractCard card) {
        super(ORB_ID);
        this.card = card.makeStatEquivalentCopy();
        this.card.targetDrawScale = DRAW_SCALE_SMALL;
        this.card.isCostModified = false;
        this.card.current_x = card.current_x;
        this.card.current_y = card.current_y;
        this.card.costForTurn = -2;

        this.targetType = this.card.target;
        this.thisCardGroup.addToTop(this.card);
        this.movingType = CardOrbMovingType.Idle;
    }

    @Override
    public final void onEvoke() {
        onRemoveCard();
    }

    protected abstract void onRemoveCard();

    public CardOrb setDesc(String description) {
        this.description = description;
        this.card.rawDescription = description;
        this.card.initializeDescription();
        return this;
    }

    protected void setCanNotInterrupt() {
        canInterrupt = false;
    }

    public abstract boolean shouldRemove();

    @Override
    public void render(SpriteBatch spriteBatch) {
        float offset = YOffsetBoBing();
        card.current_y += offset;
        card.render(spriteBatch);
        if (card.hb.hovered) {
            card.renderCardTip(spriteBatch);
        }
        card.current_y -= offset;
    }

    public void tryMoveTo(Vector2 vector2) {
        this.card.target_x = vector2.x;
        this.card.target_y = vector2.y;
        movingIsStop = false;

    }

    @Override
    public void update() {
        this.hb.update();
        this.card.update();
        this.card.updateHoverLogic();
    }

//    protected abstract ActionUtility.VoidSupplier checkAndSetTheType();

    protected boolean checkShouldStopMoving() {
        if (this.movingType == CardOrbMovingType.Idle) return true;
        return Math.abs(this.card.current_y - this.card.target_y) < 0.01f && Math.abs(this.card.current_x - this.card.target_x) < 0.01f;
    }

    protected void updateAnimationIdle() {
        if (isCardHovered()) {
            this.card.target_x = this.cX;
            this.card.target_y = this.cY + YOffsetWhenHovered();
            card.targetDrawScale = DRAW_SCALE_BIG;
            this.drawOrder = DrawOrder.top;
        } else {
            this.card.target_x = this.cX;
            this.card.target_y = this.cY + YOffsetWhenHovered();
            card.targetDrawScale = DRAW_SCALE_SMALL;
            this.drawOrder = DrawOrder.bottom;
        }
    }

    @Override
    public void updateDescription() {
        if (this.card == null) return;
        this.card.calculateDamageDisplay(CardOrb_CardTrigger.getHoveredMonsterSafe());
        this.card.initializeDescription();
        super.updateDescription();
    }

    protected float YOffsetBoBing() {
        return ANIMATION_Y_SCALE * this.bobEffect.y * this.card.drawScale * DRAW_SCALE_BIG / DRAW_SCALE_SMALL;
    }

    protected boolean isCardHovered() {
        return this.card.hb.hovered;
    }

    protected float YOffsetWhenHovered() {
        return -this.card.hb.height * (this.card.drawScale) / DRAW_SCALE_BIG / 2;
    }

    @Override
    public void playChannelSFX() {

    }

    @SafeVarargs
    public final CardOrb setCardPredicate(Predicate<AbstractCard>... cardMatchers) {
        Arrays.stream(cardMatchers).forEach(mather -> this.cardMatcher = this.cardMatcher.and(mather));
        return this;
    }

    public final void onCardUsed(AbstractCard card) {
        if (card == null) return;
        if (cardMatcher.test(card))
            onProperCardUsed(card);
    }

    @Override
    public void applyFocus() {
    }

    public abstract void onProperCardUsed(AbstractCard card);

    public enum DrawOrder {
        bottom,
        middle,
        top
    }


    public enum CardOrbMovingType {
        Idle,//闲置
        focusOnMonster,
        focusOnSelf,
        focusOnNothing,
        Moving
    }
}
