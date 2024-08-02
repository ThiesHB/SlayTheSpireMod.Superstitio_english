package superstitio.orbs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import superstitio.powers.SexualHeat;
import superstitioapi.hangUpCard.ICardOrb_WaitTime;

import java.util.function.Consumer;

public class CardOrb_OnOrgasm_WaitTime extends CardOrb_OnOrgasm implements ICardOrb_WaitTime {

    public CardOrb_OnOrgasm_WaitTime(AbstractCard card, CardGroup cardGroupReturnAfterEvoke, int OrbCounter,
                                     Consumer<CardOrb_OnOrgasm> action_thisCard) {
        super(card, cardGroupReturnAfterEvoke, OrbCounter, action_thisCard);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new CardOrb_OnOrgasm_WaitTime(card, cardGroupReturnAfterEvoke, OrbCounter, action);
    }

    @Override
    public void onOrgasm(SexualHeat SexualHeatPower) {
        OrbCounter--;
        if (OrbCounter < 0) return;
        if (OrbCounter == 0)
            actionAccept();
    }

    @Override
    public void forceAcceptAction(AbstractCard card) {
        OrbCounter--;
        if (OrbCounter < 0) return;
        if (OrbCounter == 0)
            actionAccept();
    }
}
