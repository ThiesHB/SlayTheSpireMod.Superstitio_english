package superstitioapi.hangUpCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import org.apache.logging.log4j.util.BiConsumer
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.ActionUtility.VoidSupplier
import superstitioapi.utils.CardUtility
import java.util.function.Predicate

abstract class CardOrb_CardTrigger(
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    orbCounter: CardUtility.CostSmart,
    val action: BiConsumer<CardOrb_CardTrigger, AbstractCard>
) : CardOrb(card, cardGroupReturnAfterEvoke, orbCounter) {
    var cardMatcher: Predicate<AbstractCard> = Predicate { card: AbstractCard? -> true }
    var evokeOnEndOfTurn: Boolean = false

    fun setEvokeOnEndOfTurn(): CardOrb_CardTrigger {
        this.evokeOnEndOfTurn = true
        return this
    }

    fun setDiscardOnEndOfTurn(): CardOrb_CardTrigger {
        this.evokeOnEndOfTurn = true
        this.setTriggerDiscardIfMoveToDiscard()
        return this
    }

    protected abstract fun onProperCardUsed_checkIfShouldApplyAction(card: AbstractCard?): Boolean

    protected fun actionAccept(card: AbstractCard) {
        if (orbCounter < 0) return
        AutoDoneInstantAction.addToBotAbstract(VoidSupplier { action.accept(this, card) })
    }

    private fun TestIfCardIsRight_use(hoveredCard: AbstractCard?): Boolean {
        if (hoveredCard == null) return false
        if (hoveredCard is Card_TriggerHangCardManually) {
            return false
        }
        return cardMatcher.test(hoveredCard)
    }

    fun onCardUsed(card: AbstractCard?) {
        if (card == null) return
        if (!TestIfCardIsRight_use(card)) return
        if (orbCounter <= 0) return
        this.fakeCard.calculateCardDamage(null)
        orbCounter--
        if (onProperCardUsed_checkIfShouldApplyAction(card)) actionAccept(card)
    }

    @SafeVarargs
    fun setCardPredicate(vararg cardMatchers: Predicate<AbstractCard>?): CardOrb_CardTrigger {
        for (mather in cardMatchers) {
            this.cardMatcher = cardMatcher.and(mather!!)
        }
        return this
    }

    override fun forceAcceptAction(card: AbstractCard) {
        orbCounter--
        if (onProperCardUsed_checkIfShouldApplyAction(card)) actionAccept(card)
    }

    override fun onEndOfTurn() {
        if (!evokeOnEndOfTurn) return
        //        InBattleDataManager.getHangUpCardOrbGroup().ifPresent(group -> group.evokeOrb(this));
        setShouldRemove()
    }

    override fun onRemoveCard() {
    }

    override fun TestIfCardIsRight_hover(hoveredCard: AbstractCard?): Boolean {
        if (hoveredCard == null) return false
        if (hoveredCard is Card_TriggerHangCardManually) {
            return (hoveredCard as Card_TriggerHangCardManually).forceFilterCardOrbToHoveredMode(this)
        }
        return cardMatcher.test(hoveredCard)
    }
}
