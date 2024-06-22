package superstitioapi.hangUpCard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import org.apache.logging.log4j.util.BiConsumer;

public class CardOrb_EachCardTrigger extends CardOrb_CardTrigger implements ICardOrb_EachTime {
//    private int triggerTime;

    public CardOrb_EachCardTrigger(AbstractCard card, CardGroup cardGroupReturnAfterEvoke, int triggerTime, BiConsumer<CardOrb_CardTrigger,
            AbstractCard> action_thisOrb_triggerCard) {
        super(card, cardGroupReturnAfterEvoke, triggerTime, action_thisOrb_triggerCard);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new CardOrb_EachCardTrigger(getOriginCard(), cardGroupReturnAfterEvoke, OrbCounter, action);
    }

    @Override
    protected boolean onProperCardUsed_checkIfShouldApplyAction(AbstractCard card) {
        return true;
    }
}
