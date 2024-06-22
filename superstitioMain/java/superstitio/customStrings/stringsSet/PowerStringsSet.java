package superstitio.customStrings.stringsSet;

import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import superstitio.customStrings.interFace.HasOriginAndSFWVersion;
import superstitio.customStrings.interFace.StringSetUtility;
import superstitio.customStrings.interFace.WordReplace;

import java.util.List;

public class PowerStringsSet implements HasOriginAndSFWVersion<PowerStrings> {

    private final PowerStrings Origin = new PowerStrings();
    private final PowerStrings SFW = new PowerStrings();
    private String NAME;
    private String NAME_SFW;
    private String[] DESCRIPTIONS;

    private String[] DESCRIPTIONS_SFW;

    public PowerStringsSet() {
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
    }

//    private void replaceWord_NAME(PowerStrings replaced, List<WordReplace> replaceRules) {
//        if (StringSetUtility.isNullOrEmpty(replaced.NAME))
//            replaced.NAME = WordReplace.replaceWord(this.NAME, replaceRules);
//        else
//            replaced.NAME = WordReplace.replaceWord(replaced.NAME, replaceRules);
//    }
//
//    private void replaceWord_DESCRIPTIONS(PowerStrings replaced, List<WordReplace> replaceRules) {
//        if (StringSetUtility.isNullOrEmpty(replaced.DESCRIPTIONS))
//            replaced.DESCRIPTIONS = WordReplace.replaceWord(this.DESCRIPTIONS, replaceRules);
//        else
//            WordReplace.replaceWord(replaced.DESCRIPTIONS, replaceRules);
//    }

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

    public String getNAME() {
        return getFromRightVersion(strings -> strings.NAME);
    }

    public String[] getDESCRIPTIONS() {
        return getArrayFromRightVersion(strings -> strings.DESCRIPTIONS);
    }

    @Override
    public PowerStrings getSFWVersion() {
        return SFW;
    }

    @Override
    public PowerStrings getOriginVersion() {
        return Origin;
    }
}
