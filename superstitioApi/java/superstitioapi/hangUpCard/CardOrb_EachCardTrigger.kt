package superstitioapi.hangUpCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.orbs.AbstractOrb
import org.apache.logging.log4j.util.BiConsumer
import superstitioapi.utils.CardUtility

class CardOrb_EachCardTrigger  //    private int triggerTime;
    (
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    triggerTime: CardUtility.CostSmart,
    action_thisOrb_triggerCard: BiConsumer<CardOrb_CardTrigger, AbstractCard>
) : CardOrb_CardTrigger(card, cardGroupReturnAfterEvoke, triggerTime, action_thisOrb_triggerCard), ICardOrb_EachTime {
    override fun makeCopy(): AbstractOrb {
        return CardOrb_EachCardTrigger(originCard, cardGroupReturnAfterEvoke, orbCounter, action)
    }

    override fun onProperCardUsed_checkIfShouldApplyAction(card: AbstractCard?): Boolean {
        return true
    }
}
