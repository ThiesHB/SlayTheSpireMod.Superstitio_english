package superstitio.customStrings;

import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import superstitio.WordReplace;

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
        if (HasSFWVersion.ifReturnSFWVersion(SFW.NAME))
            return SFW.NAME;
        return Origin.NAME;
    }

    public String[] getDESCRIPTION() {
        if (HasSFWVersion.ifReturnSFWVersion(SFW.DESCRIPTION))
            return SFW.DESCRIPTION;
        return Origin.DESCRIPTION;
    }

    public OrbStrings getRightVersion() {
        if (HasSFWVersion.ifReturnSFWVersion(SFW.NAME))
            return SFW;
        return Origin;
    }

    @Override
    public void initialSelfBlack() {
        this.NAME = "[MISSING_NAME]";
        this.DESCRIPTION = LocalizedStrings.createMockStringArray(10);
    }

    @Override
    public void setupSFWStringByWordReplace(WordReplace replaceRule) {
        replaceWord_NAME(this.SFW, replaceRule);
        replaceWord_DESCRIPTION(this.SFW, replaceRule);
    }

    private void replaceWord_NAME(OrbStrings replaced, WordReplace replaceRule) {
        if (HasSFWVersion.isEmptyOrNull(replaced.NAME))
            replaced.NAME = WordReplace.replaceWord(this.Origin.NAME, replaceRule);
        else
            replaced.NAME = WordReplace.replaceWord(replaced.NAME, replaceRule);
    }

    private void replaceWord_DESCRIPTION(OrbStrings replaced, WordReplace replaceRule) {
        if (HasSFWVersion.isEmptyOrNull(replaced.DESCRIPTION))
            replaced.DESCRIPTION = WordReplace.replaceWord(this.Origin.DESCRIPTION, replaceRule);
        else
            WordReplace.replaceWord(replaced.DESCRIPTION, replaceRule);
    }

//    public WordReplace toCardNameReplaceRule(){
//        return new WordReplace(this.NAME,this.NAME_SFW);
//    }
//
//    public static List<WordReplace> makeCardNameReplaceRules(List<PowerStringsWithSFW> cards){
//        return cards.stream().map(PowerStringsWithSFW::toCardNameReplaceRule).collect(Collectors.toList());
//    }
}
