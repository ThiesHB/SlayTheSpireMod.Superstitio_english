package superstitioapi.hangUpCard.onDamage

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitioapi.hangUpCard.CardOrb
import superstitioapi.hangUpCard.CardOrb_BondToPower
import superstitioapi.hangUpCard.ICardOrb_CanEvokeOnEndOfTurn
import superstitioapi.utils.CostSmart

abstract class CardOrb_OnAttackedToChangeDamage<PowerType>(
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    OrbCounter: CostSmart,
    power: PowerType
) : CardOrb_BondToPower<PowerType>(card, cardGroupReturnAfterEvoke, OrbCounter, power),
    ICardOrb_CanEvokeOnEndOfTurn<CardOrb_OnAttackedToChangeDamage<PowerType>>
        where  PowerType : AbstractPower, PowerType : CardOrb_BondToPower.IBondToCardOrb_Power
{
    override var evokeOnEndOfTurn: Boolean = false

//    abstract fun onPlayerDamaged(amount: Int, info: DamageInfo?): Int

    override fun onEndOfTurn()
    {
        if (!evokeOnEndOfTurn) return
        setShouldRemove()
    }

    override fun onRemoveCard()
    {
    }

    open class Power_OnAttackedToChangeDamage(amountForCardOrb: Int, id: String) :
        AbstractPower(), InvisiblePower, IBondToCardOrb_Power
    {
        override var amountForCardOrb: Int
            get() = amount
            set(value)
            {
                amount = value
            }

        override var cardOrb: CardOrb? = null
        override var order: Int = 0

        init
        {
            this.name = id
            this.ID = id
            this.owner = AbstractDungeon.player
            this.type = PowerType.BUFF
            this.amount = amountForCardOrb
            this.description = "Power_OnAttackedToChangeDamage"

            this.loadRegion("buffer")
        }
    }
}

