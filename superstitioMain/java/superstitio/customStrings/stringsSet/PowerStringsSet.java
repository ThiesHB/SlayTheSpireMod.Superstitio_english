package superstitio.customStrings.stringsSet;

import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import superstitio.customStrings.SuperstitioKeyWord;
import superstitio.customStrings.interFace.HasDifferentVersionStringSet;
import superstitio.customStrings.interFace.HasOriginAndSFWVersion;
import superstitio.customStrings.interFace.StringSetUtility;
import superstitio.customStrings.interFace.WordReplace;

import java.util.List;

public class PowerStringsSet implements HasOriginAndSFWVersion<PowerStrings>, SuperstitioKeyWord.WillMakeSuperstitioKeyWords {

    private final PowerStrings Origin = new PowerStrings();
    private final PowerStrings SFW = new PowerStrings();
    private String NAME;
    private String NAME_SFW;
    private String[] DESCRIPTIONS;
    private SuperstitioKeyWord[] MAKE_KEYWORDS;
    private String[] DESCRIPTIONS_SFW;

    public PowerStringsSet() {
    }

    public String getNAME() {
        return getFromRightVersion(strings -> strings.NAME);
    }

    public String[] getDESCRIPTIONS() {
        return getArrayFromRightVersion(strings -> strings.DESCRIPTIONS);
    }

    @Override
    public void initialSelfBlack() {
        this.NAME = "[MISSING_NAME]";
        this.DESCRIPTIONS = LocalizedStrings.createMockStringArray(10);
    }

    @Override
    public void initialOrigin(PowerStrings origin) {
        origin.NAME = NAME;
        origin.DESCRIPTIONS = DESCRIPTIONS;
    }

    @Override
    public HasDifferentVersionStringSet<PowerStrings> makeCopy() {
        PowerStringsSet clone = new PowerStringsSet();
        clone.NAME = this.NAME;
        clone.DESCRIPTIONS = this.DESCRIPTIONS;
        clone.NAME_SFW = this.NAME_SFW;
        clone.DESCRIPTIONS_SFW = DESCRIPTIONS_SFW;
        clone.MAKE_KEYWORDS = this.MAKE_KEYWORDS;
        return clone;
    }

    @Override
    public void initialSFW(PowerStrings sfw) {
        sfw.NAME = NAME_SFW;
        sfw.DESCRIPTIONS = DESCRIPTIONS_SFW;
    }

    @Override
    public void setupSFWStringByWordReplace(List<WordReplace> replaceRules) {
        if (StringSetUtility.isNullOrEmpty(this.SFW.NAME))
            this.SFW.NAME = WordReplace.replaceWord(this.Origin.NAME, replaceRules);
        if (StringSetUtility.isNullOrEmpty(this.SFW.DESCRIPTIONS))
            this.SFW.DESCRIPTIONS = WordReplace.replaceWord(this.Origin.DESCRIPTIONS, replaceRules);

        if (StringSetUtility.isNullOrEmpty(this.NAME_SFW))
            this.NAME_SFW = this.SFW.NAME;
        if (StringSetUtility.isNullOrEmpty(this.DESCRIPTIONS_SFW))
            this.DESCRIPTIONS_SFW = this.SFW.DESCRIPTIONS;
    }

    @Override
    public HasDifferentVersionStringSet<PowerStrings> makeSFWCopy() {
        HasDifferentVersionStringSet<PowerStrings> clone = this.makeCopy();
        if (clone instanceof PowerStringsSet) {
            PowerStringsSet clone1 = (PowerStringsSet) clone;
            clone1.NAME = null;
            clone1.DESCRIPTIONS = null;
            clone1.MAKE_KEYWORDS = null;
        }
        return clone;
    }

    @Override
    public PowerStrings getRightVersion() {
        if (StringSetUtility.shouldReturnSFWVersion(SFW.NAME))
            return SFW;
        return Origin;
    }

    @Override
    public Class<PowerStrings> getSubClass() {
        return PowerStrings.class;
    }

    @Override
    public PowerStrings getSFWVersion() {
        return SFW;
    }

    @Override
    public PowerStrings getOriginVersion() {
        return Origin;
    }

    @Override
    public SuperstitioKeyWord[] getWillMakeKEYWORDS() {
        if (MAKE_KEYWORDS != null && MAKE_KEYWORDS.length > 0)
            return MAKE_KEYWORDS;
        return new SuperstitioKeyWord[]{};
    }
}
