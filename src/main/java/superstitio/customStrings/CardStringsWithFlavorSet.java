package superstitio.customStrings;

import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import superstitio.SuperstitioModSetup;

import java.util.List;
import java.util.stream.Collectors;

public class CardStringsWithFlavorSet implements HasSFWVersionWithT<CardStrings> {
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

    public CardStringsWithFlavorSet() {
    }

    public static List<WordReplace> makeCardNameReplaceRules(List<CardStringsWithFlavorSet> cards) {
        return cards.stream().map(CardStringsWithFlavorSet::toCardNameReplaceRule).collect(Collectors.toList());
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
        if (HasSFWVersion.shouldReturnSFWVersion(NAME_SFW))
            return NAME_SFW;
        return NAME;
    }

    public String getDESCRIPTION() {
        if (HasSFWVersion.shouldReturnSFWVersion(DESCRIPTION_SFW))
            return DESCRIPTION_SFW;
        return DESCRIPTION;
    }

    public String getUPGRADE_DESCRIPTION() {
        if (HasSFWVersion.shouldReturnSFWVersion(UPGRADE_DESCRIPTION_SFW))
            return UPGRADE_DESCRIPTION_SFW;
        return UPGRADE_DESCRIPTION;
    }

    public String[] getEXTENDED_DESCRIPTION() {
        if (HasSFWVersion.shouldReturnSFWVersion(EXTENDED_DESCRIPTION_SFW))
            return EXTENDED_DESCRIPTION_SFW;
        return EXTENDED_DESCRIPTION;
    }

    public String getFLAVOR() {
        if (SuperstitioModSetup.getEnableSFW())
            return "";
        return FLAVOR;
    }

    @Override
    public void setupSFWStringByWordReplace(List<WordReplace> replaceRules) {
        this.DESCRIPTION_SFW = WordReplace.replaceWord(this.getDESCRIPTION(), replaceRules);
        if (this.getUPGRADE_DESCRIPTION() != null)
            this.UPGRADE_DESCRIPTION_SFW = WordReplace.replaceWord(this.getUPGRADE_DESCRIPTION(), replaceRules);
    }

    private WordReplace toCardNameReplaceRule() {
        return new WordReplace(this.NAME, this.NAME_SFW);
    }

    @Override
    public CardStrings getRightVersion() {
        if (HasSFWVersion.shouldReturnSFWVersion(SFW.NAME))
            return SFW;
        return Origin;
    }
}
