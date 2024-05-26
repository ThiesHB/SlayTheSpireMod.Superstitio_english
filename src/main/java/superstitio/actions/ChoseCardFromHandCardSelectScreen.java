//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package superstitio.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class ChoseCardFromHandCardSelectScreen extends AbstractContinuallyAction {
    private final AbstractPlayer player = AbstractDungeon.player;
    private final Function<AbstractCard, AbstractGameAction> gameActionMaker;
    private final CardGroup temp_remove_from_hand = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private String windowText = "";
    private boolean anyNumber = false;
    private boolean canPickZero = false;
    private Predicate<AbstractCard> retainFilter = card -> true;

    public ChoseCardFromHandCardSelectScreen(Function<AbstractCard, AbstractGameAction> gameActionMaker) {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_XFAST);
        this.gameActionMaker = gameActionMaker;
        this.target = player;
        this.source = player;
        this.amount = 1;
    }

    @Override
    protected void RunAction() {
        if (AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) return;
        this.temp_remove_from_hand.group.forEach(card -> this.player.hand.addToTop(card));
        AbstractDungeon.handCardSelectScreen.selectedCards.group.forEach(card -> {
            this.player.hand.addToTop(card);
            doAction(card);
        });
        AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        this.temp_remove_from_hand.clear();
        this.player.hand.refreshHandLayout();
    }

    @Override
    protected void ActionSetUp() {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead() || this.amount <= 0) {
            this.isDone = true;
            return;
        }
        this.player.hand.group.stream().filter(card -> !retainFilter.test(card)).forEach(this.temp_remove_from_hand.group::add);
        this.player.hand.group.removeAll(this.temp_remove_from_hand.group);
//        this.player.hand.group.clear();

        if (this.player.hand.size() <= this.amount && !anyNumber && !canPickZero) {
            CardGroup hand = this.player.hand;
            this.amount = hand.size();
            hand.group.forEach(this::doAction);
            this.temp_remove_from_hand.group.forEach(card -> this.player.hand.addToTop(card));
            this.isDone = true;
        }
        else //if (this.player.hand.size() > this.amount)
            AbstractDungeon.handCardSelectScreen.open(windowText, this.amount, anyNumber, canPickZero);
        AbstractDungeon.player.hand.applyPowers();
    }

    private void doAction(AbstractCard c) {
        this.addToBot(this.gameActionMaker.apply(c));
    }

    public ChoseCardFromHandCardSelectScreen setChoiceAmount(int choiceAmount) {
        this.amount = choiceAmount;
        return this;
    }

    public ChoseCardFromHandCardSelectScreen setSource(AbstractCreature source) {
        this.source = source;
        return this;
    }

    public ChoseCardFromHandCardSelectScreen setWindowText(String windowText) {
        this.windowText = windowText;
        return this;
    }

    public ChoseCardFromHandCardSelectScreen setAnyNumber(boolean anyNumber) {
        this.anyNumber = anyNumber;
        return this;
    }

    public ChoseCardFromHandCardSelectScreen setCanPickZero(boolean canPickZero) {
        this.canPickZero = canPickZero;
        return this;
    }

    @SafeVarargs
    public final ChoseCardFromHandCardSelectScreen setRetainFilter(Predicate<AbstractCard>... filters) {
        Arrays.stream(filters).forEach(abstractCardPredicate -> this.retainFilter = this.retainFilter.and(abstractCardPredicate));
        return this;
    }
}
