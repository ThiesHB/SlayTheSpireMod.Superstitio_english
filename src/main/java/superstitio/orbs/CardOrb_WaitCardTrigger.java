package superstitio.orbs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import org.apache.logging.log4j.util.BiConsumer;

public class CardOrb_WaitCardTrigger extends CardOrb_CardTrigger {
    private int waitTime;

    public CardOrb_WaitCardTrigger(AbstractCard card, BiConsumer<CardOrb_CardTrigger, AbstractCard> action_thisCard_targetCard, int waitTime) {
        super(card, action_thisCard_targetCard);
        this.waitTime = waitTime;
    }

    @Override
    public AbstractOrb makeCopy() {
        return new CardOrb_WaitCardTrigger(card, action, waitTime);
    }

    @Override
    public boolean shouldRemove() {
        return this.waitTime <= 0 && checkShouldStopMoving();
    }

    @Override
    public void update() {
        super.update();
        setEvokeAmount(waitTime - 1);
        setPassiveAmount(waitTime);
    }

    @Override
    protected CardOrbMovingType checkAndSetTheType() {
        if (this.waitTime > 1)
            return CardOrbMovingType.focusOnNothing;
        return super.checkAndSetTheType();
    }

    @Override
    public void onProperCardUsed(AbstractCard card) {
        waitTime--;
        this.card.flash();
        if (waitTime > 0) return;
        action.accept(this, card);
    }
}
