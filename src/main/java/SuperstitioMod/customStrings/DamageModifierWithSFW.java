package SuperstitioMod.customStrings;

import SuperstitioMod.WordReplace;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import java.util.List;
import java.util.stream.Collectors;

public class DamageModifierWithSFW implements HasSFWVersion {
    private String NAME;
    private String NAME_SFW;
    private String BASIC_INFO;
    private String BASIC_INFO_SFW;
    private String DESCRIPTION;
    private String DESCRIPTION_SFW;
    private String[] EXTENDED_DESCRIPTION;
    private String[] EXTENDED_DESCRIPTION_SFW;

    private final CardStrings Origin = new CardStrings();
    private final CardStrings SFW = new CardStrings();

    public DamageModifierWithSFW() {
    }

    @Override
    public void initialOrigin() {
//        Origin.NAME = NAME;
//        Origin.DESCRIPTION = DESCRIPTION;
//        Origin.UPGRADE_DESCRIPTION = UPGRADE_DESCRIPTION;
    }

    @Override
    public void initialSelfBlack() {
        this.NAME = "[MISSING_TITLE]";
        this.DESCRIPTION = "[MISSING_DESCRIPTION]";
        this.BASIC_INFO = "[MISSING_DESCRIPTION+]";
        this.EXTENDED_DESCRIPTION = LocalizedStrings.createMockStringArray(10);
    }

    public String getNAME() {
        if (HasSFWVersion.ifReturnSFWVersion(NAME_SFW))
            return NAME_SFW;
        return NAME;
    }

    public String getDESCRIPTION() {
        if (HasSFWVersion.ifReturnSFWVersion(DESCRIPTION_SFW))
            return DESCRIPTION_SFW;
        return DESCRIPTION;
    }

    public String getBasicInfo() {
        if (HasSFWVersion.ifReturnSFWVersion(BASIC_INFO_SFW))
            return BASIC_INFO_SFW;
        return BASIC_INFO;
    }

    public String[] getEXTENDED_DESCRIPTION() {
        if (HasSFWVersion.ifReturnSFWVersion(EXTENDED_DESCRIPTION_SFW))
            return EXTENDED_DESCRIPTION_SFW;
        return EXTENDED_DESCRIPTION;
    }

    @Override
    public void setupSFWStringByWordReplace(WordReplace replaceRule) {
        this.DESCRIPTION_SFW = WordReplace.replaceWord(this.getDESCRIPTION(), replaceRule);
        this.BASIC_INFO_SFW = WordReplace.replaceWord(this.getBasicInfo(), replaceRule);
    }

    public WordReplace toDamageNameReplaceRule() {
        return new WordReplace(this.NAME, this.NAME_SFW);
    }

    public static List<WordReplace> makeDamageNameReplaceRules(List<DamageModifierWithSFW> cards) {
        return cards.stream().map(DamageModifierWithSFW::toDamageNameReplaceRule).collect(Collectors.toList());
    }
}
