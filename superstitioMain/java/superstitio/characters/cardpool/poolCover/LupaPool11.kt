package superstitio.characters.cardpool.poolCover

import com.megacrit.cardcrawl.cards.AbstractCard
import superstitio.DataManager
import superstitio.cards.CardOwnerPlayerManager
import superstitio.cards.lupa.PowerCard.defend.DrinkSemenBeer
import java.util.function.*

class LupaPool : AbstractCover(ID, DrinkSemenBeer()) {
    override fun getAddedCard(): Predicate<AbstractCard> {
        return Predicate(CardOwnerPlayerManager::isOnlyLupaCard)
    }

    override fun getBanedCard(): Predicate<AbstractCard>? {
        return null
    }

    companion object {
        val ID: String = DataManager.MakeTextID(LupaPool::class.java)
    }
}
