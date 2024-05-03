//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SuperstitioMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Function;

public class ChoseCardFromHandCardSelectScreen extends ContinuallyAction {
    public static int numDiscarded;
    private final AbstractPlayer player;
    private final Function<AbstractCard, AbstractGameAction> gameActionMaker;
    private final String windowText;
    private final boolean anyNumber;
    private final boolean canPickZero;

    public ChoseCardFromHandCardSelectScreen(AbstractCreature source, String windowText, int choseAmount, Function<AbstractCard,
            AbstractGameAction> gameActionMaker, boolean anyNumber, boolean canPickZero) {
        super(ActionType.DISCARD, Settings.ACTION_DUR_XFAST);
        this.anyNumber = anyNumber;
        this.canPickZero = canPickZero;
        this.player = AbstractDungeon.player;
        this.windowText = windowText;
        this.gameActionMaker = gameActionMaker;
        this.target = player;
        this.source = source;
        this.amount = choseAmount;
    }

    public ChoseCardFromHandCardSelectScreen(AbstractCreature source, String windowText, int choseAmount, Function<AbstractCard,
            AbstractGameAction> gameActionMaker, boolean anyNumber) {
        this(source, windowText, choseAmount, gameActionMaker, anyNumber, false);
    }

    public ChoseCardFromHandCardSelectScreen(AbstractCreature source, String windowText, int choseAmount, Function<AbstractCard,
            AbstractGameAction> gameActionMaker) {
        this(source, windowText, choseAmount, gameActionMaker, false, false);
    }

    @Override
    protected void RunAction() {
        if (AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) return;
        AbstractDungeon.handCardSelectScreen.selectedCards.group.forEach(card -> {
            this.player.hand.addToTop(card);
            doAction(card);
        });
        AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        this.player.hand.refreshHandLayout();
    }

    @Override
    protected void ActionSetUp() {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead() || this.amount <= 0) {
            this.isDone = true;
            return;
        }

        if (this.player.hand.size() <= this.amount) {
            CardGroup hand = this.player.hand;
            this.amount = hand.size();
            hand.group.forEach(this::doAction);
        } else {
            numDiscarded = this.amount;
            if (this.player.hand.size() > this.amount)
                AbstractDungeon.handCardSelectScreen.open(windowText, this.amount, anyNumber, canPickZero);
        }
        AbstractDungeon.player.hand.applyPowers();
    }

    private void doAction(AbstractCard c) {
        this.addToBot(this.gameActionMaker.apply(c));
    }
}
