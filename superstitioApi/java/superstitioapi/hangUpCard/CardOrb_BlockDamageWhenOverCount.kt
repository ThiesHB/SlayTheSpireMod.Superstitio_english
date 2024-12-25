package superstitioapi.hangUpCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.orbs.AbstractOrb
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.hangUpCard.CardOrb_BlockDamageWhenOverCount.Power_BlockDamageWhenOverCount
import superstitioapi.utils.CostSmart
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.addToBot_applyPowerSelf
import superstitioapi.utils.addToBot_removeSelf
import kotlin.math.max

/**
 * 受到攻击伤害时，超出阈值再处理
 */
open class CardOrb_BlockDamageWhenOverCount(
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    OrbCounter: CostSmart,
    private val actionOnDamagedRemove: () -> Unit,
    private val actionOnNaturalRemove: () -> Unit
) : CardOrb_OnAttackedToChangeDamage<Power_BlockDamageWhenOverCount>(
    card, cardGroupReturnAfterEvoke, OrbCounter, Power_BlockDamageWhenOverCount(
        OrbCounter.toInt(), CardOrb_BlockDamageWhenOverCount::class.java.simpleName
    )
)
{
//    override fun onPlayerDamaged(amount: Int, info: DamageInfo?): Int
//    {
//
//        if (amount < this.orbCounter.toInt())
//            return amount
//        this.orbCounter = CostSmart.Zero
//        return amount - this.orbCounter.toInt()
//    }

    override fun addToBot_HangCard()
    {
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(this)
        AutoDoneInstantAction.addToBotAbstract {
            var max = 0
            PowerUtility.foreachPower { if (it is Power_BlockDamageWhenOverCount) max = max(max, it.order) }
            this.power.ID += max + 1
            this.power.order = max + 1
            this.power.addToBot_applyPowerSelf()
        }
    }

    /***
     * 强行执行没有意义
     */
    override fun forceAcceptAction(card: AbstractCard)
    {
    }

    private fun onDamagedRemove()
    {
        tryCheckZeroAndAcceptAction { this.actionOnDamagedRemove() }
    }

    private fun onNaturalRemove()
    {
        tryCheckZeroAndAcceptAction { this.actionOnNaturalRemove() }
    }

    override fun onRemoveCard()
    {
        super.onRemoveCard()
        if (!this.power.flagOfDamageRemove)
            this.onNaturalRemove()
        this.power.amount = 0
        this.power.addToBot_removeSelf()
    }

    override fun makeCopy(): AbstractOrb
    {
        return CardOrb_BlockDamageWhenOverCount(
            this.originCard,
            this.cardGroupReturnAfterEvoke,
            this.orbCounter,
            this.actionOnDamagedRemove,
            this.actionOnNaturalRemove
        )
    }

    class Power_BlockDamageWhenOverCount(amountForCardOrb: Int, id: String) :
        Power_OnAttackedToChangeDamage(amountForCardOrb, id)
    {
        var flagOfDamageRemove = false
        var order: Int = 0
        override fun onAttackedToChangeDamage(info: DamageInfo?, damageAmount: Int): Int
        {
            if (info?.type != DamageInfo.DamageType.NORMAL)
                return damageAmount
            if (damageAmount < amount)
                return damageAmount
            val amountReturn = max(0, damageAmount - amount)
            this.addToBot_removeSelf()
            cardOrb?.setShouldRemove()
            this.amount = 0
            this.cardOrb?.orbCounter = CostSmart.Zero
            (cardOrb as? CardOrb_BlockDamageWhenOverCount)?.onDamagedRemove()
            flagOfDamageRemove = true
            return amountReturn
        }
    }
}

