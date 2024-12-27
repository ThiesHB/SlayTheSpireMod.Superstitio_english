package superstitioapi.hangUpCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import superstitioapi.utils.CostSmart
import java.util.function.Predicate

abstract class CardOrb_CardTrigger(
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    orbCounter: CostSmart,
    val action: (CardOrb_CardTrigger, AbstractCard) -> Unit
) : CardOrb(card, cardGroupReturnAfterEvoke, orbCounter), ICardOrb_CanEvokeOnEndOfTurn<CardOrb_CardTrigger>
{
    var cardMatcher: Predicate<AbstractCard> = Predicate { true }
    override var evokeOnEndOfTurn: Boolean = false

    fun setEvokeOnEndOfTurn(): CardOrb_CardTrigger
    {
        this.evokeOnEndOfTurn = true
        return this
    }

    protected abstract fun onProperCardUsed_checkIfShouldApplyAction(card: AbstractCard?): Boolean

    private fun tryAcceptAction(card: AbstractCard)
    {
        tryCheckZeroAndAcceptAction { action(this, card) }
    }

    private fun TestIfCardIsRight_use(hoveredCard: AbstractCard?): Boolean
    {
        if (hoveredCard == null) return false
        if (hoveredCard is Card_TriggerHangCardManually)
        {
            return false
        }
        return cardMatcher.test(hoveredCard)
    }

    fun onCardUsed(card: AbstractCard?)
    {
        if (card == null) return
        if (!TestIfCardIsRight_use(card)) return
        this.fakeCard.calculateCardDamage(null)
        orbCounter--
        if (onProperCardUsed_checkIfShouldApplyAction(card))
        {
            tryAcceptAction(card)
        }
    }

    @SafeVarargs
    fun setCardPredicate(vararg cardMatchers: Predicate<AbstractCard>): CardOrb_CardTrigger
    {
        for (mather in cardMatchers)
        {
            this.cardMatcher = cardMatcher.and(mather)
        }
        return this
    }

    override fun forceAcceptAction(card: AbstractCard)
    {
        orbCounter--
        if (onProperCardUsed_checkIfShouldApplyAction(card))
            tryAcceptAction(card)
    }

    override fun onEndOfTurn()
    {
        if (!evokeOnEndOfTurn) return
        setShouldRemove()
    }

    override fun onRemoveCard()
    {
    }

    override fun TestIfCardIsRight_hover(hoveredCard: AbstractCard?): Boolean
    {
        if (hoveredCard == null) return false
        if (hoveredCard is Card_TriggerHangCardManually)
        {
            return hoveredCard.forceFilterCardOrbToHoveredMode(this)
        }
        return cardMatcher.test(hoveredCard)
    }
}
