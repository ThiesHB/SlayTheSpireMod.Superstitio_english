package superstitio.customStrings;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.megacrit.cardcrawl.cards.AbstractCard;
import superstitio.DataManager;
import superstitio.customStrings.interFace.StringSetUtility;
import superstitio.customStrings.interFace.WordReplace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SuperstitioKeyWord {
    public static final List<SuperstitioKeyWord> KeywordsFromFile = new ArrayList<>();
    private static final Map<String, SuperstitioKeyWord> KeywordsWithID = new HashMap<>();
    public String ID = "";
    public String PROPER_NAME;
    public String PROPER_NAME_SFW;
    public String[] NAMES;
    public String[] NAMES_SFW;
    public String DESCRIPTION;
    public String DESCRIPTION_SFW;

    public static List<WordReplace> makeCardNameReplaceRules(List<SuperstitioKeyWord> keyWords) {
        return keyWords.stream().flatMap(keyWord -> keyWord.toReplaceRule().stream()).collect(Collectors.toList());
    }

    public static SuperstitioKeyWord STSLibKeyWordToThisType(Keyword keyword) {
        SuperstitioKeyWord superstitioKeyWord = new SuperstitioKeyWord();
        superstitioKeyWord.ID = keyword.ID;
        superstitioKeyWord.PROPER_NAME = keyword.PROPER_NAME;
        superstitioKeyWord.NAMES = keyword.NAMES;
        superstitioKeyWord.DESCRIPTION = keyword.DESCRIPTION;
        return superstitioKeyWord;
    }

//    public static com.megacrit.cardcrawl.localization.Keyword toSTSKeyWord(SuperstitioKeyWord superstitioKeyWord) {
//        com.megacrit.cardcrawl.localization.Keyword keyword = new com.megacrit.cardcrawl.localization.Keyword();
//        keyword.NAMES = superstitioKeyWord.NAMES;
//    }

    public static void addToCard(AbstractCard card, String keywordId) {
        String keywordFullId = DataManager.MakeTextID(keywordId);
        if (SuperstitioKeyWord.KeywordsWithID.containsKey(keywordFullId))
            card.keywords.add(SuperstitioKeyWord.KeywordsWithID.get(keywordFullId).getPROPER_NAME());
    }

    private List<WordReplace> toReplaceRule() {
        List<WordReplace> wordReplaceList = new ArrayList<>();
        if (!StringSetUtility.isNullOrEmpty(PROPER_NAME_SFW))
            wordReplaceList.add(new WordReplace(this.PROPER_NAME, this.PROPER_NAME_SFW));
        if (!StringSetUtility.isNullOrEmpty(NAMES_SFW) && NAMES.length == NAMES_SFW.length) {
            for (int i = 0; i < NAMES.length; i++) {
                wordReplaceList.add(new WordReplace(this.NAMES[i], this.NAMES_SFW[i]));
            }
        }
        return wordReplaceList;
    }

    public String getPROPER_NAME() {
        if (StringSetUtility.shouldReturnSFWVersion(PROPER_NAME_SFW))
            return PROPER_NAME_SFW;
        return PROPER_NAME;
    }

    public String[] getNAMES() {
        if (StringSetUtility.shouldReturnSFWVersion(NAMES_SFW))
            return NAMES_SFW;
        return NAMES;
    }

    public String getDESCRIPTION() {
        if (StringSetUtility.shouldReturnSFWVersion(DESCRIPTION_SFW))
            return DESCRIPTION_SFW;
        return DESCRIPTION;
    }

    public void makeSFWVersion(List<WordReplace> replaceRules) {
        this.PROPER_NAME_SFW = WordReplace.replaceWord(this.getPROPER_NAME(), replaceRules);
        this.DESCRIPTION_SFW = WordReplace.replaceWord(this.getDESCRIPTION(), replaceRules);
    }

    private String getIDWithMod() {
        return DataManager.MakeTextID(ID);
    }

    public void addToGame() {
        BaseMod.addKeyword(DataManager.getModID().toLowerCase(), this.PROPER_NAME, this.NAMES, this.DESCRIPTION);
        if (hasSFWVersionSelf())
            BaseMod.addKeyword(DataManager.getModID().toLowerCase(), this.getPROPER_NAME(), this.getNAMES(), this.getDESCRIPTION());
        if (!ID.isEmpty() && !KeywordsWithID.containsKey(getIDWithMod()))
            KeywordsWithID.put(getIDWithMod(), this);
    }

    public void registerKeywordFormFile() {
        if (!KeywordsFromFile.contains(this))
            KeywordsFromFile.add(this);
    }

    private boolean hasSFWVersionSelf() {
        return !StringSetUtility.isNullOrEmpty(PROPER_NAME_SFW) || !StringSetUtility.isNullOrEmpty(NAMES_SFW) || !StringSetUtility.isNullOrEmpty(DESCRIPTION_SFW);
    }
}
