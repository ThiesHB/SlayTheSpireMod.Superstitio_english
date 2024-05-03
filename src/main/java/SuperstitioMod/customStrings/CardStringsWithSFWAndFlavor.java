package SuperstitioMod.customStrings;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.WordReplace;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.util.List;
import java.util.stream.Collectors;

public class CardStringsWithSFWAndFlavor implements HasSFWVersion {
    private String NAME;
    private String NAME_SFW;
    private String DESCRIPTION;
    private String DESCRIPTION_SFW;
    private String UPGRADE_DESCRIPTION;
    private String UPGRADE_DESCRIPTION_SFW;
    private String[] EXTENDED_DESCRIPTION;
    private String[] EXTENDED_DESCRIPTION_SFW;
    private String FLAVOR;

    private final CardStrings Origin = new CardStrings();
    private final CardStrings SFW = new CardStrings();

    public CardStringsWithSFWAndFlavor() {
    }

    @Override
    public void initialOrigin() {
        Origin.NAME = NAME;
        Origin.DESCRIPTION = DESCRIPTION;
        Origin.UPGRADE_DESCRIPTION = UPGRADE_DESCRIPTION;
    }

    @Override
    public void initialSelfBlack() {
        this.NAME = "[MISSING_TITLE]";
        this.DESCRIPTION = "[MISSING_DESCRIPTION]";
        this.UPGRADE_DESCRIPTION = "[MISSING_DESCRIPTION+]";
        this.EXTENDED_DESCRIPTION = LocalizedStrings.createMockStringArray(10);
        this.FLAVOR = "[MISSING_FLAVOR]";
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

    public String getUPGRADE_DESCRIPTION() {
        if (HasSFWVersion.ifReturnSFWVersion(UPGRADE_DESCRIPTION_SFW))
            return UPGRADE_DESCRIPTION_SFW;
        return UPGRADE_DESCRIPTION;
    }

    public String[] getEXTENDED_DESCRIPTION() {
        if (HasSFWVersion.ifReturnSFWVersion(EXTENDED_DESCRIPTION_SFW))
            return EXTENDED_DESCRIPTION_SFW;
        return EXTENDED_DESCRIPTION;
    }

    public String getFLAVOR() {
        if (SuperstitioModSetup.enableSFW)
            return "";
        return FLAVOR;
    }

    @Override
    public void setupSFWStringByWordReplace(WordReplace replaceRule) {
        this.DESCRIPTION_SFW = WordReplace.replaceWord(this.getDESCRIPTION(), replaceRule);
        this.UPGRADE_DESCRIPTION_SFW = WordReplace.replaceWord(this.getUPGRADE_DESCRIPTION(), replaceRule);
    }

    public WordReplace toCardNameReplaceRule() {
        return new WordReplace(this.NAME, this.NAME_SFW);
    }

    public static List<WordReplace> makeCardNameReplaceRules(List<CardStringsWithSFWAndFlavor> cards) {
        return cards.stream().map(CardStringsWithSFWAndFlavor::toCardNameReplaceRule).collect(Collectors.toList());
    }
}
