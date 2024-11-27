package superstitio.characters.cardpool.poolCover

import com.megacrit.cardcrawl.cards.AbstractCard
import superstitio.DataManager
import superstitio.cards.CardOwnerPlayerManager
import superstitio.cards.lupa.BaseCard.Masturbate
import java.util.function.Predicate

class GeneralPool : AbstractCover(ID, Masturbate()) {
    override fun getAddedCard(): Predicate<AbstractCard> {
        return Predicate<AbstractCard>(CardOwnerPlayerManager::isGeneralCard)
    }

    override fun getBanedCard(): Predicate<AbstractCard>? {
        return null
    }

    companion object {
        val ID: String = DataManager.MakeTextID(GeneralPool::class.java)
    }
}
