package superstitio.cards.general;

import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;

import static superstitio.cards.CardOwnerPlayerManager.IsLupaCard;
import static superstitio.cards.CardOwnerPlayerManager.IsMasoCard;

public abstract class AbstractGeneralCard extends SuperstitioCard implements IsMasoCard, IsLupaCard {
    /**
     * 普通的方法
     *
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    public AbstractGeneralCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget) {
        this(id, cardType, cost, cardRarity, cardTarget, CardTypeToString(cardType));
    }

    /**
     * 当需要自定义卡牌存储的二级目录名时
     */
    public AbstractGeneralCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, String imgSubFolder) {
        this(id, cardType, cost, cardRarity, cardTarget, DataManager.SPTT_DATA.GeneralEnums.GENERAL_CARD, imgSubFolder);
    }

    public AbstractGeneralCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor) {
        this(id, cardType, cost, cardRarity, cardTarget, cardColor, CardTypeToString(cardType));
    }

    public AbstractGeneralCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor,
                               String imgSubFolder) {
        super(id, cardType, cost, cardRarity, cardTarget, cardColor, imgSubFolder);
    }
}
