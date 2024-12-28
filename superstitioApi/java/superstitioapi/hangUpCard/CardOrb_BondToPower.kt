package superstitioapi.hangUpCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.CostSmart
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.addToBot_applyPowerSelf
import superstitioapi.utils.addToBot_removeSpecificPower
import kotlin.math.max

/***
 * 效果全部交给power去实现，自己只作为展示
 */
abstract class CardOrb_BondToPower<PowerType>(
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    OrbCounter: CostSmart,
    val power: PowerType
) : CardOrb(card, cardGroupReturnAfterEvoke, OrbCounter)
        where PowerType : AbstractPower, PowerType : CardOrb_BondToPower.IBondToCardOrb_Power
{
    interface IBondToCardOrb_Power
    {
        var amountForCardOrb: Int

        var cardOrb: CardOrb?

        var order: Int
    }

    override fun addToBot_HangCard()
    {
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(this)
        AutoDoneInstantAction.addToBotAbstract {
            var max = 0
            PowerUtility.foreachPower { if (it is IBondToCardOrb_Power) max = max(max, it.order) }
            this.power.ID += max + 1
            this.power.order = max + 1
            this.power.addToBot_applyPowerSelf()
            this.power.cardOrb = this
        }
    }

    override fun onPowerModified()
    {
        this.orbCounter.changeCost { power.amountForCardOrb }
    }

    override fun afterOrbCounterChange(field: CostSmart)
    {
        super.afterOrbCounterChange(field)
        this.power.amountForCardOrb = field.toInt()
    }

    override fun forceAcceptAction(card: AbstractCard)
    {
    }

    override fun onRemoveCard()
    {
        this.power.addToBot_removeSpecificPower(this.power)
    }

}
