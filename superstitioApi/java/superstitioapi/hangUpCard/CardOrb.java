package superstitioapi.hangUpCard;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import superstitioapi.DataUtility;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.utils.CardUtility;
import superstitioapi.utils.CreatureUtility;

import java.util.Optional;
import java.util.function.Consumer;

import static superstitioapi.utils.ActionUtility.FunctionReturnSelfType;
import static superstitioapi.utils.ActionUtility.VoidSupplier;

public abstract class CardOrb extends AbstractOrb {
    public static final String ORB_ID = DataUtility.MakeTextID(CardOrb.class);
    public static final float ANIMATION_Y_SCALE = 1.0f;
    public static final float DRAW_SCALE_BIG = 0.9f;

    public static final float DRAW_SCALE_MIDDLE = 0.5f;
    public static final float DRAW_SCALE_SMALL = 0.25f;
    protected static final float DRAW_SCALE_SMALL_BIGGER = 0.30f;
    private static final float TIMER_ANIMATION = 2.0f;
    public final CardGroup thisCardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public final HangOnTarget targetTypeOrigin;
    public final HangEffectType actionTypeOrigin;
    public final CardGroup cardHolder = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    protected final AbstractCard originCard;
    public CardGroup cardGroupReturnAfterEvoke = null;
    public HangOnTarget targetType = HangOnTarget.None;
    public HangEffectType actionType = HangEffectType.None;
    public FunctionReturnSelfType movingType;
    public AbstractCreature lastTarget;
    public int OrbCounter;
    protected AbstractCard card;
    protected boolean isNewMovingModeSetup;
    private Consumer<CardOrb> afterEvokeConsumer;
    private boolean shouldRemove;
    private boolean isRemoved;
    //    public abstract void forceAcceptAction(AbstractCard card);
    private boolean stopShowOriginCard = false;

    public CardOrb(AbstractCard card, CardGroup cardGroupReturnAfterEvoke, int OrbCounter) {
        this.ID = ORB_ID;
        this.name = "";
        this.OrbCounter = OrbCounter;
        this.basePassiveAmount = 0;
        this.passiveAmount = 0;
        this.baseEvokeAmount = 0;
        this.evokeAmount = 0;
        this.description = "";
        this.updateDescription();

        this.cardGroupReturnAfterEvoke = cardGroupReturnAfterEvoke;
        this.cardHolder.addToTop(card);
        this.originCard = card;
        this.originCard.targetDrawScale = DRAW_SCALE_SMALL;

        setUpShownCard(card.makeStatEquivalentCopy()); //什么替身文学，绷不住了

        setHoverTypeFromCard(this.card.target);
        targetTypeOrigin = targetType;
        actionTypeOrigin = actionType;

        this.thisCardGroup.addToTop(this.card);
        this.movingType = State_Idle();


    }

    public static AbstractMonster getHoveredMonsterSafe() {
        return CreatureUtility.getMonsterOrRandomMonster(ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "hoveredMonster"));
    }

    public static Optional<AbstractMonster> getHoveredMonster() {
        AbstractMonster hoveredMonster = ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "hoveredMonster");
        if (hoveredMonster == null) return Optional.empty();
        return Optional.of(hoveredMonster);
    }

    public CardOrb setTriggerDiscardIfMoveToDiscard() {
        this.setAfterEvokeConsumer(orb -> {
            if (orb.OrbCounter <= 0) return;
            if (orb.cardGroupReturnAfterEvoke != AbstractDungeon.player.discardPile) return;
            orb.getOriginCard().triggerOnManualDiscard();
            GameActionManager.incrementDiscard(false);
        });
        return this;
    }

    public void addToBot_HangCard() {
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(this);
        AutoDoneInstantAction.addToBotAbstract(AbstractDungeon::onModifyPower);
    }

    /**
     * 这个函数是被addToBot调用的
     */
    public void onRemove() {
        if (isRemoved) return;
        this.isRemoved = true;
        if (cardGroupReturnAfterEvoke != null && cardHolder.contains(originCard)) {
            this.originCard.current_x = this.card.current_x;
            this.originCard.current_y = this.card.current_y;
            this.originCard.drawScale = this.card.drawScale;
            switch (cardGroupReturnAfterEvoke.type) {
                case DRAW_PILE:
                    cardHolder.moveToDeck(originCard, true);
                    break;
                case HAND:
                    CardUtility.moveToHandOrDiscardWhenMaxHand(cardHolder, originCard);
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
        } else AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
        onRemoveCard();
        if (afterEvokeConsumer != null)
            afterEvokeConsumer.accept(this);
    }

    public CardOrb setDesc(String description) {
        this.description = description;
        this.card.rawDescription = description;
        this.card.initializeDescription();
        return this;
    }

    public void checkShouldRemove() {
        if (shouldRemove) return;
        this.shouldRemove = this.OrbCounter <= 0 && checkShouldStopMoving();
    }

    public void tryMoveTo(Vector2 vector2) {
        this.card.target_x = vector2.x;
        this.card.target_y = vector2.y;
    }
//
//    public static void renderCardPreview(){
//        float tmpScale = this.drawScale * 0.8F;
//        if (this.current_x > (float) Settings.WIDTH * 0.75F) {
//            this.cardsToPreview.current_x = this.current_x + (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * this.drawScale;
//        } else {
//            this.cardsToPreview.current_x = this.current_x - (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * this.drawScale;
//        }
//
//        this.cardsToPreview.current_y = this.current_y + (IMG_HEIGHT / 2.0F - IMG_HEIGHT / 2.0F * 0.8F) * this.drawScale;
//        this.cardsToPreview.drawScale = tmpScale;
//        this.cardsToPreview.render(sb);
//    }

    public CardOrb setShowCard(AbstractCard showCard) {
        setUpShownCard(showCard);
        return this;
    }

    public CardOrb setTargetType(AbstractCard.CardTarget cardTarget) {
        setHoverTypeFromCard(cardTarget);
        return this;
    }

    public AbstractCard getOriginCard() {
        return originCard;
    }

    public void StartHitCreature(AbstractCreature target) {
        this.movingType = this::State_Moving;
        AbstractCreature creature = CreatureUtility.getTargetOrRandomMonster(target);
        this.tryMoveTo(new Vector2(this.cX - (this.cX - creature.hb.cX) / 1.2f, this.cY - (this.cY - creature.hb.cY) / 1.2f));
        this.card.superFlash(CardUtility.getColorFormCard(card));
    }

    public abstract void forceAcceptAction(AbstractCard card);

    public void setShouldRemove() {
        this.shouldRemove = true;
    }

    public boolean ifShouldRemove() {
        return this.shouldRemove;
    }

    public CardOrb setAfterEvokeConsumer(Consumer<CardOrb> afterEvokeConsumer) {
        this.afterEvokeConsumer = afterEvokeConsumer;
        return this;
    }

    protected void setHoverTypeFromCard(AbstractCard.CardTarget cardTarget) {
        switch (cardTarget) {
            case ENEMY:
            case ALL_ENEMY:
                targetType = HangOnTarget.Enemy;
                actionType = HangEffectType.Bad;
                break;
            case SELF:
                targetType = HangOnTarget.Self;
                actionType = HangEffectType.Good;
                break;
            case NONE:
            case ALL:
            case SELF_AND_ENEMY:
            default:
                targetType = HangOnTarget.None;
                actionType = HangEffectType.Special;
                break;
        }
    }

    protected VoidSupplier checkAndSetTheHoverType() {
        switch (targetType) {
            case Enemy:
                return this::State_WhenHoverCard_OnMonster;
            case Self:
                return this::State_WhenHoverCard_OnSelf;
            case None:
            default:
                return this::State_WhenHoverCard_OnNothing;
        }
    }

    protected abstract void onRemoveCard();

    protected boolean checkShouldStopMoving() {
        return Math.abs(this.card.current_y - this.card.target_y) < 0.01f && Math.abs(this.card.current_x - this.card.target_x) < 0.01f;
    }

    protected void updateAnimationIdle() {
        if (isCardHovered()) {
            this.card.target_x = this.cX;
            this.card.target_y = this.cY + YOffsetWhenHovered();
            card.targetDrawScale = DRAW_SCALE_BIG;
//            this.drawOrder = DrawOrder.top;
        } else {
            this.card.target_x = this.cX;
            this.card.target_y = this.cY + YOffsetWhenHovered();
            card.targetDrawScale = DRAW_SCALE_SMALL;
//            this.drawOrder = DrawOrder.bottom;
        }
    }

    protected void setEvokeAmount(int amount) {
        evokeAmount = amount;
        baseEvokeAmount = amount;
    }

    protected void setPassiveAmount(int amount) {
        passiveAmount = amount;
        basePassiveAmount = amount;
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

    protected void showEvokeNum() {
        evokeAmount = Math.max(0, evokeAmount);
        card.costForTurn = evokeAmount;
        card.isCostModified = true;
        card.beginGlowing();
    }

    protected void showPassiveNum() {
        passiveAmount = Math.max(0, passiveAmount);
        card.stopGlowing();
        card.costForTurn = passiveAmount;
        card.isCostModified = false;
    }

    protected FunctionReturnSelfType State_Moving() {
        showPassiveNum();
        this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
        if (checkShouldStopMoving()) {
            return this::State_Idle;
        }
        return this::State_Moving;
    }

    protected void State_WhenHoverCard_OnMonster() {
        updateIfFocusOnMonster();
    }

    protected void State_WhenHoverCard_OnSelf() {
        updateIfFocusOnSelf();
    }

    protected void State_WhenHoverCard_OnNothing() {
        updateIfFocusOnNothing();
    }

    protected void updateIfFocusOnMonster() {
        AbstractCreature target = CardOrb.getHoveredMonster().orElse(null);
        if (!CreatureUtility.isAlive(target)) return;
        this.lastTarget = target;
        this.tryMoveTo(new Vector2(this.cX - (this.cX - target.hb.cX) / 3, this.cY - (this.cY - target.hb.cY) / 3 + YOffsetWhenHovered()));
    }

    protected void updateIfFocusOnSelf() {
        if (!CreatureUtility.isAlive(AbstractDungeon.player)) return;
        this.lastTarget = AbstractDungeon.player;
        this.tryMoveTo(new Vector2(this.cX - (this.cX - AbstractDungeon.player.hb.cX) / 2.5f,
                this.cY - (this.cY - AbstractDungeon.player.hb.cY) / 2.5f + YOffsetWhenHovered()));
    }

    protected void updateIfFocusOnNothing() {
        this.tryMoveTo(new Vector2(this.cX, this.cY + YOffsetWhenHovered() + YOffsetWhenHovered()));
    }

    protected FunctionReturnSelfType State_Idle() {
        this.targetType = targetTypeOrigin;
        this.actionType = actionTypeOrigin;
        showPassiveNum();
        updateAnimationIdle();
        if (ifHoveredRightCard()) {
            return this::State_WhenHoverCard;
        }
        return this::State_Idle;
    }

    protected FunctionReturnSelfType State_WhenHoverCard() {
        showEvokeNum();
        AbstractCard hoveredCard = AbstractDungeon.player.hoveredCard;
        if (hoveredCard instanceof Card_TriggerHangCardManually)
            this.card.costForTurn = ((Card_TriggerHangCardManually) hoveredCard).forceChangeOrbCounterShown(this);
        this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
        checkAndSetTheHoverType().get();
        if (!ifHoveredRightCard()) {
            return this::State_Idle;
        }
        return this::State_WhenHoverCard;
    }

    protected boolean ifHoveredRightCard() {
        AbstractCard hoveredCard = AbstractDungeon.player.hoveredCard;
        if (hoveredCard == null) return false;
        if (!((boolean) ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "isHoveringCard"))) {
            return false;
        }
        return TestIfCardIsRight_hover(hoveredCard);
    }

    protected boolean TestIfCardIsRight_hover(AbstractCard hoveredCard) {
        if (hoveredCard == null) return false;
        if (hoveredCard instanceof Card_TriggerHangCardManually) {
            return ((Card_TriggerHangCardManually) hoveredCard).forceFilterCardOrbToHoveredMode(this);
        }
        return false;
    }

    private void setUpShownCard(AbstractCard card) {
        this.card = card;
        this.card.drawScale = card.drawScale;
        this.card.transparency = 1.0f;
        this.card.current_x = card.current_x;
        this.card.current_y = card.current_y;

        this.card.targetDrawScale = DRAW_SCALE_SMALL;
        this.card.isCostModified = false;

        this.card.costForTurn = -2;
    }

    @Override
    public void onStartOfTurn() {
    }

    @Override
    public void onEvoke() {
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (!stopShowOriginCard) {
            if (originCard.drawScale != originCard.targetDrawScale) {
                originCard.render(spriteBatch);
                return;
            } else {
                stopShowOriginCard = true;
            }
        }

        float offset = YOffsetBoBing();
        card.current_y += offset;
        card.render(spriteBatch);
        if (card.hb.hovered) {
            card.renderCardTip(spriteBatch);
        }
        card.current_y -= offset;
    }

    @Override
    public void update() {
        if (!stopShowOriginCard) {
            if (originCard.drawScale != originCard.targetDrawScale) {
                originCard.target_x = this.cX;
                originCard.target_y = this.cY + YOffsetWhenHovered();
                originCard.targetDrawScale = DRAW_SCALE_SMALL;
                originCard.current_y += YOffsetBoBing();
                originCard.update();
            } else {
                stopShowOriginCard = true;
            }
        }

        this.hb.update();
        this.card.update();
        this.card.updateHoverLogic();
        this.movingType = this.movingType.get();
        this.card.glowColor = actionType.color;
        setEvokeAmount(OrbCounter - 1);
        setPassiveAmount(OrbCounter);
    }

    @Override
    public void updateDescription() {
        if (this.card != null) {
            this.card.applyPowers();
            this.card.calculateDamageDisplay(CardOrb.getHoveredMonsterSafe());
            this.card.initializeDescription();

            if (this.card.cardsToPreview != null) {
                this.card.cardsToPreview.applyPowers();
                this.card.cardsToPreview.initializeDescription();
                this.card.cardsToPreview.calculateDamageDisplay(CardOrb.getHoveredMonsterSafe());
            }
            if (this.originCard != null) {
                this.originCard.applyPowers();
                this.originCard.calculateDamageDisplay(CardOrb.getHoveredMonsterSafe());
                this.originCard.initializeDescription();
            }
        }
    }

    @Override
    public void playChannelSFX() {

    }

    @Override
    public void applyFocus() {
    }

    public enum HangEffectType {
        Good(Color.GREEN), Bad(Color.RED), Special(Color.GOLD), None(new Color(Color.BLUE.r, Color.BLUE.g, Color.BLUE.b, 0));
        public final Color color;

        HangEffectType(Color color) {
            this.color = color.cpy();
        }
    }

    public enum HangOnTarget {
        Enemy, Self, None
    }
}
