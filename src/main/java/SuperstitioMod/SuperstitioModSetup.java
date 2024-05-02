package SuperstitioMod;

import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.characters.Lupa;
import SuperstitioMod.customStrings.CardStringsWithSFWAndFlavor;
import SuperstitioMod.customStrings.PowerStringsWithSFW;
import SuperstitioMod.relics.Sensitive;
import basemod.*;
import basemod.abstracts.CustomRelic;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
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

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
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
    public static DataManager data = new DataManager();

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
        BaseMod.addColor(DataManager.LUPA_DATA.LupaEnums.LUPA_CARD,
                DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR, DataManager.LUPA_DATA.LUPA_COLOR,
                data.lupaData.BG_ATTACK_512, data.lupaData.BG_SKILL_512, data.lupaData.BG_POWER_512,
                data.lupaData.ENERGY_ORB,
                data.lupaData.BG_ATTACK_1024, data.lupaData.BG_SKILL_1024, data.lupaData.BG_POWER_1024,
                data.lupaData.BIG_ORB, data.lupaData.SMALL_ORB);

        BaseMod.addColor(DataManager.LUPA_DATA.TempCardEnums.LUPA_TempCard_CARD,
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

    private void replaceStringsInObj(Object obj, WordReplace wordReplace) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(obj) instanceof String) {
                    String string = (String) field.get(obj);
                    string = DataManager.replaceString(wordReplace, string);
                    field.set(obj, string);
                }
                else if (field.get(obj) instanceof String[]) {
                    String[] values = (String[]) field.get(obj);
                    if (values == null || values.length == 0)
                        continue;
                    String[] list = new String[values.length];
                    for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
                        String string = values[i];
                        String apply = DataManager.replaceString(wordReplace, string);
                        list[i] = apply;
                    }

                    field.set(obj, list);
                }
            } catch (IllegalAccessException e) {
                Logger.error(e);
            }
        }
    }

    @Override
    public void receiveEditCharacters() {
        //添加角色到MOD中
        BaseMod.addCharacter(new Lupa(CardCrawlGame.playerName), data.lupaData.LUPA_CHARACTER_BUTTON, data.lupaData.LUPA_CHARACTER_PORTRAIT,
                DataManager.LUPA_DATA.LupaEnums.LUPA_Character);
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
                    BaseMod.addRelicToCustomPool(relic, DataManager.LUPA_DATA.LupaEnums.LUPA_CARD);
                    if (info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    @Override
    public void receiveEditStrings() {
        Logger.info("Beginning to edit strings for mod with ID: " + DataManager.getModID());
        DataManager.loadCustomStringsFile("card_Lupa", DataManager.cards, CardStringsWithSFWAndFlavor.class);
        if (enableSFW)
            BaseMod.loadCustomStringsFile(CharacterStrings.class, DataManager.makeLocalizationPath(Settings.language, "character_LupaSFW"));
        else
            BaseMod.loadCustomStringsFile(CharacterStrings.class, DataManager.makeLocalizationPath(Settings.language, "character_Lupa"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, DataManager.makeLocalizationPath(Settings.language, "relic_Lupa"));
        DataManager.loadCustomStringsFile("power", DataManager.powers, PowerStringsWithSFW.class);
//        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocPath(Settings.language,"event"));
//        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocPath(Settings.language,"potion"));
//        BaseMod.loadCustomStringsFile(OrbStrings.class, makeLocPath(Settings.language,"orb"));
        BaseMod.loadCustomStringsFile(UIStrings.class, DataManager.makeLocalizationPath(Settings.language, "UIStrings"));
        MakeSFWWord();
        SFWWordReplace();
        Logger.info("Done editing strings");
    }

    private void MakeSFWWord() {
        if (!enableSFW) return;
        List<WordReplace> wordReplaces = makeWordReplaceRule();

        for (WordReplace wordReplace : wordReplaces) {
            DataManager.cards.forEach((string, card) -> card.setupSFWStringByWordReplace(wordReplace));
            DataManager.powers.forEach(((string, power) -> power.setupSFWStringByWordReplace(wordReplace)));
        }
    }

    private void SFWWordReplace() {
        if (!enableSFW) return;
        Map<String, RelicStrings> relicsStrings = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "relics");

        List<WordReplace> wordReplaces = makeWordReplaceRule();

        for (WordReplace wordReplace : wordReplaces) {
            relicsStrings.forEach((s, Strings) -> replaceStringsInObj(Strings, wordReplace));
        }

        ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "relics", relicsStrings);
    }

    private List<WordReplace> makeWordReplaceRule() {
        List<WordReplace> sfwReplaces = Arrays.stream(makeJsonStringFromFile(DataManager.makeLocalizationPath(Settings.language, "SFW_replace"), WordReplace[].class)).collect(Collectors.toList());
        if (DataManager.cards != null && !DataManager.cards.isEmpty())
            sfwReplaces.addAll(CardStringsWithSFWAndFlavor.makeCardNameReplaceRules(new ArrayList<>(DataManager.cards.values())));
        return sfwReplaces;
    }

    @Override
    public void receiveEditKeywords() {
        if (!enableSFW)
            changeDefaultKeyword();

        Keyword[] keywords = makeJsonStringFromFile(DataManager.makeLocalizationPath(Settings.language, "keyword"), Keyword[].class);
        if (keywords == null)
            return;

        replaceKeyWordsToSFW(keywords);

        Arrays.stream(keywords).forEach(keyword -> {
            BaseMod.addKeyword(DataManager.getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
        });
    }

    private void changeDefaultKeyword() {
        Keyword[] keywordsForDefault = makeJsonStringFromFile(DataManager.makeLocalizationPath(Settings.language, "keywordForDefault"), Keyword[].class);
        if (keywordsForDefault == null) return;
        for (Keyword keyword : keywordsForDefault) {
            GameDictionary.keywords.remove(keyword.PROPER_NAME);
            GameDictionary.keywords.put(keyword.PROPER_NAME, keyword.DESCRIPTION);
        }
    }

    private <T> T makeJsonStringFromFile(String filePath, Class<T> objectClass) {
        Gson gson = new Gson();
        String json = Gdx.files.internal(filePath)
                .readString(String.valueOf(StandardCharsets.UTF_8));
        return gson.fromJson(json, objectClass);
    }

    private void replaceKeyWordsToSFW(Keyword[] keywords) {
        if (!enableSFW)
            return;
        for (WordReplace wordReplace : makeWordReplaceRule()) {
            Arrays.stream(keywords).forEach(keyword -> replaceStringsInObj(keyword, wordReplace));
        }
    }

    @Override
    public void receivePostInitialize() {
        Logger.info("Loading badge image and mod options");
        final Texture badgeTexture = ImageMaster.loadImage(DataManager.makeImgFilesPath_UI("ModIcon"));
        final ModPanel settingsPanel = new ModPanel();
        float settingXPos = 350.0f;
        final float startingXPos = settingXPos;
        final float xSpacing = 250.0f;
        float settingYPos = 750.0f;
        final float lineSpacing = 50.0f;
        final UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(DataManager.MakeTextID("OptionsMenu"));
        final String[] SettingText = UIStrings.TEXT;
        final ModLabeledToggleButton enableSFWButton = new ModLabeledToggleButton(SettingText[0], settingXPos, settingYPos,
                Settings.CREAM_COLOR, FontHelper.charDescFont, SuperstitioModSetup.enableSFW, settingsPanel, label -> {
        }, button -> {
            SuperstitioModSetup.enableSFW = button.enabled;
            try {
                SuperstitioModSetup.config.setBool(ENABLE_NSFW_STRING, SuperstitioModSetup.enableSFW);
                SuperstitioModSetup.config.save();
            } catch (Exception e) {
                Logger.error(e);
            }
        });
        settingsPanel.addUIElement(enableSFWButton);


        settingYPos -= 3 * lineSpacing;
        final ModLabeledButton applySFWButton = new ModLabeledButton(SettingText[1], settingXPos, settingYPos
                , settingsPanel, button -> {
            CardLibrary.cards.values().removeIf(card -> card instanceof AbstractLupaCard);

            receiveEditRelics();
            receiveEditStrings();
            receiveEditKeywords();
        });
        settingsPanel.addUIElement(applySFWButton);
//        settingXPos = startingXPos;
//        settingYPos -= lineSpacing;
//        final ModLabeledToggleButton ignoreUnlocksButton = new ModLabeledToggleButton(SettingText[1], 350.0f, settingYPos, Settings.CREAM_COLOR,
//                FontHelper.charDescFont, LoadoutMod.ignoreUnlock, settingsPanel, label -> {
//        }, button -> {
//            LoadoutMod.ignoreUnlock = button.enabled;
//            try {
//                LoadoutMod.config.setBool("ignoreUnlockProgress", LoadoutMod.ignoreUnlock);
//                LoadoutMod.config.save();
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//            return;
//        });
//        settingsPanel.addUIElement((IUIElement) ignoreUnlocksButton);
//        settingYPos -= lineSpacing;
//        final ModLabeledToggleButton enableStarterPoolButton = new ModLabeledToggleButton(StringUtils.chop(RelicViewScreen.TEXT[1]), SettingText[2]
//                , settingXPos, settingYPos, Settings.CREAM_COLOR, FontHelper.charDescFont, LoadoutMod.enableStarterPool, settingsPanel, label -> {
//        }, button -> {
//            LoadoutMod.enableStarterPool = button.enabled;
//            try {
//                LoadoutMod.config.setBool("enableStarterPool", LoadoutMod.enableStarterPool);
//                LoadoutMod.config.save();
//            } catch (Exception e3) {
//                e3.printStackTrace();
//            }
//            return;
//        });
//        settingsPanel.addUIElement((IUIElement) enableStarterPoolButton);
//        settingXPos += xSpacing;
        BaseMod.registerModBadge(badgeTexture, MOD_NAME + " Mod", "Creeper_of_Fire", "A NSFW mod", settingsPanel);
    }

}
