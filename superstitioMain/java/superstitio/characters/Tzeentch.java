package superstitio.characters;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
import superstitio.characters.cardpool.CardPoolManager;
import superstitio.characters.cardpool.MasoCardPool;
import superstitio.relics.a_starter.VulnerableTogetherRelic;
import superstitio.relics.a_starter.StartWithSexToy;
import superstitio.relics.blight.*;
import superstitioapi.player.PlayerInitPostDungeonInitialize;
import superstitioapi.renderManager.characterSelectScreenRender.RelicSelectionUI;
import superstitioapi.renderManager.characterSelectScreenRender.RenderInCharacterSelect;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static superstitio.DataManager.SPTT_DATA.LupaEnums.LUPA_Character;
import static superstitio.DataManager.SPTT_DATA.MasoEnums.MASO_Character;
import static superstitio.DataManager.SPTT_DATA.TzeentchEnums.TZEENTCH_Character;
import static superstitioapi.relicToBlight.InfoBlight.addAsInfoBlight;

//
public class Tzeentch extends BaseCharacter implements PlayerInitPostDungeonInitialize, RenderInCharacterSelect {
    public static final String ID = DataManager.MakeTextID(Tzeentch.class.getSimpleName());
    public static final CharacterStrings TezeentchCharacterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    public static final float Relic_Selection_Y = (float) Settings.HEIGHT / 2 - 100 * Settings.yScale;
    public static final RelicSelectionUI STARTER_RELIC_Selection_UI;
    public static final RelicSelectionUI DEVABODY_RELIC_Selection_UI;
    public static final RelicSelectionUI SEXUAL_HEAT_RELIC_Selection_UI;
    public static final float Relic_Selection_X = 240 * Settings.xScale;
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
                Arrays.stream(new AbstractRelic[]{new StartWithSexToy(), new VulnerableTogetherRelic()}).collect(Collectors.toList()),
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

        TzeentchSave.loadConfig();
    }

    public Tzeentch(String name) {
        super(ID, name, TZEENTCH_Character);
    }

    private static Class<? extends BaseCharacter> getOwnerFromRelic(AbstractRelic relic) {
        if (relic instanceof DevaBody_Lupa || relic instanceof Sensitive || relic instanceof StartWithSexToy) {
            return Lupa.class;
        }
        if (relic instanceof DevaBody_Masochism || relic instanceof MasochismMode || relic instanceof VulnerableTogetherRelic) {
            return Maso.class;
        }
        return Lupa.class;
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
        Logger.run("Begin loading starter Deck Strings");
        if (Lupa.class.isAssignableFrom(getOwnerFromRelic(DEVABODY_RELIC_Selection_UI.getSelectRelic())))
            return Lupa.LupaStartDeck();
        if (Maso.class.isAssignableFrom(getOwnerFromRelic(DEVABODY_RELIC_Selection_UI.getSelectRelic())))
            return Maso.MasoStartDeck();
        return Lupa.LupaStartDeck();
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    @Override
    public AbstractCard.CardColor getCardColor() {
        return DataManager.SPTT_DATA.MasoEnums.MASO_CARD;
    }

    @Override
    public ArrayList<AbstractCard> getCardPool(ArrayList<AbstractCard> tmpPool) {
        addCardByCardFilter(tmpPool);
        return tmpPool;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Tzeentch(this.name);
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public void initPostDungeonInitialize() {
        TzeentchSave.saveConfig();
        STARTER_RELIC_Selection_UI.getSelectRelic().makeCopy().instantObtain();
        addAsInfoBlight(new JokeDescription());
        addAsInfoBlight(DEVABODY_RELIC_Selection_UI.getSelectRelic());
        addAsInfoBlight(SEXUAL_HEAT_RELIC_Selection_UI.getSelectRelic());
        if (Maso.class.isAssignableFrom(getOwnerFromRelic(DEVABODY_RELIC_Selection_UI.getSelectRelic()))) {
            Maso.setUpMaso();
        }
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        super.doCharSelectScreenSelectEffect();
        Tzeentch.isCharacterInfoChanged = true;
    }

    @Override
    public void renderInCharacterSelectScreen(CharacterOption characterOption, SpriteBatch sb) {
        STARTER_RELIC_Selection_UI.render(sb);
        DEVABODY_RELIC_Selection_UI.render(sb);
        SEXUAL_HEAT_RELIC_Selection_UI.render(sb);
        CardPoolManager.instance.render(sb);
    }

    @Override
    public void updateInCharacterSelectScreen(CharacterOption characterOption) {
        updateIsUnableByGuroSetting(CardPoolManager.instance.cardPools.stream().anyMatch(baseCardPool -> baseCardPool instanceof MasoCardPool && baseCardPool.getIsSelect()));
        CardPoolManager.instance.update();
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
            TzeentchSave.saveConfig();
        }
    }
//    public static RelicSelect relicSelect;

    @Override
    protected boolean isCardCanAdd(AbstractCard card) {
        return CardPoolManager.instance.getAddedCard().test(card) && !CardPoolManager.instance.getBanedCard().test(card);
    }

    public static class TzeentchSave implements java.io.Serializable {
        private static final String TzeentchSave_STRING = "TzeentchSave";
        public static SpireConfig config = null;
        public static Properties theDefaultSettings = new Properties();
        public static Gson saveFileGson = new Gson();
        public String starterRelicId;
        public String devaBodyRelicId;
        public String sexualHeatRelicId;
        public HashMap<String, Boolean> cardPoolId_IsSelect;

        public TzeentchSave(String starterRelicId, String devaBodyRelicId, String sexualHeatRelicId, HashMap<String, Boolean> cardPoolId_IsSelect) {
            this.starterRelicId = starterRelicId;
            this.devaBodyRelicId = devaBodyRelicId;
            this.sexualHeatRelicId = sexualHeatRelicId;
            this.cardPoolId_IsSelect = cardPoolId_IsSelect;
        }

        public static void loadConfig() {
            theDefaultSettings.setProperty(TzeentchSave_STRING, "");
            try {
                config = new SpireConfig(DataManager.getModID() + TzeentchSave.class.getSimpleName(),
                        DataManager.getModID() + TzeentchSave.class.getSimpleName() + "Config",
                        theDefaultSettings);
                config.load();
                String tzeentchString = config.getString(TzeentchSave_STRING);
                TzeentchSave tzeentchSave = saveFileGson.fromJson(tzeentchString, TzeentchSave.class);
                onLoad(tzeentchSave);
            } catch (Exception e) {
                Logger.error(e);
            }
        }

        public static void saveConfig() {
            String tzeentchString = saveFileGson.toJsonTree(onSave(), TzeentchSave.class).toString();
            config.setString(TzeentchSave_STRING, tzeentchString);
            try {
                config.save();
            } catch (IOException e) {
                Logger.error(e);
            }
        }

        private static JsonElement onSaveRaw() {
            return saveFileGson.toJsonTree(onSave());
        }

        private static void onLoadRaw(JsonElement value) {
            if (value != null) {
                TzeentchSave parsed = saveFileGson.fromJson(value, TzeentchSave.class);
                onLoad(parsed);
            } else {
                onLoad(null);
            }

        }

        private static TzeentchSave onSave() {
            HashMap<String, Boolean> cardPoolData = new HashMap<>();
            CardPoolManager.instance.cardPools.forEach(cardPool -> cardPoolData.put(cardPool.getId(), cardPool.getIsSelect()));

            return new TzeentchSave(
                    STARTER_RELIC_Selection_UI.getSelectRelic().relicId,
                    DEVABODY_RELIC_Selection_UI.getSelectRelic().relicId,
                    SEXUAL_HEAT_RELIC_Selection_UI.getSelectRelic().relicId,
                    cardPoolData
            );
        }

        private static void onLoad(TzeentchSave tzeentchSave) {
            if (tzeentchSave == null) return;
            STARTER_RELIC_Selection_UI.setSelectRelic(tzeentchSave.starterRelicId);
            DEVABODY_RELIC_Selection_UI.setSelectRelic(tzeentchSave.devaBodyRelicId);
            SEXUAL_HEAT_RELIC_Selection_UI.setSelectRelic(tzeentchSave.sexualHeatRelicId);
            tzeentchSave.cardPoolId_IsSelect.forEach((cardPoolId, isSelect) -> {
                CardPoolManager.instance.cardPools.forEach(cardPool -> {
                    if (Objects.equals(cardPool.getId(), cardPoolId))
                        cardPool.setIsSelect(isSelect);
                });
            });
        }
    }

}