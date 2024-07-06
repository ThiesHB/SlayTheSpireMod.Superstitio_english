package superstitio.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import superstitio.DataManager;
import superstitio.Logger;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.general.BaseCard.Kiss;
import superstitio.cards.general.BaseCard.Masturbate;
import superstitio.cards.maso.BaseCard.FistIn;
import superstitio.cards.maso.BaseCard.Invite_Maso;
import superstitio.relics.a_starter.DoubleBlockWithVulnerable;
import superstitio.relics.blight.DevaBody_Masochism;
import superstitio.relics.blight.JokeDescription;
import superstitio.relics.blight.MasochismMode;
import superstitioapi.player.PlayerInitPostDungeonInitialize;
import superstitioapi.renderManager.characterSelectScreenRender.RenderInCharacterSelect;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;
import static superstitio.DataManager.SPTT_DATA.MasoEnums.MASO_CARD;
import static superstitio.DataManager.SPTT_DATA.MasoEnums.MASO_Character;
import static superstitioapi.relicToBlight.InfoBlight.addAsInfoBlight;

// 继承CustomPlayer类
public class Maso extends BaseCharacter implements PlayerInitPostDungeonInitialize, RenderInCharacterSelect {
    public static final String ID = DataManager.MakeTextID(Maso.class.getSimpleName());
    public static final CharacterSelectInfo characterInfo = new CharacterSelectInfo(60, 70, 99);

    public Maso(String name) {
        super(ID, name, MASO_Character);
    }

    public static void setUpMaso() {
        if (floorNum > 1 || !(CardCrawlGame.dungeon instanceof Exordium)) return;
        player.currentHealth = player.getLoadout().currentHp;
        if (ascensionLevel >= 6) {
            player.currentHealth = MathUtils.round((float) player.currentHealth * 0.9F);
        }
    }

    protected static ArrayList<String> MasoStartDeck() {
        ArrayList<String> startingDeck = new ArrayList<>();
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
                characterInfo.currentHp, // 当前血量
                characterInfo.maxHp, // 最大血量
                0, // 初始充能球栏位
                110, // 初始携带金币
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
        return MasoStartDeck();
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

    @Override
    public void initPostDungeonInitialize() {
//        if (!SuperstitioConfig.isEnableGuroCharacter()) {
//            SuperstitioConfig.setEnableGuroCharacter(true);
//        }
        setUpMaso();
        addAsInfoBlight(new JokeDescription());
        addAsInfoBlight(new DevaBody_Masochism());
        addAsInfoBlight(new MasochismMode());
    }

    @Override
    public void renderInCharacterSelectScreen(CharacterOption characterOption, SpriteBatch sb) {

    }

    @Override
    public void updateInCharacterSelectScreen(CharacterOption characterOption) {
        unableByGuroSetting();
    }

    @Override
    protected boolean isCardCanAdd(AbstractCard card) {
        return CardOwnerPlayerManager.isMasoCard(card);
    }
}