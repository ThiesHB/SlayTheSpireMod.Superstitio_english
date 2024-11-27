package superstitioapi.hangUpCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.orbs.AbstractOrb
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.ActionUtility.VoidSupplier
import superstitioapi.utils.CardUtility
import java.util.function.Consumer

class CardOrb_AtEndOfTurnEachTime(
    card: AbstractCard, cardGroupReturnAfterEvoke: CardGroup?, OrbCounter: CardUtility.CostSmart,
    val action: Consumer<CardOrb_AtEndOfTurnEachTime>
) : CardOrb(card, cardGroupReturnAfterEvoke, OrbCounter), ICardOrb_EachTime {
    protected fun actionAccept() {
        AutoDoneInstantAction.addToBotAbstract(VoidSupplier { action.accept(this) })
    }

    override fun onEndOfTurn() {
        orbCounter--
        if (orbCounter < 0) return
        actionAccept()
    }

    override fun makeCopy(): AbstractOrb {
        return CardOrb_AtEndOfTurnEachTime(originCard, cardGroupReturnAfterEvoke, orbCounter, action)
    }

    override fun forceAcceptAction(card: AbstractCard) {
        orbCounter--
        if (orbCounter < 0) return
        actionAccept()
    }

    override fun onRemoveCard() {
    }
}
