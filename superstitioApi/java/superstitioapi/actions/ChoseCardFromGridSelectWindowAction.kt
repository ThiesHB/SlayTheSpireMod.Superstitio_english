package superstitioapi.actions

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import java.util.function.Consumer
import java.util.function.Predicate

class ChoseCardFromGridSelectWindowAction(
    private val cardGroup: CardGroup,
    private val gameActionMaker: Consumer<AbstractCard>
) : AbstractContinuallyAction(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FAST) {
    private var windowText = ""
    private var anyNumber = false
    private var retainFilter = Predicate { card: AbstractCard? -> true }

    init {
        this.amount = 1
    }

    fun setAnyNumber(anyNumber: Boolean): ChoseCardFromGridSelectWindowAction {
        this.anyNumber = anyNumber
        return this
    }

    fun setWindowText(windowText: String): ChoseCardFromGridSelectWindowAction {
        this.windowText = windowText
        return this
    }

    fun setChoseAmount(choseAmount: Int): ChoseCardFromGridSelectWindowAction {
        this.amount = choseAmount
        return this
    }

    @SafeVarargs
    fun setRetainFilter(vararg filters: Predicate<AbstractCard>): ChoseCardFromGridSelectWindowAction {
        filters.forEach { abstractCardPredicate: Predicate<AbstractCard>? ->
            if (abstractCardPredicate == null)
                return@forEach
            this.retainFilter = retainFilter
                .and { card: AbstractCard? -> card?.let(abstractCardPredicate::test) ?: false }
        }
        return this
    }

    override fun StartAction() {
        if (cardGroup.isEmpty || this.amount <= 0) {
            this.isDone = true
            return
        }
        val temp = CardGroup(CardGroupType.UNSPECIFIED)
        cardGroup.group.stream().filter(this.retainFilter).forEach(temp::addToTop)
        temp.sortAlphabetically(true)
        temp.sortByRarityPlusStatusCardType(false)
        AbstractDungeon.gridSelectScreen.open(temp, amount, anyNumber, windowText)
    }

    override fun RunAction() {
        if (AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) return
        for (c in AbstractDungeon.gridSelectScreen.selectedCards) {
            gameActionMaker.accept(c)
        }
        AbstractDungeon.gridSelectScreen.selectedCards.clear()
        AbstractDungeon.player.hand.refreshHandLayout()
    }

    override fun EndAction() {
    }
}
