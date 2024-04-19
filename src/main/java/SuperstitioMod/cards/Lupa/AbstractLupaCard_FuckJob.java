package SuperstitioMod.cards.Lupa;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.characters.Lupa;

public abstract class AbstractLupaCard_FuckJob extends AbstractLupaCard {
    /**
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    public AbstractLupaCard_FuckJob(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget) {
        this(id, cardType, cost, cardRarity, cardTarget, CardTypeToString(cardType));
    }

    public AbstractLupaCard_FuckJob(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, String customCardType) {
        this(id, cardType, cost, cardRarity, cardTarget, Lupa.Enums.LUPA_CARD, customCardType);
    }

    public AbstractLupaCard_FuckJob(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor) {
        this(id, cardType, cost, cardRarity, cardTarget, cardColor, CardTypeToString(cardType));
    }

    public AbstractLupaCard_FuckJob(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor,
                                    String customCardType) {
        super(id, cardType, cost, cardRarity, cardTarget, cardColor, customCardType);
        if (!SuperstitioModSetup.enableSFW)
            this.setBackgroundTexture(
                    SuperstitioModSetup.makeImgFilesPath_UI("bg_attack_512_semen"),
                    SuperstitioModSetup.makeImgFilesPath_UI("bg_attack_semen"));
    }

}
