package superstitio.characters.cardpool.poolCover

import com.megacrit.cardcrawl.cards.AbstractCard
import superstitio.DataManager
import superstitio.cards.CardOwnerPlayerManager
import superstitio.cards.maso.SkillCard.CutWrist
import java.util.function.*

class MasoPool : AbstractCover(ID, CutWrist()) {
    override fun getAddedCard(): Predicate<AbstractCard> {
        return Predicate(CardOwnerPlayerManager::isOnlyMasoCard)
    }

    override fun getBanedCard(): Predicate<AbstractCard>? {
        return null
    }

    companion object {
        val ID: String = DataManager.MakeTextID(MasoPool::class.java)
    }
}
