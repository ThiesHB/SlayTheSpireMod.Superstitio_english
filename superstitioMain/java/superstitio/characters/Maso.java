package superstitio.characters;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import superstitio.DataManager;
import superstitio.Logger;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.general.BaseCard.Kiss;
import superstitio.cards.general.BaseCard.Masturbate;
import superstitio.cards.maso.BaseCard.FistIn;
import superstitio.cards.maso.BaseCard.Invite_Maso;
import superstitio.relics.a_starter.DoubleBlockWithVulnerable;

import java.util.ArrayList;

import static superstitio.DataManager.SPTT_DATA.MasoEnums.MASO_CARD;
import static superstitio.DataManager.SPTT_DATA.MasoEnums.MASO_Character;

// 继承CustomPlayer类
public class Maso extends BaseCharacter {
    public static final String ID = DataManager.MakeTextID("Maso");

    public Maso(String name) {
        super(ID, name, MASO_Character);
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    // 初始遗物
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(DoubleBlockWithVulnerable.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(characterStrings.NAMES[0], // 人物名字
                characterStrings.TEXT[0], // 人物介绍
                60, // 当前血量
                70, // 最大血量
                0, // 初始充能球栏位
                99, // 初始携带金币
                5, // 每回合抽牌数量
                this, // 别动
                this.getStartingRelics(), // 初始遗物
                this.getStartingDeck(), // 初始卡组
                false // 别动
        );
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> startingDeck = new ArrayList<>();
        Logger.run("Begin loading starter Deck Strings");
        for (int x = 0; x < 5; x++) {
            startingDeck.add(Kiss.ID);
        }
        for (int x = 0; x < 4; x++) {
            startingDeck.add(Invite_Maso.ID);
        }
        startingDeck.add(Masturbate.ID);
        startingDeck.add(FistIn.ID);
        return startingDeck;
    }

    @Override
    protected boolean cardFilter(AbstractCard card) {
        return CardOwnerPlayerManager.isMasoCard(card);
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    @Override
    public AbstractCard.CardColor getCardColor() {
        return MASO_CARD;
    }

    // 创建人物实例，照抄
    @Override
    public AbstractPlayer newInstance() {
        return new Maso(this.name);
    }
}