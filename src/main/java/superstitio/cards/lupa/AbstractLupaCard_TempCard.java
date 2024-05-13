package superstitio.cards.lupa;

import superstitio.SuperstitioModSetup;


public abstract class AbstractLupaCard_TempCard extends AbstractLupaCard {
    /**
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    public AbstractLupaCard_TempCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget) {
        this(id, cardType, cost, cardRarity, cardTarget, CardTypeToString(cardType));
    }

    public AbstractLupaCard_TempCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, String customCardImgFolder) {
        this(id, cardType, cost, cardRarity, cardTarget, SuperstitioModSetup.TempCardEnums.LUPA_TempCard_CARD, customCardImgFolder);
    }

    public AbstractLupaCard_TempCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor) {
        this(id, cardType, cost, cardRarity, cardTarget, cardColor, CardTypeToString(cardType));
    }

    public AbstractLupaCard_TempCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor,
                                     String customCardImgFolder) {
        super(id, cardType, cost, cardRarity, cardTarget, cardColor, customCardImgFolder);
    }
}
