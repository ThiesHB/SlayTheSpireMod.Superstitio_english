package superstitio.orbs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import superstitio.powers.SexualHeat;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.hangUpCard.CardOrb;

import java.util.function.Consumer;

public abstract class CardOrb_OnOrgasm extends CardOrb implements OnOrgasm_onOrgasm {
    protected final Consumer<CardOrb_OnOrgasm> action;
    public boolean evokeOnEndOfTurn;

    public CardOrb_OnOrgasm(AbstractCard card, CardGroup cardGroupReturnAfterEvoke, int OrbCounter, Consumer<CardOrb_OnOrgasm> action_thisCard) {
        super(card, cardGroupReturnAfterEvoke, OrbCounter);
        this.action = action_thisCard;
        this.evokeOnEndOfTurn = false;
    }

    public CardOrb_OnOrgasm setDiscardOnEndOfTurn() {
        this.evokeOnEndOfTurn = true;
        this.setTriggerDiscardIfMoveToDiscard();
        return this;
    }

    protected void actionAccept() {
        AutoDoneInstantAction.addToBotAbstract(() -> action.accept(this));
    }

    @Override
    public void onEndOfTurn() {
        if (!evokeOnEndOfTurn) return;
//        InBattleDataManager.getHangUpCardOrbGroup().ifPresent(group -> group.evokeOrb(this));
        setShouldRemove();
    }

    @Override
    public abstract void onOrgasm(SexualHeat SexualHeatPower);

    @Override
    public abstract void forceAcceptAction(AbstractCard card);

    @Override
    protected void onRemoveCard() {
    }
}
