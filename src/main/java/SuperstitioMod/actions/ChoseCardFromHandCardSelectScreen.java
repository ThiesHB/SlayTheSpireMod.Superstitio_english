//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SuperstitioMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.function.Function;

public class ChoseCardFromHandCardSelectScreen extends ContinuallyAction {
    public static int numDiscarded;
    private final AbstractPlayer p;
    private final Function<AbstractCard, AbstractGameAction> gameActionMaker;
    private final String windowText;

    public ChoseCardFromHandCardSelectScreen(AbstractCreature source,String windowText, int choseAmount, Function<AbstractCard, AbstractGameAction> gameActionMaker) {
        super(ActionType.DISCARD, Settings.ACTION_DUR_XFAST);
        this.p = AbstractDungeon.player;
        this.windowText = windowText;
        this.gameActionMaker = gameActionMaker;
        this.setValues(target, source, choseAmount);
    }

    @Override
    protected void RunAction() {
        if (AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) return;
        for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
            doAction(card);
        }
        AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
    }

    @Override
    protected void ActionSetUp() {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.isDone = true;
            return;
        }

        if (this.p.hand.size() <= this.amount) {
            CardGroup hand = this.p.hand;
            this.amount = hand.size();
            hand.group.forEach(this::doAction);
        }
        else if (this.amount < 0) {
            AbstractDungeon.handCardSelectScreen.open(windowText, 99, true, true);
        }
        else {
            numDiscarded = this.amount;
            if (this.p.hand.size() > this.amount)
                AbstractDungeon.handCardSelectScreen.open(windowText, this.amount, false);
        }
        AbstractDungeon.player.hand.applyPowers();
    }

    private void doAction(AbstractCard c1) {
        this.addToBot(this.gameActionMaker.apply(c1));
    }
}
