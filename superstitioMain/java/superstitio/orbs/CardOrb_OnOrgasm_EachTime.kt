package superstitio.orbs

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.orbs.AbstractOrb
import superstitio.powers.SexualHeat
import superstitioapi.hangUpCard.ICardOrb_EachTime
import superstitioapi.utils.CardUtility
import java.util.function.Consumer

class CardOrb_OnOrgasm_EachTime(
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    OrbCounter: CardUtility.CostSmart,
    action_thisCard: Consumer<CardOrb_OnOrgasm>
) : CardOrb_OnOrgasm(card, cardGroupReturnAfterEvoke, OrbCounter, action_thisCard), ICardOrb_EachTime
{
    override fun makeCopy(): AbstractOrb
    {
        return CardOrb_OnOrgasm_EachTime(fakeCard, cardGroupReturnAfterEvoke, orbCounter, action)
    }

    override fun onOrgasm(SexualHeatPower: SexualHeat)
    {
        orbCounter--
        if (orbCounter < 0) return
        actionAccept()
    }

    override fun forceAcceptAction(card: AbstractCard)
    {
        orbCounter--
        if (orbCounter < 0) return
        actionAccept()
    }
}
