package superstitio.orbs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import org.apache.logging.log4j.util.BiConsumer;
import superstitio.utils.ActionUtility;

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
    protected ActionUtility.VoidSupplier checkAndSetTheHoverType() {
        if (this.waitTime > 1)
            return this::State_WhenHoverCard_JustGlow;
        return super.checkAndSetTheHoverType();
    }

    protected static final Color ReduceWaitTime =  Color.GOLDENROD.cpy();

    private void State_WhenHoverCard_JustGlow() {
        this.card.targetDrawScale = DRAW_SCALE_SMALL_BIGGER;
        this.card.glowColor = ReduceWaitTime;
    }

    @Override
    public void onProperCardUsed(AbstractCard card) {
        waitTime--;
        if (waitTime > 0) return;
        action.accept(this, card);
    }
}
