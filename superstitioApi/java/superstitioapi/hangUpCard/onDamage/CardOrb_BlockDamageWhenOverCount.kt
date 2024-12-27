package superstitioapi.hangUpCard.onDamage

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.DamageInfo
import superstitioapi.hangUpCard.onDamage.CardOrb_BlockDamageWhenOverCount.Power_BlockDamageWhenOverCount
import superstitioapi.utils.CostSmart
import superstitioapi.utils.addToBot_removeSelf
import kotlin.math.max

/**
 * 受到攻击伤害时，超出阈值再处理
 */

abstract class CardOrb_BlockDamageWhenOverCount @JvmOverloads constructor(
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    OrbCounter: CostSmart,
    private val actionOnDamagedRemove: () -> Unit = {},
    private val actionOnNaturalRemove: () -> Unit = {}
) : CardOrb_OnAttackedToChangeDamage<Power_BlockDamageWhenOverCount>(
    card, cardGroupReturnAfterEvoke, OrbCounter, Power_BlockDamageWhenOverCount(
        OrbCounter.toInt(), CardOrb_BlockDamageWhenOverCount::class.java.simpleName
    )
)
{
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

    class Power_BlockDamageWhenOverCount(amountForCardOrb: Int, id: String) :
        Power_OnAttackedToChangeDamage(amountForCardOrb, id)
    {
        var flagOfDamageRemove = false

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
            this.cardOrb?.orbCounter = CostSmart.makeZero()
            (cardOrb as? CardOrb_BlockDamageWhenOverCount)?.onDamagedRemove()
            flagOfDamageRemove = true
            return amountReturn
        }
    }
}

