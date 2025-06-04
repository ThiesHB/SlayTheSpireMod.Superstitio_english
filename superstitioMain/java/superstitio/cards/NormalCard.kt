package superstitio.cards

/**
 * 普通的卡牌（指攻击卡、技能卡、能力卡），自动根据卡牌类型设置子文件夹
 */
abstract class NormalCard
/**
 * @param id         卡牌ID
 * @param cardType   卡牌类型
 * @param cost       卡牌消耗
 * @param cardRarity 卡牌稀有度
 * @param cardTarget 卡牌目标
 */ @JvmOverloads constructor(
    id: String,
    cardType: CardType,
    cost: Int,
    cardRarity: CardRarity,
    cardTarget: CardTarget,
    imgSubFolder: String = CardTypeToString(cardType),
) : SuperstitioCard(id, cardType, cost, cardRarity, cardTarget,imgSubFolder=imgSubFolder)