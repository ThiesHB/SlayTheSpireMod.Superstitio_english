package superstitio.customStrings;

import superstitio.SuperstitioModSetup;
import superstitio.WordReplace;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import java.util.List;
import java.util.stream.Collectors;

public class CardStringsWithSFWAndFlavor implements HasSFWVersionWithT<CardStrings> {
    private final CardStrings Origin = new CardStrings();
    private final CardStrings SFW = new CardStrings();
    private String NAME;
    private String NAME_SFW;
    private String DESCRIPTION;
    private String DESCRIPTION_SFW;
    private String UPGRADE_DESCRIPTION;
    private String UPGRADE_DESCRIPTION_SFW;
    private String[] EXTENDED_DESCRIPTION;
    private String[] EXTENDED_DESCRIPTION_SFW;
    private String FLAVOR;

    public CardStringsWithSFWAndFlavor() {
    }

    public static List<WordReplace> makeCardNameReplaceRules(List<CardStringsWithSFWAndFlavor> cards) {
        return cards.stream().map(CardStringsWithSFWAndFlavor::toCardNameReplaceRule).collect(Collectors.toList());
    }

    @Override
    public void initialOrigin() {
        Origin.NAME = NAME;
        Origin.DESCRIPTION = DESCRIPTION;
        Origin.UPGRADE_DESCRIPTION = UPGRADE_DESCRIPTION;
        Origin.EXTENDED_DESCRIPTION = EXTENDED_DESCRIPTION;
        FlavorText.CardStringsFlavorField.flavor.set(Origin, FLAVOR);
    }

    @Override
    public Class<CardStrings> getTClass() {
        return CardStrings.class;
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

    private WordReplace toCardNameReplaceRule() {
        return new WordReplace(this.NAME, this.NAME_SFW);
    }

    @Override
    public CardStrings getRightVersion() {
        if (HasSFWVersion.ifReturnSFWVersion(SFW.NAME))
            return SFW;
        return Origin;
    }
}
