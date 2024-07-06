package superstitioapi.hangUpCard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import superstitioapi.actions.AutoDoneInstantAction;

import java.util.function.Consumer;


public class CardOrb_AtEndOfTurnEachTime extends CardOrb implements ICardOrb_EachTime {

    public final Consumer<CardOrb_AtEndOfTurnEachTime> action;

    public CardOrb_AtEndOfTurnEachTime(AbstractCard card, CardGroup cardGroupReturnAfterEvoke, int OrbCounter,
                                       Consumer<CardOrb_AtEndOfTurnEachTime> action) {
        super(card, cardGroupReturnAfterEvoke, OrbCounter);
        this.action = action;
    }

    protected void actionAccept() {
        AutoDoneInstantAction.addToBotAbstract(() -> action.accept(this));
    }

    @Override
    public void onEndOfTurn() {
        OrbCounter--;
        if (OrbCounter < 0) return;
        actionAccept();
    }

    @Override
    public AbstractOrb makeCopy() {
        return new CardOrb_AtEndOfTurnEachTime(getOriginCard(), cardGroupReturnAfterEvoke, OrbCounter, action);
    }

    @Override
    public void forceAcceptAction(AbstractCard card) {
        OrbCounter--;
        if (OrbCounter < 0) return;
        actionAccept();
    }

    @Override
    protected void onRemoveCard() {
    }
}
