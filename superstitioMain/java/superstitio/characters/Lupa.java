package superstitio.characters;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import superstitio.DataManager;
import superstitio.Logger;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.general.BaseCard.Kiss;
import superstitio.cards.general.BaseCard.Masturbate;
import superstitio.cards.lupa.BaseCard.DrySemen;
import superstitio.cards.lupa.BaseCard.Invite_Lupa;
import superstitio.relics.a_starter.StartWithSexToy;
import superstitio.relics.blight.DevaBody_Lupa;
import superstitio.relics.blight.JokeDescription;
import superstitio.relics.blight.Sensitive;
import superstitioapi.player.PlayerInitPostDungeonInitialize;

import java.util.ArrayList;

import static superstitio.DataManager.SPTT_DATA.LupaEnums.LUPA_CARD;
import static superstitio.DataManager.SPTT_DATA.LupaEnums.LUPA_Character;
import static superstitioapi.relicToBlight.InfoBlight.addAsInfoBlight;

// 继承CustomPlayer类
public class Lupa extends BaseCharacter implements PlayerInitPostDungeonInitialize {
    public static final String ID = DataManager.MakeTextID(Lupa.class.getSimpleName());
    public static final CharacterSelectInfo characterInfo = new CharacterSelectInfo(65, 65, 99);

    public Lupa(String name) {
        super(ID, name, LUPA_Character);
    }

    protected static ArrayList<String> LupaStartDeck() {
        ArrayList<String> startingDeck = new ArrayList<>();
        for (int x = 0; x < 5; x++) {
            startingDeck.add(Kiss.ID);
        }
        for (int x = 0; x < 4; x++) {
            startingDeck.add(Invite_Lupa.ID);
        }
        startingDeck.add(Masturbate.ID);
        startingDeck.add(DrySemen.ID);
        return startingDeck;
    }

    @Override
    // 初始遗物
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(StartWithSexToy.ID);
//        retVal.add(JokeDescription.ID);
//        retVal.add(DevaBody_Lupa.ID);
//        retVal.add(Sensitive.ID);
//        retVal.add(SorM.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(characterStrings.NAMES[0], // 人物名字
                characterStrings.TEXT[0], // 人物介绍
                characterInfo.currentHp, // 当前血量
                characterInfo.maxHp, // 最大血量
                0, // 初始充能球栏位
                characterInfo.gold, // 初始携带金币
                5, // 每回合抽牌数量
                this, // 别动
                this.getStartingRelics(), // 初始遗物
                this.getStartingDeck(), // 初始卡组
                false // 别动
        );
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        Logger.run("Begin loading starter Deck Strings");
        return LupaStartDeck();
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    @Override
    public AbstractCard.CardColor getCardColor() {
        return LUPA_CARD;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Lupa(this.name);
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public void initPostDungeonInitialize() {
        addAsInfoBlight(new JokeDescription());
        addAsInfoBlight(new DevaBody_Lupa());
        addAsInfoBlight(new Sensitive());
    }

    @Override
    protected boolean isCardCanAdd(AbstractCard card) {
        return CardOwnerPlayerManager.isLupaCard(card);
    }
}