package superstitio.customStrings.interFace;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordReplace {
    public String WordOrigin;
    public String WordReplace;

    public WordReplace() {
    }

    public WordReplace(String WordOrigin, String WordReplace) {
        this.WordOrigin = WordOrigin;
        this.WordReplace = WordReplace;
    }

    public static String replaceWord(final String string, List<superstitio.customStrings.interFace.WordReplace> replaceRules) {
        final String[] newString = {string};
        for (superstitio.customStrings.interFace.WordReplace replaceRule : replaceRules) {
            newString[0] = replace(newString[0], replaceRule.WordOrigin, replaceRule.WordReplace);
        }
        return newString[0];
    }

    public static String replace(String StringToReplace, CharSequence target, CharSequence replacement) {
        return Pattern
                .compile(target.toString(), Pattern.LITERAL)
                .matcher(StringToReplace)
                .replaceAll(Matcher.quoteReplacement(replacement.toString()));
    }

    public static String[] replaceWord(String[] strings, List<superstitio.customStrings.interFace.WordReplace> replaceRule) {
        List<String> newStrings = new ArrayList<>();
        if (strings != null)
            for (String string : strings)
                newStrings.add(replaceWord(string, replaceRule));
        return newStrings.toArray(new String[0]);
    }

    public boolean hasNullOrEmpty() {
        return StringSetUtility.isNullOrEmpty(this.WordOrigin) || StringSetUtility.isNullOrEmpty(this.WordReplace);
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
