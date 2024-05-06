package superstitio;

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
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.characters.Lupa;
import superstitio.customStrings.*;
import superstitio.relics.AbstractLupaRelic;

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
    public DataManager data;

    public SuperstitioModSetup() {
        BaseMod.subscribe(this);
        SuperstitioModSetup.theDefaultDefaultSettings.setProperty(ENABLE_NSFW_STRING, "TRUE");
        try {
            SuperstitioModSetup.config = new SpireConfig(DataManager.getModID(), DataManager.getModID() + "Config",
                    SuperstitioModSetup.theDefaultDefaultSettings);
            SuperstitioModSetup.config.load();
            SuperstitioModSetup.enableSFW = SuperstitioModSetup.config.getBool(ENABLE_NSFW_STRING);
        } catch (Exception e) {
            Logger.error(e);
        }

        // 这里注册颜色
        data = new DataManager();
        BaseMod.addColor(LupaEnums.LUPA_CARD,
                DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR,
                DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR,
                DataManager.LUPA_DATA.LUPA_COLOR,
                data.lupaData.BG_ATTACK_512, data.lupaData.BG_SKILL_512, data.lupaData.BG_POWER_512,
                data.lupaData.ENERGY_ORB,
                data.lupaData.BG_ATTACK_1024, data.lupaData.BG_SKILL_1024, data.lupaData.BG_POWER_1024,
                data.lupaData.BIG_ORB, data.lupaData.SMALL_ORB);

        BaseMod.addColor(TempCardEnums.LUPA_TempCard_CARD,
                DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR,
                DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR,
                DataManager.LUPA_DATA.LUPA_COLOR,
                data.lupaData.BG_ATTACK_512, data.lupaData.BG_SKILL_512, data.lupaData.BG_POWER_512,
                data.lupaData.ENERGY_ORB,
                data.lupaData.BG_ATTACK_1024, data.lupaData.BG_SKILL_1024, data.lupaData.BG_POWER_1024,
                data.lupaData.BIG_ORB, data.lupaData.SMALL_ORB);

        Logger.run("Done " + this + " subscribing");
        Logger.run("Adding mod settings");
    }

    public static void initialize() {
        new SuperstitioModSetup();
    }

    private static void setUpModOptions() {
        Logger.run("Loading badge image and mod options");
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
                .packageFilter(AbstractLupaRelic.class)
                .any(CustomRelic.class, (info, relic) -> {
                    BaseMod.addRelicToCustomPool(relic, LupaEnums.LUPA_CARD);
//                    if (info.seen) {
                    UnlockTracker.markRelicAsSeen(relic.relicId);
//                    }
                });
    }

    @Override
    public void receiveEditStrings() {
        Logger.run("Beginning to edit strings for mod with ID: " + DataManager.getModID());
        DataManager.loadCustomStringsFile("card_Lupa", DataManager.cards, CardStringsWithFlavorSet.class);
        DataManager.loadCustomStringsFile("modifier_damage", DataManager.modifiers, ModifierStringsSet.class);
        DataManager.loadCustomStringsFile("modifier_block", DataManager.modifiers, ModifierStringsSet.class);
        DataManager.loadCustomStringsFile("power", DataManager.powers, PowerStringsSet.class);
        DataManager.loadCustomStringsFile("orb", DataManager.orbs, OrbStringsSet.class);
//        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocPath(Settings.language,"event"));
//        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocPath(Settings.language,"potion"));
//        BaseMod.loadCustomStringsFile(OrbStrings.class, makeLocPath(Settings.language,"orb"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                DataManager.makeLocalizationPath(Settings.language, enableSFW ? "character_LupaSFW" : "character_Lupa"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, DataManager.makeLocalizationPath(Settings.language, "relic_Lupa"));
        BaseMod.loadCustomStringsFile(UIStrings.class, DataManager.makeLocalizationPath(Settings.language, "UIStrings"));
        if (enableSFW) {
            MakeSFWWord();
            SFWWordReplace();
        }
        Logger.run("Done editing strings");
    }

    private void MakeSFWWord() {
        List<WordReplace> wordReplaces = makeWordReplaceRule();

        DataManager.cards.forEach((string, card) -> card.setupSFWStringByWordReplace(wordReplaces));
        DataManager.powers.forEach(((string, power) -> power.setupSFWStringByWordReplace(wordReplaces)));
        DataManager.modifiers.forEach(((string, modifier) -> modifier.setupSFWStringByWordReplace(wordReplaces)));
        DataManager.orbs.forEach(((string, orb) -> orb.setupSFWStringByWordReplace(wordReplaces)));

    }

    private void SFWWordReplace() {
        Map<String, RelicStrings> relicsStrings = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "relics");

        List<WordReplace> wordReplaces = makeWordReplaceRule();
        relicsStrings.forEach((s, Strings) -> {
            if (!s.contains(DataManager.getModID().toLowerCase())) return;
            Strings.FLAVOR = "";
            wordReplaces.forEach(wordReplace -> DataManager.replaceStringsInObj(Strings, wordReplace));
        });

        ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "relics", relicsStrings);
    }

    private List<WordReplace> makeWordReplaceRule() {
        List<WordReplace> sfwReplaces =
                Arrays.stream(DataManager.makeJsonStringFromFile("SFW_replace", WordReplace[].class)).collect(Collectors.toList());
        if (DataManager.cards != null && !DataManager.cards.isEmpty())
            sfwReplaces.addAll(CardStringsWithFlavorSet.makeCardNameReplaceRules(new ArrayList<>(DataManager.cards.values())));
        if (DataManager.modifiers != null && !DataManager.modifiers.isEmpty())
            sfwReplaces.addAll(ModifierStringsSet.makeModifierNameReplaceRules(new ArrayList<>(DataManager.modifiers.values())));
        return sfwReplaces.stream().filter(wordReplace -> !wordReplace.hasNullOrEmpty()).collect(Collectors.toList());
    }

    @Override
    public void receiveEditKeywords() {
        List<Keyword> keywords = new ArrayList<>();
        keywords.addAll(Arrays.asList(DataManager.makeJsonStringFromFile("keyword", Keyword[].class)));
        keywords.addAll(Arrays.asList(ModifierStringsSet.MakeKeyWords()));

        keywords.forEach(keyword ->
                BaseMod.addKeyword(DataManager.getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION));

        List<Keyword> keywordsSFW = new ArrayList<>();
        keywordsSFW.addAll(Arrays.asList(DataManager.makeJsonStringFromFile("keyword", Keyword[].class)));
        keywordsSFW.addAll(Arrays.asList(ModifierStringsSet.MakeKeyWords()));

        replaceKeyWordsToSFW(keywordsSFW.toArray(new Keyword[0]));

        keywordsSFW.forEach(keyword ->
                BaseMod.addKeyword(DataManager.getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION));
    }

    private void replaceKeyWordsToSFW(Keyword[] keywords) {
        for (WordReplace wordReplace : makeWordReplaceRule())
            Arrays.stream(keywords).forEach(keyword -> DataManager.replaceStringsInObj(keyword, wordReplace));
    }

    @Override
    public void receivePostInitialize() {
//        CustomTargeting.registerCustomTargeting(SelfOrEnemyTargeting.SELF_OR_ENEMY, new SelfOrEnemyTargeting());
        setUpModOptions();
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
