package SuperstitioMod.cards.ChooseSelfOrEnemy;

import SuperstitioMod.utils.ActionUtility;
import SuperstitioMod.utils.EventHelper;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class AttractAction extends AbstractGameAction {
    public static ArrayList<AbstractCard> drawnCards;

    static {
        AttractAction.drawnCards = new ArrayList<AbstractCard>();
    }

    private final AbstractPlayer player;
    private final int amt;

    public AttractAction(final int amt) {
        this.player = AbstractDungeon.player;
        this.amt = amt;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    private static void ShowCard(AbstractCard abstractCard) {
        if (AbstractDungeon.player.discardPile.contains(abstractCard) && AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
            AbstractDungeon.player.hand.addToHand(abstractCard);
            abstractCard.unhover();
            abstractCard.setAngle(0.0f, true);
            abstractCard.lighten(false);
            abstractCard.drawScale = 0.12f;
            abstractCard.targetDrawScale = 0.75f;
            abstractCard.applyPowers();
            AbstractDungeon.player.discardPile.removeCard(abstractCard);
            AttractAction.drawnCards.add(abstractCard);
            EventHelper.ON_ATTRACT_SUBSCRIBERS.forEach(sub -> sub.onAttract(abstractCard));
        } else if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            AbstractDungeon.player.createHandIsFullDialog();
        }
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.glowCheck();
        return;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            AttractAction.drawnCards.clear();
            if (this.player.discardPile.size() > this.amt) {
                for (int i = 0; i < this.amt; ++i) {
                    ActionUtility.addToTopAbstract(() -> {
                        AbstractCard c = this.player.discardPile.getTopCard();
                        ShowCard(c);
                    });
                }
            } else {
                for (final AbstractCard c2 : this.player.discardPile.group) {
//                    final AbstractCard abstractCard;
                    ActionUtility.addToTopAbstract(() -> ShowCard(c2));
                }
            }
            AbstractDungeon.player.hand.refreshHandLayout();
            this.isDone = true;
        }
        this.tickDuration();
    }
}
