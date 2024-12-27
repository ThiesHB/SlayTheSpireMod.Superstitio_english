package superstitio.characters.cardpool

import basemod.IUIElement
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.Settings
import superstitioapi.cardPool.BaseCardPool
import java.util.function.Predicate

object CardPoolManager : IUIElement
{
    val cardPools: MutableList<BaseCardPool> = ArrayList()

    init
    {
        cardPools.add(GeneralCardPool)
        cardPools.add(LupaCardPool)
        cardPools.add(MasoCardPool)

        var x = Settings.WIDTH * 0.2f
        val y = Settings.HEIGHT * 0.8f
        for (cardPool in cardPools)
        {
            cardPool.transportTo(x, y)
            x += BaseCardPool.HB_W_CARD * BaseCardPool.COVER_DRAW_SCALE * BaseCardPool.COVER_DRAW_HOVER_SCALE_RATE
        }
    }

    fun getAddedCard(): Predicate<AbstractCard?>
    {
        var cardPredicate = Predicate<AbstractCard?> { false }
        for (cardPool in cardPools)
        {
            cardPredicate = cardPredicate.or(cardPool.getAddedCard())
        }
        return cardPredicate
    }

    fun getBanedCard(): Predicate<AbstractCard?>
    {
        var cardPredicate = Predicate<AbstractCard?> { false }
        for (cardPool in cardPools)
        {
            cardPredicate = cardPredicate.or(cardPool.getBanedCard())
        }
        return cardPredicate
    }

    override fun render(spriteBatch: SpriteBatch)
    {
        cardPools.forEach { cardPool: BaseCardPool -> cardPool.render(spriteBatch) }
    }

    override fun update()
    {
        cardPools.forEach(BaseCardPool::update)
    }

    override fun renderLayer(): Int
    {
        return 0
    }

    override fun updateOrder(): Int
    {
        return 0
    }
}
