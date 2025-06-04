package superstitio.cards

import com.megacrit.cardcrawl.cards.AbstractCard
import superstitio.DataManager
import superstitio.SPTT_Color
import superstitioapi.DataUtility.ImgSubPath
import superstitioapi.utils.CardUtility.hasAnnotation

object CardOwnerPlayerManager
{
    fun getImgPath(tag: String, id: String): String
    {
        return DataManager.tryGetImgPath(ImgSubPath.cardsPath.createScope(tag), id, ImgSubPath.cardsPath.resolveFile("default"))
    }


    // --- Lupa 卡牌判断 ---
    fun isLupaCard(card: AbstractCard?): Boolean
    {
        if (card == null) return false
        return isLupaCard(card.javaClass)
    }

    fun isLupaCard(cardType: Class<*>): Boolean
    {
        // 检查是否有 IsNotLupaCard，它具有更高优先级
        if (hasAnnotation(cardType, IsNotLupaCard::class.java))
        {
            return false
        }
        // 检查是否有 IsLupaCard
        // 如果需要考虑继承：可以遍历父类检查注解，但这会更复杂。
        // 通常注解是直接应用于具体类。如果想让 IsLupaCard 可继承，需要@Inherited (Java注解)
        // Kotlin 中注解默认不可继承。如果需要，可以手动检查父类。
        // 为简单起见，这里只检查当前类。
        return hasAnnotation(cardType, IsLupaCard::class.java)
    }

    // --- Maso 卡牌判断 ---
    fun isMasoCard(card: AbstractCard?): Boolean
    {
        if (card == null) return false
        return isMasoCard(card.javaClass)
    }

    fun isMasoCard(cardType: Class<*>): Boolean
    {
        if (hasAnnotation(cardType, IsNotMasoCard::class.java))
        {
            return false
        }
        return hasAnnotation(cardType, IsMasoCard::class.java)
    }

    // --- 组合判断 ---
    fun isOnlyLupaCard(card: AbstractCard?): Boolean
    {
        return isLupaCard(card) && !isMasoCard(card)
    }

    fun isOnlyMasoCard(card: AbstractCard?): Boolean
    {
        return isMasoCard(card) && !isLupaCard(card)
    }

    fun isGeneralCard(card: AbstractCard?): Boolean
    { // 参数名 cardType 改为 card 以匹配重载
        if (card == null) return false
        return isMasoCard(card.javaClass) && isLupaCard(card.javaClass)
    }

    fun isGeneralCard(cardType: Class<*>): Boolean
    {
        // General 卡牌意味着它既是 Lupa 也是 Maso，且没有被 IsNotXxx 排除
        return isMasoCard(cardType) && isLupaCard(cardType)
    }
}

/**
 * 标记卡牌属于 Lupa 类型。
 */
@Target(AnnotationTarget.CLASS) // 这个注解只能用于类
@Retention(AnnotationRetention.RUNTIME) // 注解在运行时可通过反射访问
annotation class IsLupaCard

/**
 * 标记卡牌不属于 Lupa 类型，具有更高优先级。
 * 如果一个类同时被 IsLupaCard 和 IsNotLupaCard 标记，
 * 或者其父类被 IsLupaCard 标记而它自身被 IsNotLupaCard 标记，
 * 则它不被视为 Lupa 卡牌。
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IsNotLupaCard

/**
 * 标记卡牌属于 Maso 类型。
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IsMasoCard

/**
 * 标记卡牌不属于 Maso 类型，具有更高优先级。
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IsNotMasoCard

/**
 * 卡牌所属的角色（在杀戮尖塔中表现为卡牌颜色）
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SetCardColor(val color: SPTT_Color)