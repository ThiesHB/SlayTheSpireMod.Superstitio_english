package superstitioapi.hangUpCard

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.orbs.AbstractOrb
import org.apache.logging.log4j.util.BiConsumer
import superstitioapi.utils.ActionUtility.VoidSupplier
import superstitioapi.utils.CardUtility

class CardOrb_WaitCardTrigger(
    card: AbstractCard,
    cardGroupReturnAfterEvoke: CardGroup?,
    waitTime: CardUtility.CostSmart,
    action_thisOrb_triggerCard: BiConsumer<CardOrb_CardTrigger, AbstractCard>
) : CardOrb_CardTrigger(card, cardGroupReturnAfterEvoke, waitTime, action_thisOrb_triggerCard), ICardOrb_WaitTime {
    private fun State_WhenHoverCard_JustGlow() {
        fakeCard.targetDrawScale = DRAW_SCALE_SMALL_BIGGER
        fakeCard.glowColor = ReduceWaitTime
    }

    override fun makeCopy(): AbstractOrb {
        return CardOrb_WaitCardTrigger(originCard, cardGroupReturnAfterEvoke, orbCounter, action)
    }

    override fun checkAndSetTheHoverType(): VoidSupplier {
        if (this.orbCounter > 1) return VoidSupplier(this::State_WhenHoverCard_JustGlow)
        return super.checkAndSetTheHoverType()
    }

    override fun onProperCardUsed_checkIfShouldApplyAction(card: AbstractCard?): Boolean {
        return orbCounter <= 0
    }

    companion object {
        protected val ReduceWaitTime: Color = Color.GOLDENROD.cpy()
    }
}
