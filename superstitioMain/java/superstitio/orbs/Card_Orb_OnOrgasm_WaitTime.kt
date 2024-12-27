package superstitio.orbs

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import superstitio.powers.SexualHeat
import superstitioapi.hangUpCard.CardOrb
import superstitioapi.hangUpCard.ICardOrb_WaitTime
import superstitioapi.utils.CostSmart
import java.util.function.Consumer

class Card_Orb_OnOrgasm_WaitTime(
    card: AbstractCard, cardGroupReturnAfterEvoke: CardGroup?, OrbCounter: CostSmart,
    action_thisCard: Consumer<Card_Orb_OnOrgasm>
) : Card_Orb_OnOrgasm(card, cardGroupReturnAfterEvoke, OrbCounter, action_thisCard), ICardOrb_WaitTime
{
    override fun makeCopy(): CardOrb
    {
        return Card_Orb_OnOrgasm_WaitTime(fakeCard, cardGroupReturnAfterEvoke, orbCounter, action)
    }

    override fun onOrgasm(SexualHeatPower: SexualHeat)
    {
        orbCounter--
        if (orbCounter.isZero())
            tryAcceptAction()
    }

    override fun forceAcceptAction(card: AbstractCard)
    {
        orbCounter--
        if (orbCounter.isZero())
            tryAcceptAction()
    }
}
