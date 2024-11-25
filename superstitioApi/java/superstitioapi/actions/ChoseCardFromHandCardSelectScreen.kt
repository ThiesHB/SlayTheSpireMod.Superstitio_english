//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package superstitioapi.actions

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import java.util.function.Consumer
import java.util.function.Predicate

class ChoseCardFromHandCardSelectScreen(private val actionApply: Consumer<AbstractCard>) :
    AbstractContinuallyAction(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_XFAST) {
    private val player: AbstractPlayer = AbstractDungeon.player
    private val temp_remove_from_hand = CardGroup(CardGroupType.UNSPECIFIED)
    private var windowText = ""
    private var anyNumber = false
    private var canPickZero = false
    private var retainFilter = Predicate { card: AbstractCard? -> true }

    init {
        this.target = player
        this.source = player
        this.amount = 1
    }

    private fun doAction(c: AbstractCard) {
        actionApply.accept(c)
    }

    fun setChoiceAmount(choiceAmount: Int): ChoseCardFromHandCardSelectScreen {
        this.amount = choiceAmount
        return this
    }

    fun setSource(source: AbstractCreature): ChoseCardFromHandCardSelectScreen {
        this.source = source
        return this
    }

    fun setWindowText(windowText: String): ChoseCardFromHandCardSelectScreen {
        this.windowText = windowText
        return this
    }

    fun setAnyNumber(anyNumber: Boolean): ChoseCardFromHandCardSelectScreen {
        this.anyNumber = anyNumber
        return this
    }

    fun setCanPickZero(canPickZero: Boolean): ChoseCardFromHandCardSelectScreen {
        this.canPickZero = canPickZero
        return this
    }

    @SafeVarargs
    fun setRetainFilter(vararg filters: Predicate<AbstractCard>?): ChoseCardFromHandCardSelectScreen {
        filters.forEach { abstractCardPredicate: Predicate<AbstractCard>? ->
            if (abstractCardPredicate == null)
                return@forEach
            this.retainFilter = retainFilter
                .and { card: AbstractCard? -> card?.let(abstractCardPredicate::test) ?: false }
        }
        return this
    }

    override fun StartAction() {
        if (this.amount <= 0) {
            this.isDone = true
            return
        }
        player.hand.group.stream().filter { card: AbstractCard -> !retainFilter.test(card) }
            .forEach(temp_remove_from_hand.group::add)
        player.hand.group.removeAll(temp_remove_from_hand.group.toSet())

        //        this.player.hand.group.clear();
        if (player.hand.size() <= this.amount && !anyNumber && !canPickZero) {
            val hand = player.hand
            this.amount = hand.size()
            hand.group.forEach(Consumer(this::doAction))
            temp_remove_from_hand.group.forEach(Consumer(player.hand::addToTop))
            this.isDone = true
        } else if (!player.hand.isEmpty) //if (this.player.hand.size() > this.amount)
            AbstractDungeon.handCardSelectScreen.open(windowText, this.amount, anyNumber, canPickZero)
        AbstractDungeon.player.hand.applyPowers()
    }

    override fun RunAction() {
        if (AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) return
        temp_remove_from_hand.group.forEach(Consumer(player.hand::addToTop))
        AbstractDungeon.handCardSelectScreen.selectedCards.group.forEach(Consumer { card: AbstractCard ->
            player.hand.addToTop(card)
            doAction(card)
        })
        AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true
        AbstractDungeon.handCardSelectScreen.selectedCards.group.clear()
        temp_remove_from_hand.clear()
        player.hand.refreshHandLayout()
    }

    override fun EndAction() {
    }
}
