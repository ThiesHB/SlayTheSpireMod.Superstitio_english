package superstitioapi.hangUpCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.orbs.AbstractOrb
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.ActionUtility.VoidSupplier
import java.util.function.Consumer

class CardOrb_AtStartOfTurnEachTime(
    card: AbstractCard, cardGroupReturnAfterEvoke: CardGroup?, OrbCounter: Int,
    val action: Consumer<CardOrb_AtStartOfTurnEachTime>
) : CardOrb(card, cardGroupReturnAfterEvoke, OrbCounter), ICardOrb_EachTime {
    protected fun actionAccept() {
        AutoDoneInstantAction.addToBotAbstract(VoidSupplier { action.accept(this) })
    }

    override fun onStartOfTurn() {
        orbCounter--
        if (orbCounter < 0) return
        actionAccept()
    }

    override fun makeCopy(): AbstractOrb {
        return CardOrb_AtStartOfTurnEachTime(originCard, cardGroupReturnAfterEvoke, orbCounter, action)
    }

    override fun forceAcceptAction(card: AbstractCard) {
        orbCounter--
        if (orbCounter < 0) return
        actionAccept()
    }

    override fun onRemoveCard() {
    }
}
