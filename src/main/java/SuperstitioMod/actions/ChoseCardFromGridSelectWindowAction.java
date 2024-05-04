package SuperstitioMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class ChoseCardFromGridSelectWindowAction extends AbstractContinuallyAction {
    private final Function<AbstractCard, AbstractGameAction> gameActionMaker;
    private final CardGroup cardGroup;
    private String windowText = "";
    private boolean anyNumber = false;
    private Predicate<AbstractCard> retainFilter = card -> false;

    public ChoseCardFromGridSelectWindowAction(
            final CardGroup cardGroup,
            Function<AbstractCard, AbstractGameAction> GameActionMaker) {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FAST);
        this.cardGroup = cardGroup;
        this.gameActionMaker = GameActionMaker;
        this.amount = 1;
    }

    public ChoseCardFromGridSelectWindowAction setupAnyNumber(boolean anyNumber) {
        this.anyNumber = anyNumber;
        return this;
    }

    @SafeVarargs
    public final ChoseCardFromGridSelectWindowAction setupRetainFilter(Predicate<AbstractCard>... filters) {
        Arrays.stream(filters).forEach(abstractCardPredicate -> this.retainFilter = this.retainFilter.or(abstractCardPredicate));
        return this;
    }

    public ChoseCardFromGridSelectWindowAction setupWindowText(String windowText) {
        this.windowText = windowText;
        return this;
    }

    public ChoseCardFromGridSelectWindowAction setupChoseAmount(int choseAmount) {
        this.amount = choseAmount;
        return this;
    }

    @Override
    protected void RunAction() {
        if (AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) return;
        for (final AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
            this.addToBot(gameActionMaker.apply(c));
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
