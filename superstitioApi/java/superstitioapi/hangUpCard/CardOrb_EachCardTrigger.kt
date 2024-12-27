package superstitioapi.hangUpCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import superstitioapi.utils.CostSmart

class CardOrb_EachCardTrigger  //    private int triggerTime;
    (
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    triggerTime: CostSmart,
    action_thisOrb_triggerCard: (CardOrb_CardTrigger, AbstractCard) -> Unit
) : CardOrb_CardTrigger(card, cardGroupReturnAfterEvoke, triggerTime, action_thisOrb_triggerCard), ICardOrb_EachTime
{
    override fun makeCopy(): CardOrb
    {
        return CardOrb_EachCardTrigger(originCard, cardGroupReturnAfterEvoke, orbCounter, action)
    }

    override fun onProperCardUsed_checkIfShouldApplyAction(card: AbstractCard?): Boolean
    {
        return true
    }
}
