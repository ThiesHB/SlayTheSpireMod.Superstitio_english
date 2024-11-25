package superstitioapi.cards

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.cards.AbstractCard

interface HasMultiCardsToPreview {
    val multiCardsToPreview: MutableList<AbstractCard>

    var cardsToPreviewTimer: Float

    val self: AbstractCard
        get() = this as AbstractCard

    fun setCardsToPreview(cardsToPreview: AbstractCard)

    fun update() {
        if (multiCardsToPreview.isEmpty())
            return
        val timer = cardsToPreviewTimer
        if (timer < 0) {
            cardsToPreviewTimer = multiCardsToPreview.size * SHOW_TIME
        }
        var index = MathUtils.floor(timer / SHOW_TIME)
        if (index < 0) index = 0
        if (index > multiCardsToPreview.size - 1)
            index = multiCardsToPreview.size - 1
        val newCardsToPreview = multiCardsToPreview[index]
        setCardsToPreview(newCardsToPreview)

        cardsToPreviewTimer -= Gdx.graphics.deltaTime
    }

    companion object {
        const val SHOW_TIME: Float = 1.0f
    }
}
