package superstitio.orbs

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.orbs.AbstractOrb
import superstitio.powers.SexualHeat
import superstitioapi.hangUpCard.ICardOrb_WaitTime
import superstitioapi.utils.CostSmart
import java.util.function.Consumer

class CardOrb_OnOrgasm_WaitTime(
    card: AbstractCard, cardGroupReturnAfterEvoke: CardGroup?, OrbCounter: CostSmart,
    action_thisCard: Consumer<CardOrb_OnOrgasm>
) : CardOrb_OnOrgasm(card, cardGroupReturnAfterEvoke, OrbCounter, action_thisCard), ICardOrb_WaitTime
{
    override fun makeCopy(): AbstractOrb
    {
        return CardOrb_OnOrgasm_WaitTime(fakeCard, cardGroupReturnAfterEvoke, orbCounter, action)
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
