package SuperstitioMod;

import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.characters.Lupa;
import SuperstitioMod.customStrings.CardStringsWithSFWAndFlavor;
import SuperstitioMod.customStrings.PowerStringsWithSFW;
import SuperstitioMod.relics.Sensitive;
import basemod.*;
import basemod.abstracts.CustomRelic;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.*;
import java.util.stream.Collectors;

@SpireInitializer
public class SuperstitioModSetup implements
        EditStringsSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditKeywordsSubscriber,
        EditCharactersSubscriber, AddAudioSubscriber, PostInitializeSubscriber {

    public static final String MOD_NAME = "Superstitio";
    private static final String ENABLE_NSFW_STRING = "enableSFW";
    public static boolean enableSFW = false;
    public static SpireConfig config = null;
    public static Properties theDefaultDefaultSettings = new Properties();
    public DataManager data = new DataManager();

    public SuperstitioModSetup() {
        BaseMod.subscribe(this);
        SuperstitioModSetup.theDefaultDefaultSettings.setProperty(ENABLE_NSFW_STRING, "TRUE");
        try {
            SuperstitioModSetup.config = new SpireConfig(DataManager.getModID(), DataManager.getModID() + "Config", SuperstitioModSetup.theDefaultDefaultSettings);
            SuperstitioModSetup.config.load();
            SuperstitioModSetup.enableSFW = SuperstitioModSetup.config.getBool(ENABLE_NSFW_STRING);
        } catch (Exception e) {
            Logger.error(e);
        }

        // 这里注册颜色
        BaseMod.addColor(LupaEnums.LUPA_CARD,
                DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR,
                data.lupaData.BG_ATTACK_512, data.lupaData.BG_SKILL_512, data.lupaData.BG_POWER_512,
                data.lupaData.ENERGY_ORB,
                data.lupaData.BG_ATTACK_1024, data.lupaData.BG_SKILL_1024, data.lupaData.BG_POWER_1024,
                data.lupaData.BIG_ORB, data.lupaData.SMALL_ORB);

        BaseMod.addColor(TempCardEnums.LUPA_TempCard_CARD,
                DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR,
                data.lupaData.BG_ATTACK_512, data.lupaData.BG_SKILL_512, data.lupaData.BG_POWER_512,
                data.lupaData.ENERGY_ORB,
                data.lupaData.BG_ATTACK_1024, data.lupaData.BG_SKILL_1024, data.lupaData.BG_POWER_1024,
                data.lupaData.BIG_ORB, data.lupaData.SMALL_ORB);

        Logger.info("Done subscribing");
        Logger.info("Adding mod settings");
    }

    public static void initialize() {
        new SuperstitioModSetup();
    }

    @Override
    public void receiveEditCharacters() {
        //添加角色到MOD中
        BaseMod.addCharacter(new Lupa(CardCrawlGame.playerName), data.lupaData.LUPA_CHARACTER_BUTTON, data.lupaData.LUPA_CHARACTER_PORTRAIT,
                LupaEnums.LUPA_Character);
    }

    @Override
    public void receiveEditCards() {
        //将卡牌添加
        new AutoAdd(MOD_NAME.toLowerCase())
                .packageFilter(AbstractLupaCard.class)
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveAddAudio() {
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(MOD_NAME.toLowerCase())
                .packageFilter(Sensitive.class)
                .any(CustomRelic.class, (info, relic) -> {
                    BaseMod.addRelicToCustomPool(relic, LupaEnums.LUPA_CARD);
                    if (info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    @Override
    public void receiveEditStrings() {
        Logger.info("Beginning to edit strings for mod with ID: " + DataManager.getModID());
        DataManager.loadCustomStringsFile("card_Lupa", DataManager.cards, CardStringsWithSFWAndFlavor.class);
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                DataManager.makeLocalizationPath(Settings.language, enableSFW ? "character_LupaSFW" : "character_Lupa"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, DataManager.makeLocalizationPath(Settings.language, "relic_Lupa"));
        DataManager.loadCustomStringsFile("power", DataManager.powers, PowerStringsWithSFW.class);
//        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocPath(Settings.language,"event"));
//        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocPath(Settings.language,"potion"));
//        BaseMod.loadCustomStringsFile(OrbStrings.class, makeLocPath(Settings.language,"orb"));
        BaseMod.loadCustomStringsFile(UIStrings.class, DataManager.makeLocalizationPath(Settings.language, "UIStrings"));
        if (enableSFW) {
            MakeSFWWord();
            SFWWordReplace();
        }
        Logger.info("Done editing strings");
    }

    private void MakeSFWWord() {
        List<WordReplace> wordReplaces = makeWordReplaceRule();

        for (WordReplace wordReplace : wordReplaces) {
            DataManager.cards.forEach((string, card) -> card.setupSFWStringByWordReplace(wordReplace));
            DataManager.powers.forEach(((string, power) -> power.setupSFWStringByWordReplace(wordReplace)));
        }
    }

    private void SFWWordReplace() {
        Map<String, RelicStrings> relicsStrings = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "relics");

        List<WordReplace> wordReplaces = makeWordReplaceRule();

        for (WordReplace wordReplace : wordReplaces)
            relicsStrings.forEach((s, Strings) -> DataManager.replaceStringsInObj(Strings, wordReplace));

        ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "relics", relicsStrings);
    }

    private List<WordReplace> makeWordReplaceRule() {
        List<WordReplace> sfwReplaces = Arrays.stream(DataManager.makeJsonStringFromFile("SFW_replace", WordReplace[].class)).collect(Collectors.toList());
        if (DataManager.cards != null && !DataManager.cards.isEmpty())
            sfwReplaces.addAll(CardStringsWithSFWAndFlavor.makeCardNameReplaceRules(new ArrayList<>(DataManager.cards.values())));
        return sfwReplaces;
    }

    @Override
    public void receiveEditKeywords() {
        Keyword[] keywords = DataManager.makeJsonStringFromFile("keyword", Keyword[].class);

        Arrays.stream(keywords).forEach(keyword ->
                BaseMod.addKeyword(DataManager.getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION));

        Keyword[] keywordsSFW = DataManager.makeJsonStringFromFile("keyword", Keyword[].class);

        replaceKeyWordsToSFW(keywordsSFW);

        Arrays.stream(keywordsSFW).forEach(keyword ->
                BaseMod.addKeyword(DataManager.getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION));
    }

    private void replaceKeyWordsToSFW(Keyword[] keywords) {
        for (WordReplace wordReplace : makeWordReplaceRule())
            Arrays.stream(keywords).forEach(keyword -> DataManager.replaceStringsInObj(keyword, wordReplace));
    }

    @Override
    public void receivePostInitialize() {
        Logger.info("Loading badge image and mod options");
        final Texture badgeTexture = ImageMaster.loadImage(DataManager.makeImgFilesPath_UI("ModIcon"));
        final ModPanel settingsPanel = new ModPanel();
        float settingXPos = 350.0f;
        float settingYPos = 750.0f;
        final float lineSpacing = 50.0f;
        final UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(DataManager.MakeTextID("OptionsMenu"));
        final String[] SettingText = UIStrings.TEXT;

        settingsPanel.addUIElement(new ModLabeledToggleButton(SettingText[0], settingXPos, settingYPos,
                Settings.CREAM_COLOR, FontHelper.charDescFont, SuperstitioModSetup.enableSFW, settingsPanel, label -> {
        }, button -> {
            SuperstitioModSetup.enableSFW = button.enabled;
            try {
                SuperstitioModSetup.config.setBool(ENABLE_NSFW_STRING, SuperstitioModSetup.enableSFW);
                SuperstitioModSetup.config.save();
            } catch (Exception e) {
                Logger.error(e);
            }
        }));

        settingYPos -= 3 * lineSpacing;

        settingsPanel.addUIElement(new ModLabeledButton(SettingText[1], settingXPos, settingYPos
                , settingsPanel, button -> {
//            CardLibrary.cards.values().removeIf(card -> card instanceof AbstractLupaCard);
//            receiveEditCards();
//            SFWWordReplace();
        }));
        BaseMod.registerModBadge(badgeTexture, MOD_NAME + " Mod", "Creeper_of_Fire", "A NSFW mod", settingsPanel);
    }

    // 为原版人物枚举、卡牌颜色枚举扩展的枚举，需要写，接下来要用
    public static class LupaEnums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass LUPA_Character;

        @SpireEnum(name = "LUPA_PINK")
        public static AbstractCard.CardColor LUPA_CARD;

        @SpireEnum(name = "LUPA_PINK")
        public static CardLibrary.LibraryType LUPA_LIBRARY;
    }

    // 为原版人物枚举、卡牌颜色枚举扩展的枚举，需要写，接下来要用
    public static class TempCardEnums {
        @SpireEnum(name = "LUPA_TEMP")
        public static AbstractCard.CardColor LUPA_TempCard_CARD;

        @SpireEnum(name = "LUPA_TEMP")
        public static CardLibrary.LibraryType LUPA_TempCard_LIBRARY;
    }
}
