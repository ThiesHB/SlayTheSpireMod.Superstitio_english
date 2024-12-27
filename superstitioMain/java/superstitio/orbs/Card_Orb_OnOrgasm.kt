package superstitio.orbs

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import superstitio.powers.SexualHeat
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm
import superstitioapi.hangUpCard.CardOrb
import superstitioapi.hangUpCard.ICardOrb_CanEvokeOnEndOfTurn
import superstitioapi.utils.CostSmart
import java.util.function.Consumer

abstract class Card_Orb_OnOrgasm(
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    OrbCounter: CostSmart,
    protected val action: Consumer<Card_Orb_OnOrgasm>
) : CardOrb(card, cardGroupReturnAfterEvoke, OrbCounter), OnOrgasm_onOrgasm,
    ICardOrb_CanEvokeOnEndOfTurn<Card_Orb_OnOrgasm>
{
    override var evokeOnEndOfTurn: Boolean = false

    protected fun tryAcceptAction()
    {
        tryCheckZeroAndAcceptAction { action.accept(this) }
    }

    override fun onEndOfTurn()
    {
        if (!evokeOnEndOfTurn) return
        setShouldRemove()
    }

    abstract override fun onOrgasm(SexualHeatPower: SexualHeat)

    abstract override fun forceAcceptAction(card: AbstractCard)

    override fun onRemoveCard()
    {
    }
}
