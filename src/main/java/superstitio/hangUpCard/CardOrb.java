package superstitio.hangUpCard;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import superstitio.DataManager;
import superstitio.orbs.AbstractLupaOrb;
import superstitio.orbs.SexMarkEmptySlot;
import superstitio.utils.ActionUtility;

public abstract class CardOrb extends AbstractLupaOrb {
    public static final String ORB_ID = DataManager.MakeTextID(SexMarkEmptySlot.class);
    public static final float ANIMATION_Y_SCALE = 1.0f;
    public static final float DRAW_SCALE_BIG = 0.9f;

    public static final float DRAW_SCALE_MIDDLE = 0.5f;
    public static final float DRAW_SCALE_SMALL = 0.25f;
    protected static final float DRAW_SCALE_SMALL_BIGGER = 0.30f;
    private static final float TIMER_ANIMATION = 2.0f;
    public final CardGroup thisCardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    protected final AbstractCard card;
    protected final AbstractCard originCard;
    private final CardGroup cardHolder = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public CardGroup cardGroupReturnAfterEvoke = null;
    public AbstractCard.CardTarget targetType = AbstractCard.CardTarget.NONE;
    public boolean evokeOnEndOfTurn;
    public ActionUtility.FunctionReturnSelfType movingType;
    public DrawOrder drawOrder = CardOrb.DrawOrder.bottom;
    public boolean shouldRemove;
    private boolean isRemoved;

    public CardOrb(AbstractCard card, CardGroup cardGroupReturnAfterEvoke) {
        super(ORB_ID);
        this.cardGroupReturnAfterEvoke = cardGroupReturnAfterEvoke;
        this.cardHolder.addToTop(card);
        this.originCard = card;
        this.originCard.targetDrawScale = DRAW_SCALE_SMALL;

        this.card = card.makeStatEquivalentCopy(); //什么替身文学，绷不住了
        this.card.drawScale = card.drawScale;
        this.card.transparency = 1.0f;
        this.card.current_x = card.current_x;
        this.card.current_y = card.current_y;


        this.card.targetDrawScale = DRAW_SCALE_SMALL;
        this.card.isCostModified = false;

        this.card.costForTurn = -2;

        this.targetType = this.card.target;
        this.thisCardGroup.addToTop(this.card);
        this.movingType = State_Idle();

        this.evokeOnEndOfTurn = true;
    }

    protected ActionUtility.FunctionReturnSelfType State_Idle() {
        showPassiveNum();
        updateAnimationIdle();
        return this::State_Idle;
    }

    public final void onRemove() {
        if (isRemoved) return;
        this.isRemoved = true;
        this.originCard.current_x = this.card.current_x;
        this.originCard.current_y = this.card.current_y;
        this.originCard.drawScale = this.card.drawScale;
        if (cardGroupReturnAfterEvoke != null) {
            switch (cardGroupReturnAfterEvoke.type) {
                case DRAW_PILE:
                    cardHolder.moveToDeck(originCard, true);
                    break;
                case HAND:
                    cardHolder.moveToHand(originCard);
                    break;
                case EXHAUST_PILE:
                    AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
                    cardHolder.moveToExhaustPile(originCard);
                    break;
                case DISCARD_PILE:
                default:
                    cardHolder.moveToDiscardPile(originCard);
                    break;
            }
        }
        else
            AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
        onRemoveCard();
    }

    @Override
    public void onEvoke() {
    }

    protected abstract void onRemoveCard();

    public CardOrb setDesc(String description) {
        this.description = description;
        this.card.rawDescription = description;
        this.card.initializeDescription();
        return this;
    }

    public abstract void checkShouldRemove();

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (originCard.drawScale != originCard.targetDrawScale) {
            originCard.render(spriteBatch);
            return;
        }

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
    }

    public CardOrb setNotEvokeOnEndOfTurn() {
        this.evokeOnEndOfTurn = false;
        return this;
    }

    @Override
    public void onEndOfTurn() {
        if (!evokeOnEndOfTurn) return;
//        InBattleDataManager.getHangUpCardOrbGroup().ifPresent(group -> group.evokeOrb(this));
        shouldRemove = true;
    }

//    public abstract void forceAcceptAction(AbstractCard card);

    @Override
    public void update() {
        if (originCard.drawScale != originCard.targetDrawScale) {
            originCard.target_x = this.cX;
            originCard.target_y = this.cY + YOffsetWhenHovered();
            originCard.targetDrawScale = DRAW_SCALE_SMALL;
            originCard.current_y += YOffsetBoBing();
            originCard.update();
        }

        this.hb.update();
        this.card.update();
        this.card.updateHoverLogic();
    }

    protected boolean checkShouldStopMoving() {
        return Math.abs(this.card.current_y - this.card.target_y) < 0.01f && Math.abs(this.card.current_x - this.card.target_x) < 0.01f;
    }

    protected void updateAnimationIdle() {
        if (isCardHovered()) {
            this.card.target_x = this.cX;
            this.card.target_y = this.cY + YOffsetWhenHovered();
            card.targetDrawScale = DRAW_SCALE_BIG;
            this.drawOrder = DrawOrder.top;
        }
        else {
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


    @Override
    public void applyFocus() {
    }

    protected void showEvokeNum() {
        card.costForTurn = evokeAmount;
        card.isCostModified = true;
        card.beginGlowing();
    }

    protected void showPassiveNum() {
        card.stopGlowing();
        card.costForTurn = passiveAmount;
        card.isCostModified = false;
    }

    public CardOrb setTargetType(AbstractCard.CardTarget cardTarget) {
        this.targetType = cardTarget;
        return this;
    }

    public AbstractCard getOriginCard() {
        return originCard;
    }

    public enum DrawOrder {
        bottom,
        middle,
        top
    }

}
