package superstitio.customStrings

import basemod.BaseMod
import com.evacipated.cardcrawl.mod.stslib.Keyword
import superstitio.DataManager
import superstitio.customStrings.interFace.StringSetUtility
import superstitio.customStrings.interFace.WordReplace
import java.util.*
import java.util.stream.Collectors

class SuperstitioKeyWord
{
    var ID: String = ""
    var PROPER_NAME: String? = null
    var PROPER_NAME_SFW: String? = null
    var NAMES: Array<String>? = null
    var NAMES_SFW: Array<String>? = null
    var DESCRIPTION: String? = null
    var DESCRIPTION_SFW: String? = null

    fun makeCopy(): SuperstitioKeyWord
    {
        val clone = SuperstitioKeyWord()
        clone.ID = this.ID
        clone.PROPER_NAME = this.PROPER_NAME
        clone.PROPER_NAME_SFW = this.PROPER_NAME_SFW
        clone.NAMES = this.NAMES
        clone.NAMES_SFW = this.NAMES_SFW
        clone.DESCRIPTION = this.DESCRIPTION
        clone.DESCRIPTION_SFW = this.DESCRIPTION_SFW
        return clone
    }

    fun findPROPER_NAME(): String
    {
        if (StringSetUtility.shouldReturnSFWVersion(PROPER_NAME_SFW))
            return PROPER_NAME_SFW!!
        return PROPER_NAME!!
    }

    fun findNAMES(): Array<String>
    {
        if (StringSetUtility.shouldReturnSFWVersion(NAMES_SFW))
            return NAMES_SFW!!
        return NAMES!!
    }

    fun findDESCRIPTION(): String
    {
        if (StringSetUtility.shouldReturnSFWVersion(DESCRIPTION_SFW))
            return DESCRIPTION_SFW!!
        return DESCRIPTION!!
    }

    fun makeSFWVersion(replaceRules: List<WordReplace>)
    {
        if (NAMES_SFW.isNullOrEmpty())
            this.NAMES_SFW = WordReplace.replaceWord(this.findNAMES(), replaceRules)
        if (PROPER_NAME_SFW.isNullOrEmpty())
            this.PROPER_NAME_SFW = WordReplace.replaceWord(this.findPROPER_NAME(), replaceRules)
        if (DESCRIPTION_SFW.isNullOrEmpty())
            this.DESCRIPTION_SFW = WordReplace.replaceWord(this.findDESCRIPTION(), replaceRules)
    }

    fun addToGame()
    {
        BaseMod.addKeyword(
            DataManager.getModID().lowercase(Locale.getDefault()),
            this.PROPER_NAME,
            this.NAMES,
            this.DESCRIPTION
        )
        if (hasSFWVersionSelf()) BaseMod.addKeyword(
            DataManager.getModID().lowercase(Locale.getDefault()),
            this.findPROPER_NAME(),
            this.findNAMES(),
            this.findDESCRIPTION()
        )
        if (ID.isNotEmpty() && !KeywordsWithID.containsKey(ID))
            KeywordsWithID.put(ID, this)
    }

    fun registerKeywordFormFile()
    {
        if (!KeywordsFromFile.contains(this)) KeywordsFromFile.add(
            this
        )
    }

    private fun toReplaceRule(): List<WordReplace>
    {
        val wordReplaceList: MutableList<WordReplace> = ArrayList()
        if (!PROPER_NAME_SFW.isNullOrEmpty())
            wordReplaceList.add(WordReplace(this.PROPER_NAME!!, this.PROPER_NAME_SFW!!))
        if (!NAMES_SFW.isNullOrEmpty() && NAMES!!.size == NAMES_SFW!!.size)
        {
            for (i in NAMES!!.indices)
            {
                wordReplaceList.add(WordReplace(NAMES!![i], NAMES_SFW!![i]))
            }
        }
        return wordReplaceList
    }

    private fun hasSFWVersionSelf(): Boolean
    {
        return !PROPER_NAME_SFW.isNullOrEmpty() && !NAMES_SFW.isNullOrEmpty() && !DESCRIPTION_SFW.isNullOrEmpty()
    }

    interface WillMakeSuperstitioKeyWords
    {
        //        default void forKeywords(Consumer<SuperstitioKeyWord> keyWordConsumer) {
        fun getWillMakeKEYWORDS(): Array<SuperstitioKeyWord>

        //            for (SuperstitioKeyWord keyword : makeKEYWORDS()) {
        //                keyWordConsumer.accept(keyword);
        //            }
        //        }
    }

    interface SetupWithKeyWords
    {
        val needAddKeywords: List<SuperstitioKeyWord>
            get()
            {
                val superstitioKeyWords: MutableList<SuperstitioKeyWord> = ArrayList()
                for (keywordId in getWillAddKEYWORDS_ID())
                {
                    val keyWord = getAddedKeyword(keywordId)
                    keyWord?.let(superstitioKeyWords::add)
                }
                return superstitioKeyWords
            }

        //        default void forKeywords(Consumer<SuperstitioKeyWord> keyWordConsumer) {
        fun getWillAddKEYWORDS_ID(): Array<String>

        //            for (SuperstitioKeyWord keyword : addKEYWORDS()) {
        //                keyWordConsumer.accept(keyword);
        //            }
        //        }
    }

    companion object
    {
        val KeywordsFromFile: MutableList<SuperstitioKeyWord> = ArrayList()
        private val KeywordsWithID: MutableMap<String, SuperstitioKeyWord> = HashMap()

        //    private static final Map<String,String[]> multiKeyWords
        fun makeKeywordNameReplaceRules(keyWords: List<SuperstitioKeyWord>): List<WordReplace>
        {
            return keyWords.stream().flatMap { keyWord: SuperstitioKeyWord -> keyWord.toReplaceRule().stream() }
                .collect(Collectors.toList())
        }

        fun STSLibKeyWordToThisType(keyword: Keyword): SuperstitioKeyWord
        {
            val superstitioKeyWord = SuperstitioKeyWord()
            superstitioKeyWord.ID = keyword.ID
            superstitioKeyWord.PROPER_NAME = keyword.PROPER_NAME
            superstitioKeyWord.NAMES = keyword.NAMES
            superstitioKeyWord.DESCRIPTION = keyword.DESCRIPTION
            return superstitioKeyWord
        }

        //    public static com.megacrit.cardcrawl.localization.Keyword toSTSKeyWord(SuperstitioKeyWord superstitioKeyWord) {
        //        com.megacrit.cardcrawl.localization.Keyword keyword = new com.megacrit.cardcrawl.localization.Keyword();
        //        keyword.NAMES = superstitioKeyWord.NAMES;
        //    }
        fun getAddedKeyword(keywordId: String): SuperstitioKeyWord?
        {
            if (KeywordsWithID.containsKey(keywordId))
                return KeywordsWithID[keywordId]!!
            return null
        }

        fun getAndRegisterKeywordsFormFile(): List<SuperstitioKeyWord>
        {
            if (KeywordsFromFile.isEmpty())
            {
                val keywordsSFW: MutableList<SuperstitioKeyWord> = ArrayList()
                keywordsSFW.addAll(
                    DataManager.makeJsonStringFromFile(
                        "keyword", Array<SuperstitioKeyWord>::class.java
                    )
                )
                keywordsSFW.addAll(

                    DataManager.makeJsonStringFromFile(
                        "keyword_hangUpCard", Array<SuperstitioKeyWord>::class.java
                    )
                )
                keywordsSFW.addAll(
                    DataManager.makeJsonStringFromFile(
                        "keyword_Lupa", Array<SuperstitioKeyWord>::class.java
                    )

                )
                keywordsSFW.addAll(
                    DataManager.makeJsonStringFromFile(
                        "keyword_Maso", Array<SuperstitioKeyWord>::class.java
                    )
                )

                DataManager.forEachDataEachValue {
                    (it as? WillMakeSuperstitioKeyWords)?.getWillMakeKEYWORDS()?.toList()?.let(keywordsSFW::addAll)
                }

                keywordsSFW.forEach(SuperstitioKeyWord::registerKeywordFormFile)
            }
            return KeywordsFromFile
        }
    }
}
