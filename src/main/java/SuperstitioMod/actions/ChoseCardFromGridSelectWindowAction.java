package SuperstitioMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Function;

public class ChoseCardFromGridSelectWindowAction extends ContinuallyAction {
    private final String windowText;
    private final Function<AbstractCard, AbstractGameAction> gameActionMaker;
    private final boolean anyNumber;
    private final int choseAmount;
    private final CardGroup cardGroup;

    public ChoseCardFromGridSelectWindowAction(
            int choseAmount,
            final CardGroup cardGroup,
            String WindowText,
            Function<AbstractCard, AbstractGameAction> GameActionMaker,
            boolean anyNumber) {
        super(AbstractGameAction.ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FAST);
        this.choseAmount = choseAmount;
        this.cardGroup = cardGroup;
        this.windowText = WindowText;
        this.gameActionMaker = GameActionMaker;
        this.anyNumber = anyNumber;
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
        cardGroup.group.forEach(temp::addToTop);
        temp.sortAlphabetically(true);
        temp.sortByRarityPlusStatusCardType(false);
        AbstractDungeon.gridSelectScreen.open(temp, choseAmount, anyNumber, windowText);
    }
}
