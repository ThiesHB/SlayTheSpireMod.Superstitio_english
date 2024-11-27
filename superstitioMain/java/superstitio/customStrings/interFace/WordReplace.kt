package superstitio.customStrings.interFace

import java.util.regex.Matcher
import java.util.regex.Pattern

class WordReplace
{
    lateinit var WordOrigin: String
    lateinit var WordReplace: String

    constructor()

    constructor(WordOrigin: String?, WordReplace: String?)
    {
        if (WordOrigin == null || WordReplace == null)
            return
        this.WordOrigin = WordOrigin
        this.WordReplace = WordReplace
    }

    fun hasNullOrEmpty(): Boolean
    {
        if (::WordReplace.isInitialized && ::WordOrigin.isInitialized)
            return this.WordOrigin.isEmpty() || this.WordReplace.isEmpty()
        return true
    }

    companion object
    {
        @JvmStatic
        fun replaceWord(string: String?, replaceRules: List<WordReplace>): String
        {
            if (string == null) return ""
            val newString = arrayOf(string)
            for (replaceRule in replaceRules)
            {
                newString[0] = replace(newString[0], replaceRule.WordOrigin, replaceRule.WordReplace)
            }
            return newString[0]
        }

        @JvmStatic
        fun replace(StringToReplace: String, target: CharSequence, replacement: CharSequence): String
        {
            return Pattern
                .compile(target.toString(), Pattern.LITERAL)
                .matcher(StringToReplace)
                .replaceAll(Matcher.quoteReplacement(replacement.toString()))
        }

        @JvmStatic
        fun replaceWord(strings: Array<String>?, replaceRule: List<WordReplace>): Array<String>
        {
            if (strings == null) return emptyArray()
            val newStrings: MutableList<String> = ArrayList()
            for (string in strings)
                newStrings.add(replaceWord(string, replaceRule))
            return newStrings.toTypedArray<String>()
        }
    }
}
