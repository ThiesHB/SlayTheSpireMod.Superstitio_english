package superstitio.cards.general;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import superstitio.DataManager;
import superstitio.cards.general.BaseCard.Kiss;
import superstitio.cards.lupa.BaseCard.Masturbate;
import superstitio.characters.BaseCharacter;
import superstitio.relics.blight.JokeDescription;

import java.util.ArrayList;

// 继承CustomPlayer类
public class GeneralCardVirtualCharacter extends BaseCharacter {
    public static final String ID = DataManager.MakeTextID("GeneralCard");

    public GeneralCardVirtualCharacter(String name) {
        super(ID, name, DataManager.SPTT_DATA.GeneralEnums.GENERAL_Virtual_Character);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> startingDeck = new ArrayList<>();
        for (int x = 0; x < 5; x++) {
            startingDeck.add(Kiss.ID);
        }
        startingDeck.add(Masturbate.ID);
        return startingDeck;
    }

    @Override
    // 初始遗物
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(JokeDescription.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(characterStrings.NAMES[0], // 人物名字
                characterStrings.TEXT[0], // 人物介绍
                0, // 当前血量
                60, // 最大血量
                0, // 初始充能球栏位
                99, // 初始携带金币
                5, // 每回合抽牌数量
                this, // 别动
                this.getStartingRelics(), // 初始遗物
                this.getStartingDeck(), // 初始卡组
                false // 别动
        );
    }


    // 你的卡牌颜色（这个枚举在最下方创建）
    @Override
    public AbstractCard.CardColor getCardColor() {
        return DataManager.SPTT_DATA.GeneralEnums.GENERAL_CARD;
    }

    // 碎心图片
    @Override
    public ArrayList<CutscenePanel> getCutscenePanels() {
        return new ArrayList<>();
    }

    // 创建人物实例，照抄
    @Override
    public AbstractPlayer newInstance() {
        return new GeneralCardVirtualCharacter(this.name);
    }

    @Override
    protected boolean isCardCanAdd(AbstractCard card) {
        return false;
    }
}