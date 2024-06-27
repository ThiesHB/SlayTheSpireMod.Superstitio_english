package superstitioapi.hangUpCard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import superstitioapi.actions.AutoDoneInstantAction;

import java.util.function.Consumer;


public class CardOrb_AtStartOfTurnEachTime extends CardOrb implements ICardOrb_EachTime {

    public final Consumer<CardOrb_AtStartOfTurnEachTime> action;

    public CardOrb_AtStartOfTurnEachTime(AbstractCard card, CardGroup cardGroupReturnAfterEvoke, int OrbCounter, Consumer<CardOrb_AtStartOfTurnEachTime> action) {
        super(card, cardGroupReturnAfterEvoke, OrbCounter);
        this.action = action;
    }

    @Override
    public void onStartOfTurn() {
        OrbCounter--;
        if (OrbCounter < 0) return;
        actionAccept();
    }

    @Override
    public AbstractOrb makeCopy() {
        return new CardOrb_AtStartOfTurnEachTime(getOriginCard(), cardGroupReturnAfterEvoke, OrbCounter, action);
    }

    protected void actionAccept() {
        AutoDoneInstantAction.addToBotAbstract(() -> action.accept(this));
    }

    @Override
    protected void onRemoveCard() {
    }

    @Override
    public void forceAcceptAction(AbstractCard card) {
        OrbCounter--;
        if (OrbCounter < 0) return;
        actionAccept();
    }
}
