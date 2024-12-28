package superstitio.orbs

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import superstitio.powers.SexualHeat
import superstitioapi.hangUpCard.CardOrb
import superstitioapi.hangUpCard.ICardOrb_EachTime
import superstitioapi.utils.CostSmart
import java.util.function.Consumer

class Card_Orb_OnOrgasm_EachTime(
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    OrbCounter: CostSmart,
    action_thisCard: Consumer<Card_Orb_OnOrgasm>
) : Card_Orb_OnOrgasm(card, cardGroupReturnAfterEvoke, OrbCounter, action_thisCard), ICardOrb_EachTime
{
    override fun makeCopy(): CardOrb
    {
        return Card_Orb_OnOrgasm_EachTime(fakeCard, cardGroupReturnAfterEvoke, orbCounter, action)
    }

    override fun onOrgasm(SexualHeatPower: SexualHeat)
    {
        orbCounter -= 1
        tryAcceptAction()
    }

    override fun forceAcceptAction(card: AbstractCard)
    {
        orbCounter -= 1
        tryAcceptAction()
    }
}
