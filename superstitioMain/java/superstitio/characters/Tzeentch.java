package superstitio.characters;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import superstitio.DataManager;
import superstitio.Logger;
import superstitio.SuperstitioConfig;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.general.BaseCard.Kiss;
import superstitio.cards.general.BaseCard.Masturbate;
import superstitio.cards.lupa.BaseCard.DrySemen;
import superstitio.cards.lupa.BaseCard.Invite_Lupa;
import superstitio.relics.a_starter.DoubleBlockWithVulnerable;
import superstitio.relics.a_starter.StartWithSexToy;
import superstitio.relics.blight.*;
import superstitioapi.player.PlayerInitPostDungeonInitialize;
import superstitioapi.renderManager.characterSelectScreenRender.RelicSelectionUI;
import superstitioapi.renderManager.characterSelectScreenRender.RenderInCharacterSelect;
import superstitioapi.utils.TipsUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static superstitio.DataManager.SPTT_DATA.LupaEnums.LUPA_Character;
import static superstitio.DataManager.SPTT_DATA.MasoEnums.MASO_Character;
import static superstitio.DataManager.SPTT_DATA.TzeentchEnums.TZEENTCH_Character;
import static superstitioapi.relicToBlight.InfoBlight.addAsInfoBlight;

//
public class Tzeentch extends BaseCharacter implements PlayerInitPostDungeonInitialize, RenderInCharacterSelect {
    public static final String ID = DataManager.MakeTextID(Tzeentch.class.getSimpleName());
    public static final CharacterStrings TezeentchCharacterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    public static final float Relic_Selection_Y = 500 * Settings.scale;
    public static final RelicSelectionUI STARTER_RELIC_Selection_UI;
    public static final RelicSelectionUI DEVABODY_RELIC_Selection_UI;
    public static final RelicSelectionUI SEXUAL_HEAT_RELIC_Selection_UI;
    public static final float Relic_Selection_X = 240 * Settings.scale;
    public static CharacterSelectInfo characterInfo = new CharacterSelectInfo(
            65, // 当前血量
            65, // 最大血量
            99 // 初始携带金币
    );
    public static boolean isCharacterInfoChanged = false;

    static {
        STARTER_RELIC_Selection_UI = new RelicSelectionUI(
                Relic_Selection_X,
                Relic_Selection_Y,
                Arrays.stream(new AbstractRelic[]{new StartWithSexToy(), new DoubleBlockWithVulnerable()}).collect(Collectors.toList()),
                new PowerTip(TezeentchCharacterStrings.TEXT[2], TezeentchCharacterStrings.TEXT[3]))
                .setRefreshAfterSelect(relic -> {
                    if (Lupa.class.isAssignableFrom(getOwnerFromRelic(relic))) {
                        characterInfo.gold = Lupa.characterInfo.gold;
                        Tzeentch.isCharacterInfoChanged = true;
                    }
                    if (Maso.class.isAssignableFrom(getOwnerFromRelic(relic))) {
                        characterInfo.gold = Maso.characterInfo.gold;
                        Tzeentch.isCharacterInfoChanged = true;
                    }
                });

        DEVABODY_RELIC_Selection_UI = new RelicSelectionUI(
                Relic_Selection_X + STARTER_RELIC_Selection_UI.getTotalWidth(),
                Relic_Selection_Y,
                Arrays.stream(new AbstractRelic[]{new DevaBody_Lupa(), new DevaBody_Masochism()}).collect(Collectors.toList()),
                new PowerTip(TezeentchCharacterStrings.TEXT[4], TezeentchCharacterStrings.TEXT[5]))
                .setRefreshAfterSelect(relic -> {
                    if (Lupa.class.isAssignableFrom(getOwnerFromRelic(relic))) {
                        characterInfo.currentHp = Lupa.characterInfo.currentHp;
                        characterInfo.maxHp = Lupa.characterInfo.maxHp;
                        Tzeentch.isCharacterInfoChanged = true;
                    }
                    if (Maso.class.isAssignableFrom(getOwnerFromRelic(relic))) {
                        characterInfo.currentHp = Maso.characterInfo.currentHp;
                        characterInfo.maxHp = Maso.characterInfo.maxHp;
                        Tzeentch.isCharacterInfoChanged = true;
                    }
                });

        SEXUAL_HEAT_RELIC_Selection_UI = new RelicSelectionUI(
                Relic_Selection_X + STARTER_RELIC_Selection_UI.getTotalWidth() + DEVABODY_RELIC_Selection_UI.getTotalWidth(),
                Relic_Selection_Y,
                Arrays.stream(new AbstractRelic[]{new Sensitive(), new MasochismMode()}).collect(Collectors.toList()),
                new PowerTip(TezeentchCharacterStrings.TEXT[6], TezeentchCharacterStrings.TEXT[7]));
    }

    public Tzeentch(String name) {
        super(ID, name, TZEENTCH_Character);
    }

    private static Class<? extends BaseCharacter> getOwnerFromRelic(AbstractRelic relic) {
        if (relic instanceof DevaBody_Lupa || relic instanceof Sensitive || relic instanceof StartWithSexToy) {
            return Lupa.class;
        }
        if (relic instanceof DevaBody_Masochism || relic instanceof MasochismMode || relic instanceof DoubleBlockWithVulnerable) {
            return Maso.class;
        }
        return Lupa.class;
    }

    @Override
    // 初始遗物
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
//        retVal.add(StartWithSexToy.ID);
//        retVal.add(JokeDescription.ID);
//        retVal.add(DevaBody_Lupa.ID);
//        retVal.add(Sensitive.ID);
//        retVal.add(SorM.ID);
        return retVal;
    }

    private void refreshInit() {
        // 初始化你的人物，如果你的人物只有一张图，那么第一个参数填写你人物图片的路径。
        this.initializeClass(
                LUPA_CHARACTER, // 人物图片
                LUPA_CHARACTER_SHOULDER_2, LUPA_CHARACTER_SHOULDER_1, LUPA_CORPSE_IMAGE, // 人物死亡图像
                getLoadout(), 0.0F, 0.0F, 250.0F, 375.0F, // 人物碰撞箱大小，越大的人物模型这个越大
                new EnergyManager(3) // 初始每回合的能量
        );
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(TezeentchCharacterStrings.NAMES[0], // 人物名字
                TezeentchCharacterStrings.TEXT[0], // 人物介绍
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
        ArrayList<String> startingDeck = new ArrayList<>();
        Logger.run("Begin loading starter Deck Strings");
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

    // 你的卡牌颜色（这个枚举在最下方创建）
    @Override
    public AbstractCard.CardColor getCardColor() {
        return DataManager.SPTT_DATA.MasoEnums.MASO_CARD;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Tzeentch(this.name);
    }

    @Override
    protected boolean cardFilter(AbstractCard card) {
        return CardOwnerPlayerManager.isLupaCard(card) || CardOwnerPlayerManager.isMasoCard(card);
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public void initPostDungeonInitialize() {
        STARTER_RELIC_Selection_UI.getSelectRelic().makeCopy().instantObtain();
        addAsInfoBlight(new JokeDescription());
        addAsInfoBlight(DEVABODY_RELIC_Selection_UI.getSelectRelic());
        addAsInfoBlight(SEXUAL_HEAT_RELIC_Selection_UI.getSelectRelic());
        if (Maso.class.isAssignableFrom(getOwnerFromRelic(DEVABODY_RELIC_Selection_UI.getSelectRelic()))) {
            Maso.setUpMaso();
        }
    }

//    public static RelicSelect relicSelect;

    @Override
    public void renderInCharacterSelectScreen(CharacterOption characterOption, SpriteBatch sb) {
        STARTER_RELIC_Selection_UI.render(sb);
        DEVABODY_RELIC_Selection_UI.render(sb);
        SEXUAL_HEAT_RELIC_Selection_UI.render(sb);
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        super.doCharSelectScreenSelectEffect();
        Tzeentch.isCharacterInfoChanged = true;
    }

    @Override
    public void updateInCharacterSelectScreen(CharacterOption characterOption) {
        if (!SuperstitioConfig.isEnableGuroCharacter()) {
            CardCrawlGame.mainMenuScreen.charSelectScreen.confirmButton.isDisabled = true;
            if (CardCrawlGame.mainMenuScreen.charSelectScreen.confirmButton.isHovered){
                TipsUtility.renderTipsWithMouse(GuroTip);
            }
        }


        STARTER_RELIC_Selection_UI.update();
        DEVABODY_RELIC_Selection_UI.update();
        SEXUAL_HEAT_RELIC_Selection_UI.update();
//        if (InputHelper.justClickedLeft || InputHelper.justClickedRight)
//            Tzeentch.isCharacterInfoChanged = true;
        if (Tzeentch.isCharacterInfoChanged) {
            Tzeentch.isCharacterInfoChanged = false;
            refreshInit();
            ReflectionHacks.setPrivate(characterOption, CharacterOption.class, "hp", characterInfo.makeHpString());
            ReflectionHacks.setPrivate(characterOption, CharacterOption.class, "gold", gold);
            if (Lupa.class.isAssignableFrom(getOwnerFromRelic(DEVABODY_RELIC_Selection_UI.getSelectRelic())))
                CardCrawlGame.mainMenuScreen.charSelectScreen.bgCharImg = ImageMaster.loadImage(BaseMod.playerPortraitMap.get(LUPA_Character));
            if (Maso.class.isAssignableFrom(getOwnerFromRelic(DEVABODY_RELIC_Selection_UI.getSelectRelic())))
                CardCrawlGame.mainMenuScreen.charSelectScreen.bgCharImg = ImageMaster.loadImage(BaseMod.playerPortraitMap.get(MASO_Character));
        }
    }
}