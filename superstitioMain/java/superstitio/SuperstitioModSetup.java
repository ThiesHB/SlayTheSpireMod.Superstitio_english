package superstitio;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import superstitio.cards.SuperstitioCard;
import superstitio.characters.Lupa;
import superstitio.characters.Maso;
import superstitio.customStrings.*;
import superstitio.customStrings.interFace.WordReplace;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.relicToBlight.InfoBlight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.unlock.UnlockTracker.relicSeenPref;
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
    public DataManager data;
    List<WordReplace> wordReplaceRules;

    public SuperstitioModSetup() {
        BaseMod.subscribe(this);
        SuperstitioConfig.loadConfig();

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

    @Override
    public void receiveEditCharacters() {
        //添加角色到MOD中
        BaseMod.addCharacter(new Maso(CardCrawlGame.playerName),
                data.spttData.LUPA_CHARACTER_BUTTON, data.spttData.MASO_CHARACTER_PORTRAIT, MASO_Character);
        BaseMod.addCharacter(new Lupa(CardCrawlGame.playerName),
                data.spttData.LUPA_CHARACTER_BUTTON, data.spttData.LUPA_CHARACTER_PORTRAIT, LUPA_Character);

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
                .packageFilter(SuperstitioRelic.class)
                .any(CustomRelic.class, (info, relic) -> {
                    if (relic instanceof InfoBlight.BecomeInfoBlight) {
                        InfoBlight.initInfoBlight(relic);
                        relicSeenPref.putInteger(relic.relicId, 1);
                        relicSeenPref.flush();
                        relic.isSeen = true;
                        return;
                    }
                    BaseMod.addRelic(relic, RelicType.SHARED);
                    if (info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
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
        DataManager.loadCustomStringsFile("card_Colorless", DataManager.cards, CardStringsWithFlavorSet.class);
        DataManager.loadCustomStringsFile("modifier_damage", DataManager.modifiers, ModifierStringsSet.class);
        DataManager.loadCustomStringsFile("modifier_block", DataManager.modifiers, ModifierStringsSet.class);
        DataManager.loadCustomStringsFile("power", DataManager.powers, PowerStringsSet.class);
        DataManager.loadCustomStringsFile("power_Lupa", DataManager.powers, PowerStringsSet.class);
        DataManager.loadCustomStringsFile("power_Maso", DataManager.powers, PowerStringsSet.class);
        DataManager.loadCustomStringsFile("orb", DataManager.orbs, OrbStringsSet.class);
        DataManager.loadCustomStringsFile("UIStringsWithSFW", DataManager.uiStrings, UIStringsSet.class);
//        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocPath(Settings.language,"event"));
//        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocPath(Settings.language,"potion"));
//        BaseMod.loadCustomStringsFile(OrbStrings.class, makeLocPath(Settings.language,"orb"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                DataManager.makeLocalizationPath(Settings.language, SuperstitioConfig.isEnableSFW() ? "character_LupaSFW" : "character_Lupa"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, DataManager.makeLocalizationPath(Settings.language, "relic_Lupa"));
        BaseMod.loadCustomStringsFile(UIStrings.class, DataManager.makeLocalizationPath(Settings.language, "UIStrings"));
        BaseMod.loadCustomStringsFile(MonsterStrings.class, DataManager.makeLocalizationPath(Settings.language, "monsters"));
        if (SuperstitioConfig.isEnableSFW()) {
            makeSFWWordForStringsSet();
            makeSFWWordForOriginStrings();
        }
        Logger.run("Done editing strings");
    }

    private void makeSFWWordForStringsSet() {
        List<WordReplace> wordReplaces = makeWordReplaceRule();

        DataManager.cards.forEach((string, card) -> card.setupSFWStringByWordReplace(wordReplaces));
        DataManager.powers.forEach(((string, power) -> power.setupSFWStringByWordReplace(wordReplaces)));
        DataManager.modifiers.forEach(((string, modifier) -> modifier.setupSFWStringByWordReplace(wordReplaces)));
        DataManager.orbs.forEach(((string, orb) -> orb.setupSFWStringByWordReplace(wordReplaces)));

    }

    private void makeSFWWordForOriginStrings() {
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
        if (wordReplaceRules != null && !wordReplaceRules.isEmpty()) return wordReplaceRules;
        List<WordReplace> sfwReplaces =
                Arrays.stream(DataManager.makeJsonStringFromFile("SFW_replace", WordReplace[].class)).collect(Collectors.toList());
        if (DataManager.cards != null && !DataManager.cards.isEmpty())
            sfwReplaces.addAll(CardStringsWithFlavorSet.makeCardNameReplaceRules(new ArrayList<>(DataManager.cards.values())));
        if (DataManager.modifiers != null && !DataManager.modifiers.isEmpty())
            sfwReplaces.addAll(ModifierStringsSet.makeModifierNameReplaceRules(new ArrayList<>(DataManager.modifiers.values())));
        sfwReplaces.addAll(SuperstitioKeyWord.makeCardNameReplaceRules(getAndRegisterKeywordsFormFile()));
        wordReplaceRules = sfwReplaces.stream().filter(wordReplace -> !wordReplace.hasNullOrEmpty()).collect(Collectors.toList());
        return wordReplaceRules;
    }

    @Override
    public void receiveEditKeywords() {
        List<SuperstitioKeyWord> keywords = getAllKeywords();
        keywords.forEach(keyWord -> keyWord.makeSFWVersion(makeWordReplaceRule()));
        keywords.forEach(SuperstitioKeyWord::addToGame);

//        List<SuperstitioKeyWord> keywordsSFW = getKeywordsFormFile();


//        replaceKeyWordsToSFW(keywordsSFW.toArray(new SuperstitioKeyWord[0]));

//        keywordsSFW.forEach(SuperstitioKeyWord::addToGame);
    }

    private List<SuperstitioKeyWord> getAllKeywords() {
        List<SuperstitioKeyWord> keywordsSFW = new ArrayList<>();
        keywordsSFW.addAll(getAndRegisterKeywordsFormFile());
        keywordsSFW.addAll(Arrays.stream(ModifierStringsSet.MakeKeyWords()).map(SuperstitioKeyWord::STSLibKeyWordToThisType).collect(Collectors.toList()));
        return keywordsSFW;
    }

    private List<SuperstitioKeyWord> getAndRegisterKeywordsFormFile() {
        if (SuperstitioKeyWord.KeywordsFromFile.isEmpty()) {
            List<SuperstitioKeyWord> keywordsSFW = new ArrayList<>();
            keywordsSFW.addAll(Arrays.asList(DataManager.makeJsonStringFromFile("keyword", SuperstitioKeyWord[].class)));
            keywordsSFW.addAll(Arrays.asList(DataManager.makeJsonStringFromFile("keyword_Lupa", SuperstitioKeyWord[].class)));
            keywordsSFW.addAll(Arrays.asList(DataManager.makeJsonStringFromFile("keyword_Maso", SuperstitioKeyWord[].class)));
            keywordsSFW.forEach(SuperstitioKeyWord::registerKeywordFormFile);
        }
        return SuperstitioKeyWord.KeywordsFromFile;
    }

//    private void replaceKeyWordsToSFW(SuperstitioKeyWord[] keywords) {
//        for (WordReplace wordReplace : makeWordReplaceRule())
//            Arrays.stream(keywords).forEach(keyword -> DataManager.replaceStringsInObj(keyword, wordReplace));
//    }

    @Override
    public void receivePostInitialize() {
        SuperstitioConfig.setUpModOptions();
    }

}
