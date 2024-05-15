package superstitio.orbs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import org.apache.logging.log4j.util.BiConsumer;

public class CardOrb_EachCardTrigger extends CardOrb_CardTrigger {
//    private int triggerTime;

    public CardOrb_EachCardTrigger(AbstractCard card, CardGroup cardGroupReturnAfterEvoke, BiConsumer<CardOrb_CardTrigger, AbstractCard> action_thisCard_targetCard, int triggerTime) {
        super(card, cardGroupReturnAfterEvoke, action_thisCard_targetCard);
        this.OrbCounter = triggerTime;
    }


    @Override
    public AbstractOrb makeCopy() {
        return new CardOrb_EachCardTrigger(getOriginCard(), cardGroupReturnAfterEvoke, action, OrbCounter);
    }

    @Override
    protected boolean onProperCardUsed_checkIfShouldApplyAction(AbstractCard card) {
        return true;
    }
}
