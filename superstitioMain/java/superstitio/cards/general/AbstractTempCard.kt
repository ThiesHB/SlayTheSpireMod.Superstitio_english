package superstitio.cards.general

import superstitio.cards.NormalCard

abstract class AbstractTempCard : NormalCard
{
    /**
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    constructor(id: String, cardType: CardType, cost: Int, cardRarity: CardRarity, cardTarget: CardTarget) : super(
        id,
        cardType,
        cost,
        cardRarity,
        cardTarget,
        "special"
    )
}
