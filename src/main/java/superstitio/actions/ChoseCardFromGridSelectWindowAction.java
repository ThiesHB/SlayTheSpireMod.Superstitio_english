package superstitio.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ChoseCardFromGridSelectWindowAction extends AbstractContinuallyAction {
    private final Consumer<AbstractCard> gameActionMaker;
    private final CardGroup cardGroup;
    private String windowText = "";
    private boolean anyNumber = false;
    private Predicate<AbstractCard> retainFilter = card -> true;

    public ChoseCardFromGridSelectWindowAction(
            final CardGroup cardGroup,
            Consumer<AbstractCard> GameActionMaker) {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FAST);
        this.cardGroup = cardGroup;
        this.gameActionMaker = GameActionMaker;
        this.amount = 1;
    }

    public ChoseCardFromGridSelectWindowAction setAnyNumber(boolean anyNumber) {
        this.anyNumber = anyNumber;
        return this;
    }

    @SafeVarargs
    public final ChoseCardFromGridSelectWindowAction setRetainFilter(Predicate<AbstractCard>... filters) {
        Arrays.stream(filters).forEach(abstractCardPredicate -> this.retainFilter = this.retainFilter.and(abstractCardPredicate));
        return this;
    }

    public ChoseCardFromGridSelectWindowAction setWindowText(String windowText) {
        this.windowText = windowText;
        return this;
    }

    public ChoseCardFromGridSelectWindowAction setChoseAmount(int choseAmount) {
        this.amount = choseAmount;
        return this;
    }

    @Override
    protected void RunAction() {
        if (AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) return;
        for (final AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
            gameActionMaker.accept(c);
        }
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        AbstractDungeon.player.hand.refreshHandLayout();
    }

    @Override
    protected void ActionSetUp() {
        if (cardGroup.isEmpty()) {
            this.isDone = true;
            return;
        }
        final CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        cardGroup.group.stream().filter(this.retainFilter).forEach(temp::addToTop);
        temp.sortAlphabetically(true);
        temp.sortByRarityPlusStatusCardType(false);
        AbstractDungeon.gridSelectScreen.open(temp, amount, anyNumber, windowText);
    }
}
