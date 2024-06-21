package superstitioapi.hangUpCard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import superstitioapi.actions.AutoDoneInstantAction;

import java.util.function.Consumer;


public class CardOrb_AtStartOfTurn extends CardOrb implements ICardOrb_EachTime {

    public final Consumer<CardOrb_AtStartOfTurn> action;

    public CardOrb_AtStartOfTurn(AbstractCard card, CardGroup cardGroupReturnAfterEvoke, int OrbCounter, Consumer<CardOrb_AtStartOfTurn> action) {
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
        return new CardOrb_AtStartOfTurn(getOriginCard(), cardGroupReturnAfterEvoke, OrbCounter, action);
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
