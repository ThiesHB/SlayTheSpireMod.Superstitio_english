package superstitio.customStrings;

import superstitio.DataManager;
import superstitio.SuperstitioConfig;
import superstitio.customStrings.interFace.WordReplace;
import superstitio.customStrings.stringsSet.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static superstitio.customStrings.stringsSet.CardStringsWillMakeFlavorSet.makeCardNameReplaceRules;
import static superstitio.customStrings.stringsSet.ModifierStringsSet.MakeKeyWords;
import static superstitio.customStrings.stringsSet.ModifierStringsSet.makeModifierNameReplaceRules;

public class StringsSetManager {
    private static final String[] powerFiles = new String[]{"power", "power_Lupa", "power_Maso"};
    private static final String[] modifierFiles = new String[]{"modifier_damage", "modifier_block"};
    private static final String[] cardFiles = new String[]{"card_Lupa", "card_General", "card_Maso", "card_FuckJobAndBase", "card_TempCard",
            "card_Colorless"};
    private static List<WordReplace> wordReplaceRules;

    public static void loadModifierStringsSet() {
        for (String modifierFile : modifierFiles)
            DataManager.loadCustomStringsFile(modifierFile, DataManager.modifiers, ModifierStringsSet.class);
    }

    public static void loadPowerStringsSet() {
        for (String powerFile : powerFiles)
            DataManager.loadCustomStringsFile(powerFile, DataManager.powers, PowerStringsSet.class);
    }

    public static void loadCardStringsSet() {
        for (String cardFile : cardFiles)
            DataManager.loadCustomStringsFile(cardFile, DataManager.cards, CardStringsWillMakeFlavorSet.class);
    }

    public static void loadAllStrings() {
        loadCardStringsSet();
        loadModifierStringsSet();
        loadPowerStringsSet();
        loadOrbStringsSet();
        loadUIStringsSet();

        makeSFWVersion();
    }

    public static List<WordReplace> makeWordReplaceRule() {
        if (wordReplaceRules != null && !wordReplaceRules.isEmpty()) return wordReplaceRules;
        List<WordReplace> sfwReplaces =
                Arrays.stream(DataManager.makeJsonStringFromFile("SFW_replace", WordReplace[].class)).collect(Collectors.toList());
        if (DataManager.cards != null && !DataManager.cards.isEmpty())
            sfwReplaces.addAll(makeCardNameReplaceRules(new ArrayList<>(DataManager.cards.values())));
        if (DataManager.modifiers != null && !DataManager.modifiers.isEmpty())
            sfwReplaces.addAll(makeModifierNameReplaceRules(new ArrayList<>(DataManager.modifiers.values())));
        sfwReplaces.addAll(SuperstitioKeyWord.makeKeywordNameReplaceRules(SuperstitioKeyWord.getAndRegisterKeywordsFormFile()));
        wordReplaceRules = sfwReplaces.stream().filter(wordReplace -> !wordReplace.hasNullOrEmpty()).collect(Collectors.toList());
        return wordReplaceRules;
    }

    public static void makeSFWVersion() {
        if (SuperstitioConfig.isEnableSFW()) {
            makeSFWWordForStringsSet();
        }
    }

    public static void makeKeyWords() {
        List<SuperstitioKeyWord> keywords = getAllKeywords();
        keywords.forEach(keyWord -> keyWord.makeSFWVersion(makeWordReplaceRule()));
        keywords.forEach(SuperstitioKeyWord::addToGame);
        return;
    }

    private static void loadUIStringsSet() {
        DataManager.loadCustomStringsFile("UIStringsWithSFW", DataManager.uiStrings, UIStringsSet.class);
    }

    private static void loadOrbStringsSet() {
        DataManager.loadCustomStringsFile("orb", DataManager.orbs, OrbStringsSet.class);
    }

    private static void makeSFWWordForStringsSet() {
        List<WordReplace> wordReplaces = makeWordReplaceRule();

        DataManager.cards.forEach((string, card) -> card.setupSFWStringByWordReplace(wordReplaces));
        DataManager.powers.forEach(((string, power) -> power.setupSFWStringByWordReplace(wordReplaces)));
        DataManager.modifiers.forEach(((string, modifier) -> modifier.setupSFWStringByWordReplace(wordReplaces)));
        DataManager.orbs.forEach(((string, orb) -> orb.setupSFWStringByWordReplace(wordReplaces)));

    }

    private static List<SuperstitioKeyWord> getAllKeywords() {
        List<SuperstitioKeyWord> keywordsSFW = new ArrayList<>();
        keywordsSFW.addAll(SuperstitioKeyWord.getAndRegisterKeywordsFormFile());
        keywordsSFW.addAll(Arrays.stream(MakeKeyWords()).map(SuperstitioKeyWord::STSLibKeyWordToThisType).collect(Collectors.toList()));
        return keywordsSFW;
    }
}
