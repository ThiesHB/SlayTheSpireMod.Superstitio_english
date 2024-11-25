package superstitio.cards.general

import superstitio.DataManager.SPTT_DATA.GeneralEnums
import superstitio.cards.CardOwnerPlayerManager.IsLupaCard
import superstitio.cards.CardOwnerPlayerManager.IsMasoCard
import superstitio.cards.SuperstitioCard

abstract class GeneralCard @JvmOverloads constructor(
    id: String, cardType: CardType, cost: Int, cardRarity: CardRarity, cardTarget: CardTarget, cardColor: CardColor,
    imgSubFolder: String = CardTypeToString(cardType)
) : SuperstitioCard(id, cardType, cost, cardRarity, cardTarget, cardColor, imgSubFolder), IsMasoCard, IsLupaCard {
    /**
     * 当需要自定义卡牌存储的二级目录名时
     */
    /**
     * 普通的方法
     *
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    @JvmOverloads
    constructor(
        id: String,
        cardType: CardType,
        cost: Int,
        cardRarity: CardRarity,
        cardTarget: CardTarget,
        imgSubFolder: String = CardTypeToString(cardType)
    ) : this(id, cardType, cost, cardRarity, cardTarget, GeneralEnums.GENERAL_CARD, imgSubFolder)
}
