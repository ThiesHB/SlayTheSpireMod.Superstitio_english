package SuperstitioMod;

public class WordReplace {
    public String WordOrigin;
    public String WordReplace;

    public WordReplace() {
    }

    public WordReplace(String WordOrigin, String WordReplace) {
        this.WordOrigin = WordOrigin;
        this.WordReplace = WordReplace;
    }


    public static String replaceWord(String string, WordReplace replaceRule) {
        if (string != null && string.contains(replaceRule.WordOrigin)) {
            return string.replace(replaceRule.WordOrigin, replaceRule.WordReplace);
        }
        return string;
    }

    public static String[] replaceWord(String[] strings, WordReplace replaceRule) {
        for (int i = 0; i < strings.length; i++)
            strings[i] = replaceWord(strings[i], replaceRule);
        return strings;
    }

//    public static WordReplace getMockCardStringWithFlavor() {
//        WordReplace retVal = new WordReplace();
//        retVal.NAME = "[MISSING_TITLE]";
//        retVal.DESCRIPTION = "[MISSING_DESCRIPTION]";
//        retVal.UPGRADE_DESCRIPTION = "[MISSING_DESCRIPTION+]";
//        retVal.EXTENDED_DESCRIPTION = new String[]{"[MISSING_0]", "[MISSING_1]", "[MISSING_2]"};
//        retVal.FLAVOR = "[MISSING_FLAVOR]";
//        return retVal;
//    }
}
