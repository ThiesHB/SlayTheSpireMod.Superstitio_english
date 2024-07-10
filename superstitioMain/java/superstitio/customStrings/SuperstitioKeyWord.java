package superstitio.customStrings;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import superstitio.DataManager;
import superstitio.customStrings.interFace.StringSetUtility;
import superstitio.customStrings.interFace.WordReplace;

import java.util.*;
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

    public SuperstitioKeyWord makeCopy() {
        SuperstitioKeyWord clone = new SuperstitioKeyWord();
        clone.ID = this.ID;
        clone.PROPER_NAME = this.PROPER_NAME;
        clone.PROPER_NAME_SFW = this.PROPER_NAME_SFW;
        clone.NAMES = this.NAMES;
        clone.NAMES_SFW = this.NAMES_SFW;
        clone.DESCRIPTION = this.DESCRIPTION;
        clone.DESCRIPTION_SFW = this.DESCRIPTION_SFW;
        return clone;
    }

//    private static final Map<String,String[]> multiKeyWords

    public static List<WordReplace> makeKeywordNameReplaceRules(List<SuperstitioKeyWord> keyWords) {
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

    public static Optional<SuperstitioKeyWord> getAddedKeyword(String keywordId) {
        if (SuperstitioKeyWord.KeywordsWithID.containsKey(keywordId))
            return Optional.of(KeywordsWithID.get(keywordId));
        return Optional.empty();
    }

    public static List<SuperstitioKeyWord> getAndRegisterKeywordsFormFile() {
        if (KeywordsFromFile.isEmpty()) {
            List<SuperstitioKeyWord> keywordsSFW = new ArrayList<>();
            keywordsSFW.addAll(Arrays.asList(DataManager.makeJsonStringFromFile("keyword", SuperstitioKeyWord[].class)));
            keywordsSFW.addAll(Arrays.asList(DataManager.makeJsonStringFromFile("keyword_hangUpCard", SuperstitioKeyWord[].class)));
            keywordsSFW.addAll(Arrays.asList(DataManager.makeJsonStringFromFile("keyword_Lupa", SuperstitioKeyWord[].class)));
            keywordsSFW.addAll(Arrays.asList(DataManager.makeJsonStringFromFile("keyword_Maso", SuperstitioKeyWord[].class)));

            DataManager.forEachData(data -> data.forEach((string, stringSet) -> {
                if (stringSet instanceof WillMakeSuperstitioKeyWords) {
                    keywordsSFW.addAll(Arrays.asList(((WillMakeSuperstitioKeyWords) stringSet).getWillMakeKEYWORDS()));
                }
            }));

            keywordsSFW.forEach(SuperstitioKeyWord::registerKeywordFormFile);
        }
        return KeywordsFromFile;
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
        if (StringSetUtility.isNullOrEmpty(NAMES_SFW))
            this.NAMES_SFW = WordReplace.replaceWord(this.NAMES, replaceRules);
        if (StringSetUtility.isNullOrEmpty(PROPER_NAME_SFW))
            this.PROPER_NAME_SFW = WordReplace.replaceWord(this.PROPER_NAME, replaceRules);
        if (StringSetUtility.isNullOrEmpty(DESCRIPTION_SFW))
            this.DESCRIPTION_SFW = WordReplace.replaceWord(this.DESCRIPTION, replaceRules);
    }

    public void addToGame() {
        BaseMod.addKeyword(DataManager.getModID().toLowerCase(), this.PROPER_NAME, this.NAMES, this.DESCRIPTION);
        if (hasSFWVersionSelf())
            BaseMod.addKeyword(DataManager.getModID().toLowerCase(), this.getPROPER_NAME(), this.getNAMES(), this.getDESCRIPTION());
        if (!ID.isEmpty() && !KeywordsWithID.containsKey(ID))
            KeywordsWithID.put(ID, this);
    }

    public void registerKeywordFormFile() {
        if (!KeywordsFromFile.contains(this))
            KeywordsFromFile.add(this);
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

    private boolean hasSFWVersionSelf() {
        return !StringSetUtility.isNullOrEmpty(PROPER_NAME_SFW) && !StringSetUtility.isNullOrEmpty(NAMES_SFW) && !StringSetUtility.isNullOrEmpty(DESCRIPTION_SFW);
    }

    public interface WillMakeSuperstitioKeyWords {
        SuperstitioKeyWord[] getWillMakeKEYWORDS();

//        default void forKeywords(Consumer<SuperstitioKeyWord> keyWordConsumer) {
//            for (SuperstitioKeyWord keyword : makeKEYWORDS()) {
//                keyWordConsumer.accept(keyword);
//            }
//        }
    }

    public interface SetupWithKeyWords {
        default List<SuperstitioKeyWord> getNeedAddKeywords() {
            List<SuperstitioKeyWord> superstitioKeyWords = new ArrayList<>();
            for (String keywordId : getWillAddKEYWORDS_ID()) {
                Optional<SuperstitioKeyWord> keyWord = getAddedKeyword(keywordId);
                keyWord.ifPresent(superstitioKeyWords::add);
            }
            return superstitioKeyWords;
        }

        String[] getWillAddKEYWORDS_ID();

//        default void forKeywords(Consumer<SuperstitioKeyWord> keyWordConsumer) {
//            for (SuperstitioKeyWord keyword : addKEYWORDS()) {
//                keyWordConsumer.accept(keyword);
//            }
//        }
    }
}
