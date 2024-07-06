package superstitio.customStrings.stringsSet;

import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import superstitio.customStrings.interFace.HasOriginAndSFWVersion;
import superstitio.customStrings.interFace.StringSetUtility;
import superstitio.customStrings.interFace.WordReplace;

import java.util.List;

public class OrbStringsSet implements HasOriginAndSFWVersion<OrbStrings> {
    private final OrbStrings Origin = new OrbStrings();
    private final OrbStrings SFW = new OrbStrings();
    private String NAME;
    private String NAME_SFW;
    private String[] DESCRIPTION;
    private String[] DESCRIPTION_SFW;

    public OrbStringsSet() {
    }

    public String getNAME() {
        return getFromRightVersion(strings -> strings.NAME);
    }

    public String[] getDESCRIPTION() {
        return getArrayFromRightVersion(strings -> strings.DESCRIPTION);
    }

    @Override
    public void initialSelfBlack() {
        this.NAME = "[MISSING_NAME]";
        this.DESCRIPTION = LocalizedStrings.createMockStringArray(10);
    }

    @Override
    public void initialOrigin(OrbStrings origin) {
        origin.NAME = NAME;
        origin.DESCRIPTION = DESCRIPTION;
    }

    @Override
    public void initialSFW(OrbStrings sfw) {
        sfw.NAME = NAME_SFW;
        sfw.DESCRIPTION = DESCRIPTION_SFW;
    }

    public OrbStrings getRightVersion() {
        if (StringSetUtility.shouldReturnSFWVersion(SFW.NAME))
            return SFW;
        return Origin;
    }

    @Override
    public Class<OrbStrings> getSubClass() {
        return OrbStrings.class;
    }

    @Override
    public OrbStrings getSFWVersion() {
        return SFW;
    }

    @Override
    public OrbStrings getOriginVersion() {
        return Origin;
    }

    @Override
    public void setupSFWStringByWordReplace(List<WordReplace> replaceRules) {
//        replaceWord_NAME(this.SFW, replaceRules);
//        replaceWord_DESCRIPTION(this.SFW, replaceRules);

        if (StringSetUtility.isNullOrEmpty(this.SFW.NAME))
            this.SFW.NAME = WordReplace.replaceWord(this.Origin.NAME, replaceRules);
        if (StringSetUtility.isNullOrEmpty(this.SFW.DESCRIPTION))
            this.SFW.DESCRIPTION = WordReplace.replaceWord(this.Origin.DESCRIPTION, replaceRules);
    }

//    private void replaceWord_NAME(OrbStrings replaced, List<WordReplace> replaceRules) {
//        if (StringSetUtility.isNullOrEmpty(replaced.NAME))
//            replaced.NAME = WordReplace.replaceWord(this.Origin.NAME, replaceRules);
//        else
//            replaced.NAME = WordReplace.replaceWord(replaced.NAME, replaceRules);
//    }

//    private void replaceWord_DESCRIPTION(OrbStrings replaced, List<WordReplace> replaceRules) {
//        if (StringSetUtility.isNullOrEmpty(replaced.DESCRIPTION))
//            replaced.DESCRIPTION = WordReplace.replaceWord(this.Origin.DESCRIPTION, replaceRules);
//        else
//            WordReplace.replaceWord(replaced.DESCRIPTION, replaceRules);
//    }

//    public WordReplace toCardNameReplaceRule(){
//        return new WordReplace(this.NAME,this.NAME_SFW);
//    }
//
//    public static List<WordReplace> makeCardNameReplaceRules(List<PowerStringsWithSFW> cards){
//        return cards.stream().map(PowerStringsWithSFW::toCardNameReplaceRule).collect(Collectors.toList());
//    }
}
