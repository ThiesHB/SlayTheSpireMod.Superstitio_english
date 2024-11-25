package superstitio.customStrings.stringsSet

import com.megacrit.cardcrawl.localization.LocalizedStrings
import com.megacrit.cardcrawl.localization.UIStrings
import superstitio.customStrings.interFace.HasDifferentVersionStringSet
import superstitio.customStrings.interFace.HasOriginAndSFWVersion
import superstitio.customStrings.interFace.StringSetUtility
import superstitio.customStrings.interFace.WordReplace

class UIStringsSet : HasOriginAndSFWVersion<UIStrings> {
    override val OriginVersion = UIStrings()
    override val SfwVersion= UIStrings()
    private var TEXT: Array<String>? = null
    private var TEXT_SFW: Array<String>? = null
    private var EXTRA_TEXT: Array<String>? = null
    private var EXTRA_TEXT_SFW: Array<String>? = null
    private var TEXT_DICT: Map<String, String>? = null
    private var TEXT_DICT_SFW: Map<String, String>? = null

    fun getTEXT(): Array<String> {
        return getArrayFromRightVersion { strings: UIStrings? -> strings!!.TEXT }
    }

    fun getEXTRA_TEXT(): Array<String> {
        return getArrayFromRightVersion { strings: UIStrings? -> strings!!.EXTRA_TEXT }
    }

    override fun initialSelfBlack() {
        this.TEXT = LocalizedStrings.createMockStringArray(1)
        this.EXTRA_TEXT = LocalizedStrings.createMockStringArray(1)
        //        this.TEXT_DICT = LocalizedStrings.createMockStringArray(1);
    }

    override fun initialOrigin(origin: UIStrings) {
        origin.TEXT = TEXT
        origin.EXTRA_TEXT = EXTRA_TEXT
        origin.TEXT_DICT = TEXT_DICT
    }

    override fun initialSFW(sfw: UIStrings) {
        sfw.TEXT = TEXT_SFW
        sfw.EXTRA_TEXT = EXTRA_TEXT_SFW
        sfw.TEXT_DICT = TEXT_DICT_SFW
    }

    override fun getSubClass() = UIStrings::class.java

    override fun makeCopy(): HasDifferentVersionStringSet<UIStrings> {
        val clone = UIStringsSet()
        clone.TEXT = this.TEXT
        clone.TEXT_SFW = this.TEXT_SFW
        clone.EXTRA_TEXT = this.EXTRA_TEXT
        clone.EXTRA_TEXT_SFW = this.EXTRA_TEXT_SFW
        clone.TEXT_DICT = this.TEXT_DICT
        clone.TEXT_DICT_SFW = this.TEXT_DICT_SFW
        return clone
    }

    //    public Map<String, String> getUPGRADE_DESCRIPTION() {
    //        return getMapFromRightVersion(strings -> strings.TEXT_DICT);
    //    }
    override fun makeSFWCopy(): HasDifferentVersionStringSet<UIStrings> {
        val clone: HasDifferentVersionStringSet<UIStrings> = this.makeCopy()
        if (clone is UIStringsSet) {
            val clone1 = clone
            clone1.TEXT = null
            clone1.EXTRA_TEXT = null
            clone1.TEXT_DICT = null
        }
        return clone
    }

    override fun setupSFWStringByWordReplace(replaceRules: List<WordReplace>) {
//        this.DESCRIPTION_SFW = WordReplace.replaceWord(this.getDESCRIPTION(), replaceRules);
//        if (this.getUPGRADE_DESCRIPTION() != null)
//            this.UPGRADE_DESCRIPTION_SFW = WordReplace.replaceWord(this.getUPGRADE_DESCRIPTION(), replaceRules);
    }

    override fun getRightVersion(): UIStrings {
        if (StringSetUtility.shouldReturnSFWVersion(SfwVersion.TEXT)) return SfwVersion
        return OriginVersion
    }
}
