package superstitioapi.hangUpCard;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import org.apache.logging.log4j.util.BiConsumer;
import superstitioapi.utils.ActionUtility;

public class CardOrb_WaitCardTrigger extends CardOrb_CardTrigger implements ICardOrb_WaitTime {

    protected static final Color ReduceWaitTime = Color.GOLDENROD.cpy();

    public CardOrb_WaitCardTrigger(AbstractCard card, CardGroup cardGroupReturnAfterEvoke, int waitTime, BiConsumer<CardOrb_CardTrigger,
            AbstractCard> action_thisOrb_triggerCard) {
        super(card, cardGroupReturnAfterEvoke, waitTime, action_thisOrb_triggerCard);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new CardOrb_WaitCardTrigger(getOriginCard(), cardGroupReturnAfterEvoke, OrbCounter, action);
    }

    @Override
    protected ActionUtility.VoidSupplier checkAndSetTheHoverType() {
        if (this.OrbCounter > 1)
            return this::State_WhenHoverCard_JustGlow;
        return super.checkAndSetTheHoverType();
    }

    private void State_WhenHoverCard_JustGlow() {
        this.card.targetDrawScale = DRAW_SCALE_SMALL_BIGGER;
        this.card.glowColor = ReduceWaitTime;
    }

    @Override
    protected boolean onProperCardUsed_checkIfShouldApplyAction(AbstractCard card) {
        return OrbCounter <= 0;
    }
}
