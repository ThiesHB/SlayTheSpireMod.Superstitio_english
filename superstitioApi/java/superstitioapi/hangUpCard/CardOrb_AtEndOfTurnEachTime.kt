package superstitioapi.hangUpCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.orbs.AbstractOrb
import superstitioapi.utils.CostSmart

class CardOrb_AtEndOfTurnEachTime(
    card: AbstractCard, cardGroupReturnAfterEvoke: CardGroup?, OrbCounter: CostSmart,
    val action: (CardOrb_AtEndOfTurnEachTime) -> Unit
) : CardOrb(card, cardGroupReturnAfterEvoke, OrbCounter), ICardOrb_EachTime
{
    protected fun tryAcceptAction()
    {
        tryCheckZeroAndAcceptAction { action(this) }
    }

    override fun onEndOfTurn()
    {
        orbCounter--
        tryAcceptAction()
    }

    override fun makeCopy(): AbstractOrb
    {
        return CardOrb_AtEndOfTurnEachTime(originCard, cardGroupReturnAfterEvoke, orbCounter, action)
    }

    override fun forceAcceptAction(card: AbstractCard)
    {
        orbCounter--
        tryAcceptAction()
    }

    override fun onRemoveCard()
    {
    }
}
