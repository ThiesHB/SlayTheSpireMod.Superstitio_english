package superstitio.customStrings.stringsSet

import com.megacrit.cardcrawl.localization.LocalizedStrings
import com.megacrit.cardcrawl.localization.OrbStrings
import superstitio.customStrings.interFace.HasDifferentVersionStringSet
import superstitio.customStrings.interFace.HasOriginAndSFWVersion
import superstitio.customStrings.interFace.StringSetUtility
import superstitio.customStrings.interFace.WordReplace

class OrbStringsSet : HasOriginAndSFWVersion<OrbStrings> {
    override val OriginVersion= OrbStrings()
    override val SfwVersion= OrbStrings()
    private var NAME: String? = null
    private var NAME_SFW: String? = null
    private var DESCRIPTION: Array<String>? = null
    private var DESCRIPTION_SFW: Array<String>? = null

    fun getNAME(): String {
        return getFromRightVersion { strings: OrbStrings? -> strings!!.NAME }
    }

    fun getDESCRIPTION(): Array<String> {
        return getArrayFromRightVersion { strings: OrbStrings? -> strings!!.DESCRIPTION }
    }

    override fun initialSelfBlack() {
        this.NAME = "[MISSING_NAME]"
        this.DESCRIPTION = LocalizedStrings.createMockStringArray(10)
    }

    override fun initialOrigin(origin: OrbStrings) {
        origin.NAME = NAME
        origin.DESCRIPTION = DESCRIPTION
    }

    override fun initialSFW(sfw: OrbStrings) {
        sfw.NAME = NAME_SFW
        sfw.DESCRIPTION = DESCRIPTION_SFW
    }

    override fun getRightVersion(): OrbStrings {
        if (StringSetUtility.shouldReturnSFWVersion(SfwVersion.NAME)) return SfwVersion
        return OriginVersion
    }

    override fun getSubClass() = OrbStrings::class.java

    override fun makeCopy(): HasDifferentVersionStringSet<OrbStrings> {
        val clone = OrbStringsSet()
        clone.NAME = this.NAME
        clone.DESCRIPTION = this.DESCRIPTION
        clone.NAME_SFW = this.NAME_SFW
        clone.DESCRIPTION_SFW = this.DESCRIPTION_SFW
        return clone
    }

    override fun setupSFWStringByWordReplace(replaceRules: List<WordReplace>) {

        if (SfwVersion.NAME.isNullOrEmpty()) SfwVersion.NAME =
            WordReplace.replaceWord(OriginVersion.NAME, replaceRules)
        if (SfwVersion.DESCRIPTION.isNullOrEmpty()) SfwVersion.DESCRIPTION =
            WordReplace.replaceWord(OriginVersion.DESCRIPTION, replaceRules)

        if (this.NAME_SFW.isNullOrEmpty()) this.NAME_SFW =
            SfwVersion.NAME
        if (this.DESCRIPTION_SFW.isNullOrEmpty()) this.DESCRIPTION_SFW =
            SfwVersion.DESCRIPTION
    }

    override fun makeSFWCopy(): HasDifferentVersionStringSet<OrbStrings> {
        val clone: HasDifferentVersionStringSet<OrbStrings> = this.makeCopy()
        if (clone is OrbStringsSet) {
            clone.NAME = null
            clone.DESCRIPTION = null
        }
        return clone
    }
}
