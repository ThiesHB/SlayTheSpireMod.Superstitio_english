package superstitio.hangUpCard;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.util.BiConsumer;
import superstitio.cards.DamageActionMaker;
import superstitio.orbs.Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb;
import superstitio.utils.ActionUtility;
import superstitio.utils.ActionUtility.FunctionReturnSelfType;
import superstitio.utils.CardUtility;

import java.util.Optional;
import java.util.function.Predicate;


public abstract class CardOrb_CardTrigger extends CardOrb {

    protected final BiConsumer<CardOrb_CardTrigger, AbstractCard> action;
    public AbstractCreature lastTarget;
    public int OrbCounter = 0;
    public Predicate<AbstractCard> cardMatcher = (card) -> true;

    public CardOrb_CardTrigger(AbstractCard card, CardGroup cardGroupReturnAfterEvoke, BiConsumer<CardOrb_CardTrigger, AbstractCard> action_thisCard_targetCard) {
        super(card, cardGroupReturnAfterEvoke);
        this.action = action_thisCard_targetCard;
    }

    public static AbstractMonster getHoveredMonsterSafe() {
        return DamageActionMaker.getMonsterOrFirstMonster(ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "hoveredMonster"));
    }

    public static Optional<AbstractMonster> getHoveredMonster() {
        AbstractMonster hoveredMonster = ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "hoveredMonster");
        if (hoveredMonster == null)
            return Optional.empty();
        return Optional.of(hoveredMonster);
    }

    @Override
    public void onEndOfTurn() {
        super.onEndOfTurn();
    }

    @Override
    protected void onRemoveCard() {
    }

    @Override
    public void checkShouldRemove() {
        if (shouldRemove) return;
        this.shouldRemove = this.OrbCounter <= 0 && checkShouldStopMoving();
    }

    @Override
    public void update() {
        super.update();
        this.movingType = this.movingType.get();
        setEvokeAmount(OrbCounter - 1);
        setPassiveAmount(OrbCounter);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
    }

    protected ActionUtility.VoidSupplier checkAndSetTheHoverType() {
        switch (targetType) {
            case ENEMY:
            case ALL_ENEMY:
                return this::State_WhenHoverCard_OnMonster;
            case SELF:
                return this::State_WhenHoverCard_OnSelf;
            case NONE:
            case ALL:
            case SELF_AND_ENEMY:
            default:
                return this::State_WhenHoverCard_OnNothing;
        }
    }

    public final void onCardUsed(AbstractCard card) {
        if (card == null) return;
        if (!TestIfCardIsRight_use(card)) return;
        if (OrbCounter <= 0) return;
        this.card.calculateCardDamage(null);
//        this.originCard.calculateCardDamage(null);
//        this.originCard.initializeDescription();
//        this.originCard.update();
        OrbCounter--;
        if (onProperCardUsed_checkIfShouldApplyAction(card))
            actionAccept(card);
    }

    protected abstract boolean onProperCardUsed_checkIfShouldApplyAction(AbstractCard card);

    public void forceAcceptAction(AbstractCard card) {
        OrbCounter--;
        if (onProperCardUsed_checkIfShouldApplyAction(card))
            actionAccept(card);
    }

    protected void actionAccept(AbstractCard card) {
        if (OrbCounter < 0) return;
        action.accept(this, card);
    }

    protected FunctionReturnSelfType State_Idle() {
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
        if (hoveredCard instanceof Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb)
            this.card.costForTurn = ((Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb) hoveredCard).forceChangeOrbCounterShown(this);
        this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
        checkAndSetTheHoverType().get();
        this.drawOrder = DrawOrder.middle;
        if (!ifHoveredRightCard()) {
            return this::State_Idle;
        }
        return this::State_WhenHoverCard;
    }

    protected FunctionReturnSelfType State_Moving() {
        showPassiveNum();
        this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
        this.drawOrder = DrawOrder.top;
        if (checkShouldStopMoving()) {
            return this::State_Idle;
        }
        return this::State_Moving;
    }

    protected void State_WhenHoverCard_OnMonster() {
        updateIfFocusOnMonster();
        this.card.glowColor = CardUtility.getColorFormCard(targetType);
    }

    protected void State_WhenHoverCard_OnSelf() {
        updateIfFocusOnSelf();
        this.card.glowColor = CardUtility.getColorFormCard(targetType);
    }

    protected void State_WhenHoverCard_OnNothing() {
        updateIfFocusOnNothing();
    }

    public void StartHitCreature(AbstractCreature target) {
        this.movingType = this::State_Moving;
        AbstractCreature creature = DamageActionMaker.getTargetOrFirstMonster(target);
        this.tryMoveTo(new Vector2(this.cX - (this.cX - creature.hb.cX) / 1.2f, this.cY - (this.cY - creature.hb.cY) / 1.2f));
        this.card.superFlash(CardUtility.getColorFormCard(card));
    }

//    public void onPlayerHoveringHandCard(AbstractCard card) {
//        if (this.movingType == State_Moving()) return;
//        if (TestIfCardIsRight_hover(card)) {
//            this.movingType = this::State_WhenHoverCard;
//        }
//        else
//            this.movingType = this::State_Idle;
//    }

    protected boolean ifHoveredRightCard() {
        AbstractCard hoveredCard = AbstractDungeon.player.hoveredCard;
        if (!((boolean) ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "isHoveringCard")))
            return false;
        if (hoveredCard == null) return false;
        return TestIfCardIsRight_hover(hoveredCard);
    }

    private boolean TestIfCardIsRight_use(AbstractCard hoveredCard) {
        if (hoveredCard == null) return false;
        if (cardMatcher == null) return false;
        if (hoveredCard instanceof Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb) {
            return false;
        }
        return this.cardMatcher.test(hoveredCard);
    }

    private boolean TestIfCardIsRight_hover(AbstractCard hoveredCard) {
        if (hoveredCard == null) return false;
        if (cardMatcher == null) return false;
        if (hoveredCard instanceof Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb) {
            return ((Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb) hoveredCard).forceFilterCardOrbToHoveredMode(this);
        }
        return this.cardMatcher.test(hoveredCard);
    }

    protected void updateIfFocusOnMonster() {
        AbstractCreature target = CardOrb_CardTrigger.getHoveredMonster().orElse(null);
        if (!ActionUtility.isAlive(target)) return;
        this.lastTarget = target;
        this.tryMoveTo(new Vector2(this.cX - (this.cX - target.hb.cX) / 3, this.cY - (this.cY - target.hb.cY) / 3 + YOffsetWhenHovered()));
    }

    protected void updateIfFocusOnSelf() {
        if (!ActionUtility.isAlive(AbstractDungeon.player)) return;
        this.lastTarget = AbstractDungeon.player;
        this.tryMoveTo(new Vector2(this.cX - (this.cX - AbstractDungeon.player.hb.cX) / 2.5f,
                this.cY - (this.cY - AbstractDungeon.player.hb.cY) / 2.5f + YOffsetWhenHovered()));
    }

    protected void updateIfFocusOnNothing() {
        this.tryMoveTo(new Vector2(this.cX, this.cY + YOffsetWhenHovered() + YOffsetWhenHovered()));
    }

    @SafeVarargs
    public final CardOrb setCardPredicate(Predicate<AbstractCard>... cardMatchers) {
        for (Predicate<AbstractCard> mather : cardMatchers) {
            this.cardMatcher = this.cardMatcher.and(mather);
        }
        return this;
    }
}
