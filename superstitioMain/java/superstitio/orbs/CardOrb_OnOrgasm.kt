package superstitio.orbs

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import superstitio.powers.SexualHeat
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.hangUpCard.CardOrb
import superstitioapi.utils.CardUtility
import java.util.function.Consumer

abstract class CardOrb_OnOrgasm(
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    OrbCounter: CardUtility.CostSmart,
    protected val action: Consumer<CardOrb_OnOrgasm>
) : CardOrb(card, cardGroupReturnAfterEvoke, OrbCounter), OnOrgasm_onOrgasm
{
    var evokeOnEndOfTurn: Boolean = false

    fun setDiscardOnEndOfTurn(): CardOrb_OnOrgasm
    {
        this.evokeOnEndOfTurn = true
        this.setTriggerDiscardIfMoveToDiscard()
        return this
    }

    protected fun actionAccept()
    {
        AutoDoneInstantAction.addToBotAbstract { action.accept(this) }
    }

    override fun onEndOfTurn()
    {
        if (!evokeOnEndOfTurn) return
        //        InBattleDataManager.getHangUpCardOrbGroup().ifPresent(group -> group.evokeOrb(this));
        setShouldRemove()
    }

    abstract override fun onOrgasm(SexualHeatPower: SexualHeat)

    abstract override fun forceAcceptAction(card: AbstractCard)

    override fun onRemoveCard()
    {
    }
}
