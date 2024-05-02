package SuperstitioMod.customStrings;

import SuperstitioMod.WordReplace;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import java.util.List;
import java.util.stream.Collectors;

public class PowerStringsWithSFW implements HasSFWVersion {
    public String NAME;
    public String NAME_SFW;
    public String[] DESCRIPTIONS;
    public String[] DESCRIPTIONS_SFW;

    public PowerStringsWithSFW() {
    }

    public String getNAME() {
        if (HasSFWVersion.ifReturnSFWVersion(NAME_SFW))
            return NAME_SFW;
        return NAME;
    }

    public String[] getDESCRIPTIONS() {
        if (HasSFWVersion.ifReturnSFWVersion(DESCRIPTIONS_SFW))
            return DESCRIPTIONS_SFW;
        return DESCRIPTIONS;
    }

    @Override
    public void initialBlack() {
        this.NAME = "[MISSING_NAME]";
        this.DESCRIPTIONS = LocalizedStrings.createMockStringArray(10);
    }

    @Override
    public void setupSFWStringByWordReplace(WordReplace replaceRule) {
        this.NAME_SFW = WordReplace.replaceWord(this.getNAME(), replaceRule);
        this.DESCRIPTIONS_SFW = WordReplace.replaceWord(this.getDESCRIPTIONS(), replaceRule);
    }

//    public WordReplace toCardNameReplaceRule(){
//        return new WordReplace(this.NAME,this.NAME_SFW);
//    }
//
//    public static List<WordReplace> makeCardNameReplaceRules(List<PowerStringsWithSFW> cards){
//        return cards.stream().map(PowerStringsWithSFW::toCardNameReplaceRule).collect(Collectors.toList());
//    }
}
