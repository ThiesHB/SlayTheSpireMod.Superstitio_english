package superstitio.customStrings.stringsSet

import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.localization.LocalizedStrings
import superstitio.Logger
import superstitio.SuperstitioConfig
import superstitio.customStrings.SuperstitioKeyWord
import superstitio.customStrings.SuperstitioKeyWord.SetupWithKeyWords
import superstitio.customStrings.SuperstitioKeyWord.WillMakeSuperstitioKeyWords
import superstitio.customStrings.interFace.HasDifferentVersionStringSet
import superstitio.customStrings.interFace.HasOriginAndSFWVersion
import superstitio.customStrings.interFace.StringSetUtility
import superstitio.customStrings.interFace.WordReplace
import java.util.stream.Collectors

class CardStringsWillMakeFlavorSet : HasOriginAndSFWVersion<CardStrings>, WillMakeSuperstitioKeyWords,
    SetupWithKeyWords {
    override val OriginVersion = CardStrings()
    override val SfwVersion = CardStrings()
    private var NAME: String? = null
    private var NAME_SFW: String? = null
    private var DESCRIPTION: String? = null
    private var DESCRIPTION_SFW: String? = null
    private var UPGRADE_DESCRIPTION: String? = null
    private var UPGRADE_DESCRIPTION_SFW: String? = null
    private var EXTENDED_DESCRIPTION: Array<String>? = null
    private var EXTENDED_DESCRIPTION_SFW: Array<String>? = null
    private var FLAVOR: String? = null
    private var FLAVOR_SFW: String? = null
    private var MAKE_KEYWORDS: Array<SuperstitioKeyWord>? = null
    private var ADD_KEYWORDS_ID: Array<String>? = null

    fun getNAME(): String {
        return getFromRightVersion(CardStrings::NAME)
    }

    fun getDESCRIPTION(): String {
        return getFromRightVersion(CardStrings::DESCRIPTION)
    }

    fun getUPGRADE_DESCRIPTION(): String {
        return getFromRightVersion(CardStrings::UPGRADE_DESCRIPTION)
    }

    //    public String[] getEXTENDED_DESCRIPTION() {
    //        return getArrayFromRightVersion(strings -> strings.EXTENDED_DESCRIPTION);
    //    }
    fun getEXTENDED_DESCRIPTION(index: Int): String {
        val EXTENDED_DESCRIPTION = getArrayFromRightVersion { strings: CardStrings? -> strings!!.EXTENDED_DESCRIPTION }
        if (index < EXTENDED_DESCRIPTION.size) return EXTENDED_DESCRIPTION[index]
        else {
            Logger.warning("Can't find the index " + index + " in the EXTENDED_DESCRIPTION array of" + this.NAME)
            return ""
        }
    }

    fun getFLAVOR(): String? {
        if (SuperstitioConfig.isEnableSFW()) return FLAVOR_SFW
        return FLAVOR
    }

    private fun toCardNameReplaceRule(): WordReplace {
        return WordReplace(this.NAME, this.NAME_SFW)
    }

    override fun initialSelfBlack() {
        this.NAME = "[MISSING_TITLE]"
        this.DESCRIPTION = "[MISSING_DESCRIPTION]"
        this.UPGRADE_DESCRIPTION = "[MISSING_DESCRIPTION+]"
        this.EXTENDED_DESCRIPTION = LocalizedStrings.createMockStringArray(10)
        this.FLAVOR = "[MISSING_FLAVOR]"
    }

    override fun initialOrigin(origin: CardStrings) {
        origin.NAME = NAME
        origin.DESCRIPTION = DESCRIPTION
        origin.UPGRADE_DESCRIPTION = UPGRADE_DESCRIPTION
        origin.EXTENDED_DESCRIPTION = EXTENDED_DESCRIPTION
        FlavorText.CardStringsFlavorField.flavor[origin] = FLAVOR
    }

    override fun initialSFW(sfw: CardStrings) {
        sfw.NAME = NAME_SFW
        sfw.DESCRIPTION = DESCRIPTION_SFW
        sfw.UPGRADE_DESCRIPTION = UPGRADE_DESCRIPTION_SFW
        sfw.EXTENDED_DESCRIPTION = EXTENDED_DESCRIPTION_SFW
        FlavorText.CardStringsFlavorField.flavor[sfw] = FLAVOR_SFW
    }

    override fun getRightVersion(): CardStrings {
        if (StringSetUtility.shouldReturnSFWVersion(SfwVersion.NAME)) return SfwVersion
        return OriginVersion
    }

    override fun getSubClass() = CardStrings::class.java

    override fun makeCopy(): HasDifferentVersionStringSet<CardStrings> {
        val clone = CardStringsWillMakeFlavorSet()
        clone.NAME = this.NAME
        clone.DESCRIPTION = this.DESCRIPTION
        clone.UPGRADE_DESCRIPTION = this.UPGRADE_DESCRIPTION
        clone.EXTENDED_DESCRIPTION = this.EXTENDED_DESCRIPTION
        clone.FLAVOR = this.FLAVOR
        clone.NAME_SFW = this.NAME_SFW
        clone.DESCRIPTION_SFW = this.DESCRIPTION_SFW
        clone.UPGRADE_DESCRIPTION_SFW = this.UPGRADE_DESCRIPTION_SFW
        clone.EXTENDED_DESCRIPTION_SFW = this.EXTENDED_DESCRIPTION_SFW
        clone.FLAVOR_SFW = this.FLAVOR_SFW
        clone.MAKE_KEYWORDS = this.MAKE_KEYWORDS
        clone.ADD_KEYWORDS_ID = this.ADD_KEYWORDS_ID
        return clone
    }

    override fun makeSFWCopy(): HasDifferentVersionStringSet<CardStrings> {
        val clone: HasDifferentVersionStringSet<CardStrings> = this.makeCopy()
        if (clone is CardStringsWillMakeFlavorSet) {
            val clone1 = clone
            clone1.NAME = null
            clone1.DESCRIPTION = null
            clone1.UPGRADE_DESCRIPTION = null
            clone1.EXTENDED_DESCRIPTION = null
            clone1.FLAVOR = null
            clone1.MAKE_KEYWORDS = null
        }
        return clone
    }

    override fun setupSFWStringByWordReplace(replaceRules: List<WordReplace>) {
        if (SfwVersion.NAME.isNullOrEmpty())
            SfwVersion.NAME = WordReplace.replaceWord(OriginVersion.NAME, replaceRules)
        if (SfwVersion.DESCRIPTION.isNullOrEmpty()) SfwVersion.DESCRIPTION =
            WordReplace.replaceWord(
                OriginVersion.DESCRIPTION, replaceRules
            )
        if (SfwVersion.UPGRADE_DESCRIPTION.isNullOrEmpty())
            if (!OriginVersion.UPGRADE_DESCRIPTION.isNullOrEmpty())
                SfwVersion.UPGRADE_DESCRIPTION =
                    WordReplace.replaceWord(OriginVersion.UPGRADE_DESCRIPTION, replaceRules)
        if (SfwVersion.EXTENDED_DESCRIPTION.isNullOrEmpty())
            SfwVersion.EXTENDED_DESCRIPTION =
                WordReplace.replaceWord(OriginVersion.EXTENDED_DESCRIPTION, replaceRules)

        if (this.NAME_SFW.isNullOrEmpty()) this.NAME_SFW =
            SfwVersion.NAME
        if (this.DESCRIPTION_SFW.isNullOrEmpty()) this.DESCRIPTION_SFW =
            SfwVersion.DESCRIPTION
        if (this.UPGRADE_DESCRIPTION_SFW.isNullOrEmpty()) this.UPGRADE_DESCRIPTION_SFW =
            SfwVersion.UPGRADE_DESCRIPTION
        if (this.EXTENDED_DESCRIPTION_SFW.isNullOrEmpty()) this.EXTENDED_DESCRIPTION_SFW =
            SfwVersion.EXTENDED_DESCRIPTION
    }


    override fun getWillMakeKEYWORDS(): Array<SuperstitioKeyWord> {
        if (MAKE_KEYWORDS != null && MAKE_KEYWORDS!!.isNotEmpty())
            return MAKE_KEYWORDS!!
        return arrayOf()
    }

    override fun getWillAddKEYWORDS_ID(): Array<String> {
        if (ADD_KEYWORDS_ID != null && ADD_KEYWORDS_ID!!.isNotEmpty())
            return ADD_KEYWORDS_ID!!
        return arrayOf()
    }

    companion object {
        fun makeCardNameReplaceRules(cards: List<CardStringsWillMakeFlavorSet>): List<WordReplace> {
            return cards.stream().map(CardStringsWillMakeFlavorSet::toCardNameReplaceRule)
                .collect(Collectors.toList())
        }
    }
}
