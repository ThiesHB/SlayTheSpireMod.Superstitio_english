package superstitioapi.hangUpCard;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import org.apache.logging.log4j.util.BiConsumer;
import superstitioapi.actions.AutoDoneInstantAction;

import java.util.function.Predicate;


public abstract class CardOrb_CardTrigger extends CardOrb {

    public final BiConsumer<CardOrb_CardTrigger, AbstractCard> action;
    public Predicate<AbstractCard> cardMatcher = (card) -> true;
    public boolean evokeOnEndOfTurn;

    public CardOrb_CardTrigger(AbstractCard card, CardGroup cardGroupReturnAfterEvoke, int OrbCounter, BiConsumer<CardOrb_CardTrigger, AbstractCard> action_thisCard_targetCard) {
        super(card, cardGroupReturnAfterEvoke, OrbCounter);
        this.action = action_thisCard_targetCard;
        this.evokeOnEndOfTurn = true;
    }

    @Override
    protected void onRemoveCard() {
    }

    @Override
    public void update() {
        super.update();
        setEvokeAmount(OrbCounter - 1);
        setPassiveAmount(OrbCounter);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
    }

    public final void onCardUsed(AbstractCard card) {
        if (card == null) return;
        if (!TestIfCardIsRight_use(card)) return;
        if (OrbCounter <= 0) return;
        this.card.calculateCardDamage(null);
        OrbCounter--;
        if (onProperCardUsed_checkIfShouldApplyAction(card))
            actionAccept(card);
    }

    protected abstract boolean onProperCardUsed_checkIfShouldApplyAction(AbstractCard card);

    @Override
    public void forceAcceptAction(AbstractCard card) {
        OrbCounter--;
        if (onProperCardUsed_checkIfShouldApplyAction(card))
            actionAccept(card);
    }

    protected void actionAccept(AbstractCard card) {
        if (OrbCounter < 0) return;
        AutoDoneInstantAction.addToBotAbstract(() -> action.accept(this, card));
    }

    private boolean TestIfCardIsRight_use(AbstractCard hoveredCard) {
        if (hoveredCard == null) return false;
        if (cardMatcher == null) return false;
        if (hoveredCard instanceof Card_TriggerHangCardManually) {
            return false;
        }
        return this.cardMatcher.test(hoveredCard);
    }

    @Override
    protected boolean TestIfCardIsRight_hover(AbstractCard hoveredCard) {
        if (hoveredCard == null) return false;
        if (cardMatcher == null) return false;
        if (hoveredCard instanceof Card_TriggerHangCardManually) {
            return ((Card_TriggerHangCardManually) hoveredCard).forceFilterCardOrbToHoveredMode(this);
        }
        return this.cardMatcher.test(hoveredCard);
    }

    @SafeVarargs
    public final CardOrb_CardTrigger setCardPredicate(Predicate<AbstractCard>... cardMatchers) {
        for (Predicate<AbstractCard> mather : cardMatchers) {
            this.cardMatcher = this.cardMatcher.and(mather);
        }
        return this;
    }

    @Override
    public void onEndOfTurn() {
        if (!evokeOnEndOfTurn) return;
//        InBattleDataManager.getHangUpCardOrbGroup().ifPresent(group -> group.evokeOrb(this));
        shouldRemove = true;
    }

    public CardOrb_CardTrigger setNotEvokeOnEndOfTurn() {
        this.evokeOnEndOfTurn = false;
        return this;
    }
}
