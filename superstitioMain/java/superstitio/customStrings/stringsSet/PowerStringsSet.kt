package superstitio.customStrings.stringsSet

import com.megacrit.cardcrawl.localization.LocalizedStrings
import com.megacrit.cardcrawl.localization.PowerStrings
import superstitio.Logger
import superstitio.customStrings.SuperstitioKeyWord
import superstitio.customStrings.SuperstitioKeyWord.WillMakeSuperstitioKeyWords
import superstitio.customStrings.interFace.*

class PowerStringsSet : HasOriginAndSFWVersion<PowerStrings>, WillMakeSuperstitioKeyWords
{
    override val OriginVersion = PowerStrings()
    override val SfwVersion = PowerStrings()
    private var NAME: String? = null
    private var NAME_SFW: String? = null
    private var DESCRIPTIONS: Array<String>? = null
    private var MAKE_KEYWORDS: Array<SuperstitioKeyWord>? = null
    private var DESCRIPTIONS_SFW: Array<String>? = null

    fun getNAME(): String
    {
        return getFromRightVersion { strings: PowerStrings? -> strings!!.NAME }!!
    }

    fun getDESCRIPTION(index: Int): String
    {
        val DESCRIPTIONS = getArrayFromRightVersion { strings: PowerStrings? -> strings!!.DESCRIPTIONS }
        if (index < DESCRIPTIONS!!.size)
            return DESCRIPTIONS[index]
        else
        {
            Logger.warning("Can't find the index " + index + " in the EXTENDED_DESCRIPTION array of" + this.NAME)
            return ""
        }
        //        return getArrayFromRightVersion(strings -> strings.DESCRIPTIONS);
    }

    fun getDESCRIPTIONS(): Array<String>
    {
        return getArrayFromRightVersion { strings: PowerStrings? -> strings!!.DESCRIPTIONS }!!
    }

    override fun initialSelfBlack()
    {
        this.NAME = "[MISSING_NAME]"
        this.DESCRIPTIONS = LocalizedStrings.createMockStringArray(10)
    }

    override fun initialOrigin(origin: PowerStrings)
    {
        origin.NAME = NAME
        origin.DESCRIPTIONS = DESCRIPTIONS
    }

    override fun makeCopy(): HasDifferentVersionStringSet<PowerStrings>
    {
        val clone = PowerStringsSet()
        clone.NAME = this.NAME
        clone.DESCRIPTIONS = this.DESCRIPTIONS
        clone.NAME_SFW = this.NAME_SFW
        clone.DESCRIPTIONS_SFW = DESCRIPTIONS_SFW
        clone.MAKE_KEYWORDS = this.MAKE_KEYWORDS
        return clone
    }

    override fun initialSFW(sfw: PowerStrings)
    {
        sfw.NAME = NAME_SFW
        sfw.DESCRIPTIONS = DESCRIPTIONS_SFW
    }

    override fun setupSFWStringByWordReplace(replaceRules: List<WordReplace>)
    {
        SfwVersion.NAME =
            updateFieldIfEmpty(SfwVersion.NAME,OriginVersion.NAME, replaceRules)
        SfwVersion.DESCRIPTIONS =
            updateFieldIfEmpty(SfwVersion.DESCRIPTIONS,OriginVersion.DESCRIPTIONS, replaceRules)

        this.NAME_SFW = this.NAME_SFW.takeIfNullOrEmpty(SfwVersion.NAME)
        this.DESCRIPTIONS_SFW = this.DESCRIPTIONS_SFW.takeIfNullOrEmpty(SfwVersion.DESCRIPTIONS)
    }

    override fun makeSFWCopy(): HasDifferentVersionStringSet<PowerStrings>
    {
        val clone: HasDifferentVersionStringSet<PowerStrings> = this.makeCopy()
        if (clone is PowerStringsSet)
        {
            clone.NAME = null
            clone.DESCRIPTIONS = null
            clone.MAKE_KEYWORDS = null
        }
        return clone
    }

    override fun getRightVersion(): PowerStrings
    {
        if (StringSetUtility.shouldReturnSFWVersion(SfwVersion.NAME)) return SfwVersion
        return OriginVersion
    }

    override fun getSubClass() = PowerStrings::class.java

    override fun getWillMakeKEYWORDS(): Array<SuperstitioKeyWord>
    {
        if (MAKE_KEYWORDS != null && MAKE_KEYWORDS!!.isNotEmpty())
            return MAKE_KEYWORDS!!
        return arrayOf()
    }
}
