package superstitio.customStrings;

import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.util.List;

public class PowerStringsSet implements HasSFWVersionWithT<PowerStrings> {
    private String NAME;
    private String[] DESCRIPTIONS;
    private final PowerStrings Origin = new PowerStrings();
    private final PowerStrings SFW = new PowerStrings();

    public PowerStringsSet() {
    }

    @Override
    public void initialOrigin() {
        Origin.NAME = NAME;
        Origin.DESCRIPTIONS = DESCRIPTIONS;
    }

    @Override
    public Class<PowerStrings> getTClass() {
        return PowerStrings.class;
    }

    public String getNAME() {
        if (HasSFWVersion.shouldReturnSFWVersion(SFW.NAME))
            return SFW.NAME;
        return Origin.NAME;
    }

    public String[] getDESCRIPTIONS() {
        if (HasSFWVersion.shouldReturnSFWVersion(SFW.DESCRIPTIONS))
            return SFW.DESCRIPTIONS;
        return Origin.DESCRIPTIONS;
    }

    public PowerStrings getRightVersion() {
        if (HasSFWVersion.shouldReturnSFWVersion(SFW.NAME))
            return SFW;
        return Origin;
    }

    @Override
    public void initialSelfBlack() {
        this.NAME = "[MISSING_NAME]";
        this.DESCRIPTIONS = LocalizedStrings.createMockStringArray(10);
    }

    @Override
    public void setupSFWStringByWordReplace(List<WordReplace> replaceRules) {
        replaceWord_NAME(this.SFW, replaceRules);
        replaceWord_DESCRIPTIONS(this.SFW, replaceRules);
    }

    private void replaceWord_NAME(PowerStrings replaced, List<WordReplace> replaceRules) {
        if (HasSFWVersion.isNullOrEmpty(replaced.NAME))
            replaced.NAME = WordReplace.replaceWord(this.NAME, replaceRules);
        else
            replaced.NAME = WordReplace.replaceWord(replaced.NAME, replaceRules);
    }

    private void replaceWord_DESCRIPTIONS(PowerStrings replaced, List<WordReplace> replaceRules) {
        if (HasSFWVersion.isNullOrEmpty(replaced.DESCRIPTIONS))
            replaced.DESCRIPTIONS = WordReplace.replaceWord(this.DESCRIPTIONS, replaceRules);
        else
            WordReplace.replaceWord(replaced.DESCRIPTIONS, replaceRules);
    }

//    public WordReplace toCardNameReplaceRule(){
//        return new WordReplace(this.NAME,this.NAME_SFW);
//    }
//
//    public static List<WordReplace> makeCardNameReplaceRules(List<PowerStringsWithSFW> cards){
//        return cards.stream().map(PowerStringsWithSFW::toCardNameReplaceRule).collect(Collectors.toList());
//    }
}
