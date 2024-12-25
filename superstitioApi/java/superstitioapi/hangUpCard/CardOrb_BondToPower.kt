package superstitioapi.hangUpCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitioapi.utils.CostSmart
import superstitioapi.utils.addToBot_removeSpecificPower

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
    }

    override fun onPowerModified()
    {
        this.orbCounter = CostSmart(power.amountForCardOrb)
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
