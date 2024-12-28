package superstitioapi.hangUpCard.onDamage

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.DamageInfo
import superstitioapi.hangUpCard.onDamage.CardOrb_BlockDamage.Power_BlockDamage
import superstitioapi.utils.CostSmart
import superstitioapi.utils.addToBot_removeSelf

/**
 * 受到攻击伤害时，超出阈值再处理
 */

abstract class CardOrb_BlockDamage @JvmOverloads constructor(
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    OrbCounter: CostSmart,
    private val actionOnDamagedRemove: () -> Unit = {},
    private val actionOnNaturalRemove: () -> Unit = {}
) : CardOrb_OnAttackedToChangeDamage<Power_BlockDamage>(
    card, cardGroupReturnAfterEvoke, OrbCounter, Power_BlockDamage(
        OrbCounter.toInt(), CardOrb_BlockDamage::class.java.simpleName
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

    class Power_BlockDamage(amountForCardOrb: Int, id: String) :
        Power_OnAttackedToChangeDamage(amountForCardOrb, id)
    {
        var flagOfDamageRemove = false
        override fun onAttackedToChangeDamage(info: DamageInfo?, damageAmount: Int): Int
        {
            if (damageAmount == 0) return 0
            if (amount == 0) return damageAmount
            if (info?.type != DamageInfo.DamageType.NORMAL) return damageAmount
            this.cardOrb?.StartHitCreature(this.owner)
            if (damageAmount < amount)
            {
//                this.addToTop_reducePowerToOwner(this, damageAmount)
                this.amount -= damageAmount
                this.cardOrb?.orbCounter?.let { it -= damageAmount }
                return 0
            }

            val amountReturn = damageAmount - amount
            this.addToBot_removeSelf()
            cardOrb?.setShouldRemove()
            this.amount = 0
            this.cardOrb?.orbCounter?.changeCost { 0 }
            (cardOrb as? CardOrb_BlockDamage)?.onDamagedRemove()
            flagOfDamageRemove = true
            return amountReturn
        }
    }
}

