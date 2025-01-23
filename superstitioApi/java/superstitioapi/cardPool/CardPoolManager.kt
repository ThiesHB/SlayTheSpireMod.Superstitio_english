package superstitioapi.cardPool

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption
import superstitioapi.OkiWillSave
import java.util.function.Predicate

object CardPoolManager
{

    /**
    对于每个角色，都可以定义她有什么可用的卡池，之后这些卡池就会自动显示
    这个类会收集所有的这些卡池，但仅在每次开启游戏后第一次时创建，之后就一直保存
     */
    val allCardPools: ArrayList<BaseCardPool> = ArrayList()

    init
    {
        for (character in CardCrawlGame.characterManager.allCharacters)
        {
            if (character is CharacterWithCardPool)
            {
                allCardPools.addAll(character.ableCardPools)
            }
        }
        CardPoolManagerSave.loadConfig()
    }


    fun getAddedCard(character: CharacterWithCardPool): Predicate<AbstractCard?>
    {
        var cardPredicate = Predicate<AbstractCard?> { false }
        for (cardPool in character.ableCardPools)
        {
            cardPredicate = cardPredicate.or(cardPool.getAddedCard())
        }
        return cardPredicate
    }

    fun getBanedCard(character: CharacterWithCardPool): Predicate<AbstractCard?>
    {
        var cardPredicate = Predicate<AbstractCard?> { false }
        for (cardPool in character.ableCardPools)
        {
            cardPredicate = cardPredicate.or(cardPool.getBanedCard())
        }
        return cardPredicate
    }

    fun render(character: CharacterWithCardPool, characterOption: CharacterOption, spriteBatch: SpriteBatch)
    {
        var x = Settings.WIDTH * 0.2f
        val y = Settings.HEIGHT * 0.8f
        for (cardPool in allCardPools)
        {
            cardPool.transportTo(x, y)
            x += BaseCardPool.HB_W_CARD * BaseCardPool.COVER_DRAW_SCALE * BaseCardPool.COVER_DRAW_HOVER_SCALE_RATE
        }
        character.ableCardPools.forEach { cardPool: BaseCardPool -> cardPool.render(spriteBatch) }
    }

    fun update(character: CharacterWithCardPool, characterOption: CharacterOption)
    {
        allCardPools.forEach(BaseCardPool::update)
    }

    class CardPoolManagerSave : OkiWillSave()
    {
        @JvmField
        var cardPoolId_IsSelect: HashMap<String, Boolean> = HashMap()

        override fun onSave()
        {
            allCardPools.forEach { cardPool: BaseCardPool ->
                cardPoolId_IsSelect[cardPool.id] = cardPool.isSelect
            }
        }

        override fun onLoad(save: OkiWillSave)
        {
            if (save !is CardPoolManagerSave) return
            this.cardPoolId_IsSelect = save.cardPoolId_IsSelect
            for ((cardPoolId: String, isSelect: Boolean) in this.cardPoolId_IsSelect)
            {
                for (cardPool: BaseCardPool in allCardPools)
                {
                    if (cardPool.id == cardPoolId)
                        cardPool.isSelect = isSelect
                }
            }
        }

        companion object
        {
            @Transient
            private val instance: CardPoolManagerSave = CardPoolManagerSave()
            fun saveConfig()
            {
                saveConfig(instance, CardPoolManagerSave::class.java)
            }

            fun loadConfig()
            {
                loadConfig(instance, CardPoolManagerSave::class.java)
            }
        }
    }
}