package superstitio;

import basemod.*;
import basemod.abstracts.CustomRelic;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import superstitio.cards.SuperstitioCard;
import superstitio.characters.Lupa;
import superstitio.characters.Maso;
import superstitio.customStrings.*;
import superstitio.relics.AbstractLupaRelic;

import java.util.*;
import java.util.stream.Collectors;

import static superstitio.DataManager.SPTT_DATA.GeneralEnums.GENERAL_CARD;
import static superstitio.DataManager.SPTT_DATA.LupaEnums.LUPA_CARD;
import static superstitio.DataManager.SPTT_DATA.LupaEnums.LUPA_Character;
import static superstitio.DataManager.SPTT_DATA.MasoEnums.MASO_CARD;
import static superstitio.DataManager.SPTT_DATA.MasoEnums.MASO_Character;
import static superstitio.DataManager.SPTT_DATA.TempCardEnums.TempCard_CARD;

@SpireInitializer
public class SuperstitioModSetup implements
        EditStringsSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditKeywordsSubscriber,
        EditCharactersSubscriber, AddAudioSubscriber, PostInitializeSubscriber {

    public static final String MOD_NAME = "Superstitio";
    private static final String ENABLE_NSFW_STRING = "enableSFW";
    public static SpireConfig config = null;
    public static Properties theDefaultDefaultSettings = new Properties();
    private static boolean enableSFW = false;
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
        BaseMod.addColor(LUPA_CARD,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR,
                data.spttData.BG_ATTACK_512, data.spttData.BG_SKILL_512, data.spttData.BG_POWER_512,
                data.spttData.ENERGY_ORB,
                data.spttData.BG_ATTACK_1024, data.spttData.BG_SKILL_1024, data.spttData.BG_POWER_1024,
                data.spttData.BIG_ORB, data.spttData.SMALL_ORB);

        BaseMod.addColor(TempCard_CARD,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR,
                data.spttData.BG_ATTACK_512, data.spttData.BG_SKILL_512, data.spttData.BG_POWER_512,
                data.spttData.ENERGY_ORB,
                data.spttData.BG_ATTACK_1024, data.spttData.BG_SKILL_1024, data.spttData.BG_POWER_1024,
                data.spttData.BIG_ORB, data.spttData.SMALL_ORB);

        BaseMod.addColor(GENERAL_CARD,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR,
                data.spttData.BG_ATTACK_512, data.spttData.BG_SKILL_512, data.spttData.BG_POWER_512,
                data.spttData.ENERGY_ORB,
                data.spttData.BG_ATTACK_1024, data.spttData.BG_SKILL_1024, data.spttData.BG_POWER_1024,
                data.spttData.BIG_ORB, data.spttData.SMALL_ORB);

        BaseMod.addColor(MASO_CARD,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR, DataManager.SPTT_DATA.SEX_COLOR,
                DataManager.SPTT_DATA.SEX_COLOR,
                data.spttData.BG_ATTACK_512, data.spttData.BG_SKILL_512, data.spttData.BG_POWER_512,
                data.spttData.ENERGY_ORB,
                data.spttData.BG_ATTACK_1024, data.spttData.BG_SKILL_1024, data.spttData.BG_POWER_1024,
                data.spttData.BIG_ORB, data.spttData.SMALL_ORB);

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
                Settings.CREAM_COLOR, FontHelper.charDescFont, SuperstitioModSetup.getEnableSFW(), settingsPanel, label -> {
        }, button -> {
            SuperstitioModSetup.enableSFW = button.enabled;
            try {
                SuperstitioModSetup.config.setBool(ENABLE_NSFW_STRING, SuperstitioModSetup.getEnableSFW());
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

    public static boolean getEnableSFW() {
        return enableSFW;
    }

    @Override
    public void receiveEditCharacters() {
        //添加角色到MOD中
        BaseMod.addCharacter(new Lupa(CardCrawlGame.playerName),
                data.spttData.LUPA_CHARACTER_BUTTON, data.spttData.LUPA_CHARACTER_PORTRAIT, LUPA_Character);
//        if (getEnableSFW()) return;
        BaseMod.addCharacter(new Maso(CardCrawlGame.playerName),
                data.spttData.LUPA_CHARACTER_BUTTON, data.spttData.MASO_CHARACTER_PORTRAIT, MASO_Character);
    }

    @Override
    public void receiveEditCards() {
        //将卡牌添加
        new AutoAdd(MOD_NAME.toLowerCase())
                .packageFilter(SuperstitioCard.class)
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
                    BaseMod.addRelicToCustomPool(relic, LUPA_CARD);
//                    if (info.seen) {
                    UnlockTracker.markRelicAsSeen(relic.relicId);
//                    }
                });
    }

    @Override
    public void receiveEditStrings() {
        Logger.run("Beginning to edit strings for mod with ID: " + DataManager.getModID());
        DataManager.loadCustomStringsFile("card_Lupa", DataManager.cards, CardStringsWithFlavorSet.class);
        DataManager.loadCustomStringsFile("card_General", DataManager.cards, CardStringsWithFlavorSet.class);
        DataManager.loadCustomStringsFile("card_Maso", DataManager.cards, CardStringsWithFlavorSet.class);
        DataManager.loadCustomStringsFile("card_FuckJobAndBase", DataManager.cards, CardStringsWithFlavorSet.class);
        DataManager.loadCustomStringsFile("card_TempCard", DataManager.cards, CardStringsWithFlavorSet.class);
        DataManager.loadCustomStringsFile("modifier_damage", DataManager.modifiers, ModifierStringsSet.class);
        DataManager.loadCustomStringsFile("modifier_block", DataManager.modifiers, ModifierStringsSet.class);
        DataManager.loadCustomStringsFile("power", DataManager.powers, PowerStringsSet.class);
        DataManager.loadCustomStringsFile("power_Lupa", DataManager.powers, PowerStringsSet.class);
        DataManager.loadCustomStringsFile("power_Maso", DataManager.powers, PowerStringsSet.class);
        DataManager.loadCustomStringsFile("orb", DataManager.orbs, OrbStringsSet.class);
//        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocPath(Settings.language,"event"));
//        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocPath(Settings.language,"potion"));
//        BaseMod.loadCustomStringsFile(OrbStrings.class, makeLocPath(Settings.language,"orb"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                DataManager.makeLocalizationPath(Settings.language, getEnableSFW() ? "character_LupaSFW" : "character_Lupa"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, DataManager.makeLocalizationPath(Settings.language, "relic_Lupa"));
        BaseMod.loadCustomStringsFile(UIStrings.class, DataManager.makeLocalizationPath(Settings.language, "UIStrings"));
        if (getEnableSFW()) {
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
            if (s.contains(DataManager.getModID().toLowerCase()) || s.contains(DataManager.getModID())) {
                Strings.FLAVOR = "";
                wordReplaces.forEach(wordReplace -> DataManager.replaceStringsInObj(Strings, wordReplace));
            }
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
        List<Keyword> keywords = getKeywordsFormFile();

        keywords.forEach(keyword ->
                BaseMod.addKeyword(DataManager.getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION));

        List<Keyword> keywordsSFW = getKeywordsFormFile();


        replaceKeyWordsToSFW(keywordsSFW.toArray(new Keyword[0]));

        keywordsSFW.forEach(keyword ->
                BaseMod.addKeyword(DataManager.getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION));
    }

    private List<Keyword> getKeywordsFormFile() {
        List<Keyword> keywordsSFW = new ArrayList<>();
        keywordsSFW.addAll(Arrays.asList(DataManager.makeJsonStringFromFile("keyword", Keyword[].class)));
        keywordsSFW.addAll(Arrays.asList(DataManager.makeJsonStringFromFile("keyword_Lupa", Keyword[].class)));
        keywordsSFW.addAll(Arrays.asList(DataManager.makeJsonStringFromFile("keyword_Maso", Keyword[].class)));
        keywordsSFW.addAll(Arrays.asList(ModifierStringsSet.MakeKeyWords()));
        return keywordsSFW;
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

}
