package superstitio.cards.general

import superstitio.DataManager.SPTT_DATA.TempCardEnums
import superstitio.cards.CardOwnerPlayerManager.IsNotLupaCard
import superstitio.cards.CardOwnerPlayerManager.IsNotMasoCard

abstract class AbstractTempCard private constructor(
    id: String,
    cardType: CardType,
    cost: Int,
    cardRarity: CardRarity,
    cardTarget: CardTarget,
    imgSubFolder: String
) : GeneralCard(id, cardType, cost, cardRarity, cardTarget, TempCardEnums.TempCard_CARD, imgSubFolder), IsNotLupaCard,
    IsNotMasoCard {
    /**
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    constructor(id: String, cardType: CardType, cost: Int, cardRarity: CardRarity, cardTarget: CardTarget) : this(
        id,
        cardType,
        cost,
        cardRarity,
        cardTarget,
        "special"
    )
}
