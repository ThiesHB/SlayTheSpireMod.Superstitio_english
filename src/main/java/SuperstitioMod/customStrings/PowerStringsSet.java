package SuperstitioMod.customStrings;

import SuperstitioMod.WordReplace;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;

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
        if (HasSFWVersion.ifReturnSFWVersion(SFW.NAME))
            return SFW.NAME;
        return Origin.NAME;
    }

    public String[] getDESCRIPTIONS() {
        if (HasSFWVersion.ifReturnSFWVersion(SFW.DESCRIPTIONS))
            return SFW.DESCRIPTIONS;
        return Origin.DESCRIPTIONS;
    }

    public PowerStrings getRightVersion(){
        if (HasSFWVersion.ifReturnSFWVersion(SFW.NAME))
            return SFW;
        return Origin;
    }

    @Override
    public void initialSelfBlack() {
        this.NAME = "[MISSING_NAME]";
        this.DESCRIPTIONS = LocalizedStrings.createMockStringArray(10);
    }

    @Override
    public void setupSFWStringByWordReplace(WordReplace replaceRule) {
        replaceWord_NAME(this.SFW,replaceRule);
        replaceWord_DESCRIPTIONS(this.SFW,replaceRule);
    }

    private void replaceWord_NAME(PowerStrings replaced,WordReplace replaceRule){
        if (HasSFWVersion.isEmptyOrNull(replaced.NAME))
            replaced.NAME = WordReplace.replaceWord(this.Origin.NAME, replaceRule);
        else
            replaced.NAME = WordReplace.replaceWord(replaced.NAME, replaceRule);
    }

    private void replaceWord_DESCRIPTIONS(PowerStrings replaced,WordReplace replaceRule){
        if (HasSFWVersion.isEmptyOrNull(replaced.DESCRIPTIONS))
            replaced.DESCRIPTIONS = WordReplace.replaceWord(this.Origin.DESCRIPTIONS, replaceRule);
        else
            WordReplace.replaceWord(replaced.DESCRIPTIONS, replaceRule);
    }

//    public WordReplace toCardNameReplaceRule(){
//        return new WordReplace(this.NAME,this.NAME_SFW);
//    }
//
//    public static List<WordReplace> makeCardNameReplaceRules(List<PowerStringsWithSFW> cards){
//        return cards.stream().map(PowerStringsWithSFW::toCardNameReplaceRule).collect(Collectors.toList());
//    }
}
