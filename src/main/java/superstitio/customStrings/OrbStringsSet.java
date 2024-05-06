package superstitio.customStrings;

import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;

import java.util.List;

public class OrbStringsSet implements HasSFWVersionWithT<OrbStrings> {
    private String NAME;
    private String[] DESCRIPTION;
    private final OrbStrings Origin = new OrbStrings();
    private final OrbStrings SFW = new OrbStrings();

    public OrbStringsSet() {
    }

    @Override
    public void initialOrigin() {
        Origin.NAME = NAME;
        Origin.DESCRIPTION = DESCRIPTION;
    }

    @Override
    public Class<OrbStrings> getTClass() {
        return OrbStrings.class;
    }

    public String getNAME() {
        if (HasSFWVersion.shouldReturnSFWVersion(SFW.NAME))
            return SFW.NAME;
        return Origin.NAME;
    }

    public String[] getDESCRIPTION() {
        if (HasSFWVersion.shouldReturnSFWVersion(SFW.DESCRIPTION))
            return SFW.DESCRIPTION;
        return Origin.DESCRIPTION;
    }

    public OrbStrings getRightVersion() {
        if (HasSFWVersion.shouldReturnSFWVersion(SFW.NAME))
            return SFW;
        return Origin;
    }

    @Override
    public void initialSelfBlack() {
        this.NAME = "[MISSING_NAME]";
        this.DESCRIPTION = LocalizedStrings.createMockStringArray(10);
    }

    @Override
    public void setupSFWStringByWordReplace(List<WordReplace> replaceRules) {
        replaceWord_NAME(this.SFW, replaceRules);
        replaceWord_DESCRIPTION(this.SFW, replaceRules);
    }

    private void replaceWord_NAME(OrbStrings replaced, List<WordReplace> replaceRules) {
        if (HasSFWVersion.isNullOrEmpty(replaced.NAME))
            replaced.NAME = WordReplace.replaceWord(this.Origin.NAME, replaceRules);
        else
            replaced.NAME = WordReplace.replaceWord(replaced.NAME, replaceRules);
    }

    private void replaceWord_DESCRIPTION(OrbStrings replaced, List<WordReplace> replaceRules) {
        if (HasSFWVersion.isNullOrEmpty(replaced.DESCRIPTION))
            replaced.DESCRIPTION = WordReplace.replaceWord(this.Origin.DESCRIPTION, replaceRules);
        else
            WordReplace.replaceWord(replaced.DESCRIPTION, replaceRules);
    }

//    public WordReplace toCardNameReplaceRule(){
//        return new WordReplace(this.NAME,this.NAME_SFW);
//    }
//
//    public static List<WordReplace> makeCardNameReplaceRules(List<PowerStringsWithSFW> cards){
//        return cards.stream().map(PowerStringsWithSFW::toCardNameReplaceRule).collect(Collectors.toList());
//    }
}
