package superstitio.cards

import com.megacrit.cardcrawl.cards.AbstractCard
import superstitio.DataManager
import superstitio.cards.lupa.LupaCard
import superstitio.cards.maso.MasoCard
import java.util.function.BiFunction

object CardOwnerPlayerManager {
    fun getImgPath(tag: String, id: String): String {
        return DataManager.makeImgPath(
            "default",
            BiFunction<String, Array<String>, String>(DataManager::makeImgFilesPath_Card),
            id,
            tag
        )
    }

    fun isLupaCard(card: AbstractCard?): Boolean {
        return card is IsLupaCard && card !is IsNotLupaCard
    }

    fun isLupaCard(cardType: Class<*>?): Boolean {
        return IsLupaCard::class.java.isAssignableFrom(cardType) && !(IsNotLupaCard::class.java.isAssignableFrom(
            cardType
        ))
    }

    fun isOnlyLupaCard(card: AbstractCard?): Boolean {
        return isLupaCard(card) && !isMasoCard(card)
    }

    fun isOnlyMasoCard(card: AbstractCard?): Boolean {
        return isMasoCard(card) && !isLupaCard(card)
    }

    fun isMasoCard(card: AbstractCard?): Boolean {
        return card is IsMasoCard && card !is IsNotMasoCard
    }

    fun isMasoCard(cardType: Class<*>): Boolean {
        return IsMasoCard::class.java.isAssignableFrom(cardType) && !(IsNotMasoCard::class.java.isAssignableFrom(
            cardType
        ))
    }

    fun isGeneralCard(cardType: AbstractCard): Boolean {
        return isMasoCard(cardType) && isLupaCard(cardType)
    }

    fun isGeneralCard(cardType: Class<*>): Boolean {
        return isMasoCard(cardType) && isLupaCard(cardType)
    }

    fun getCardClass(card: AbstractCard): String {
        val packageName = card.javaClass.getPackage().name
        if (packageName.contains(LupaCard::class.java.getPackage().name)) {
            return "Lupa"
        }
        if (packageName.contains(MasoCard::class.java.getPackage().name)) {
            return "Maso"
        }
        return "General"
    }

    interface IsMasoCard

    /**
     * 优先级更高
     */
    interface IsNotMasoCard

    interface IsLupaCard

    interface IsNotLupaCard
}
