package superstitio.orbs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import org.apache.logging.log4j.util.BiConsumer;

public class CardOrb_EachCardTrigger extends CardOrb_CardTrigger {
    private int triggerTime;

    public CardOrb_EachCardTrigger(AbstractCard card, BiConsumer<CardOrb_CardTrigger, AbstractCard> action_thisCard_targetCard, int triggerTime) {
        super(card,action_thisCard_targetCard);
        this.triggerTime = triggerTime;
    }


    @Override
    public AbstractOrb makeCopy() {
        return new CardOrb_EachCardTrigger(card, action, triggerTime);
    }

    @Override
    public boolean shouldRemove() {
        return this.triggerTime <= 0 && checkShouldStopMoving();
    }

    @Override
    public void update() {
        super.update();
        setEvokeAmount(triggerTime - 1);
        setPassiveAmount(triggerTime);
    }

    @Override
    protected boolean onProperCardUsed_IfShouldApply(AbstractCard card) {
        triggerTime--;
        return true;
    }
}
