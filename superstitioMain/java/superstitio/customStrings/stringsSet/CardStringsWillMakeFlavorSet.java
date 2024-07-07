package superstitio.customStrings.stringsSet;

import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import superstitio.SuperstitioConfig;
import superstitio.customStrings.SuperstitioKeyWord;
import superstitio.customStrings.interFace.HasOriginAndSFWVersion;
import superstitio.customStrings.interFace.StringSetUtility;
import superstitio.customStrings.interFace.WordReplace;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CardStringsWillMakeFlavorSet implements HasOriginAndSFWVersion<CardStrings>, SuperstitioKeyWord.WillMakeSuperstitioKeyWords,
        SuperstitioKeyWord.SetupWithKeyWords {

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
    private String FLAVOR_SFW;
    private SuperstitioKeyWord[] MAKE_KEYWORDS;
    private String[] ADD_KEYWORDS_ID;

    public CardStringsWillMakeFlavorSet() {
    }

    public static List<WordReplace> makeCardNameReplaceRules(List<CardStringsWillMakeFlavorSet> cards) {
        return cards.stream().map(CardStringsWillMakeFlavorSet::toCardNameReplaceRule).collect(Collectors.toList());
    }

    public String getNAME() {
        return getFromRightVersion(strings -> strings.NAME);
    }

    public String getDESCRIPTION() {
        return getFromRightVersion(strings -> strings.DESCRIPTION);
    }

    public String getUPGRADE_DESCRIPTION() {
        return getFromRightVersion(strings -> strings.UPGRADE_DESCRIPTION);
    }

    public String[] getEXTENDED_DESCRIPTION() {
        return getArrayFromRightVersion(strings -> strings.EXTENDED_DESCRIPTION);
    }

    public Optional<String> getFLAVOR() {
        if (SuperstitioConfig.isEnableSFW())
            return Optional.ofNullable(FLAVOR_SFW);
        return Optional.ofNullable(FLAVOR);
    }

    private WordReplace toCardNameReplaceRule() {
        return new WordReplace(this.NAME, this.NAME_SFW);
    }

    @Override
    public void initialSelfBlack() {
        this.NAME = "[MISSING_TITLE]";
        this.DESCRIPTION = "[MISSING_DESCRIPTION]";
        this.UPGRADE_DESCRIPTION = "[MISSING_DESCRIPTION+]";
        this.EXTENDED_DESCRIPTION = LocalizedStrings.createMockStringArray(10);
        this.FLAVOR = "[MISSING_FLAVOR]";
    }

    @Override
    public void initialOrigin(CardStrings origin) {
        origin.NAME = NAME;
        origin.DESCRIPTION = DESCRIPTION;
        origin.UPGRADE_DESCRIPTION = UPGRADE_DESCRIPTION;
        origin.EXTENDED_DESCRIPTION = EXTENDED_DESCRIPTION;
        FlavorText.CardStringsFlavorField.flavor.set(origin, FLAVOR);
    }

    @Override
    public void initialSFW(CardStrings sfw) {
        sfw.NAME = NAME_SFW;
        sfw.DESCRIPTION = DESCRIPTION_SFW;
        sfw.UPGRADE_DESCRIPTION = UPGRADE_DESCRIPTION_SFW;
        sfw.EXTENDED_DESCRIPTION = EXTENDED_DESCRIPTION_SFW;
        FlavorText.CardStringsFlavorField.flavor.set(sfw, FLAVOR_SFW);
    }

    public CardStrings getRightVersion() {
        if (StringSetUtility.shouldReturnSFWVersion(SFW.NAME))
            return SFW;
        return Origin;
    }

    @Override
    public Class<CardStrings> getSubClass() {
        return CardStrings.class;
    }

    @Override
    public void setupSFWStringByWordReplace(List<WordReplace> replaceRules) {
        if (StringSetUtility.isNullOrEmpty(this.SFW.NAME))
            this.SFW.NAME = WordReplace.replaceWord(this.Origin.NAME, replaceRules);
        if (StringSetUtility.isNullOrEmpty(this.SFW.DESCRIPTION))
            this.SFW.DESCRIPTION = WordReplace.replaceWord(this.Origin.DESCRIPTION, replaceRules);
        if (StringSetUtility.isNullOrEmpty(this.SFW.UPGRADE_DESCRIPTION))
            if (!StringSetUtility.isNullOrEmpty(this.Origin.UPGRADE_DESCRIPTION))
                this.SFW.UPGRADE_DESCRIPTION = WordReplace.replaceWord(this.Origin.UPGRADE_DESCRIPTION, replaceRules);
        if (StringSetUtility.isNullOrEmpty(this.SFW.EXTENDED_DESCRIPTION))
            this.SFW.EXTENDED_DESCRIPTION = WordReplace.replaceWord(this.Origin.EXTENDED_DESCRIPTION, replaceRules);
    }


    @Override
    public CardStrings getSFWVersion() {
        return this.SFW;
    }

    @Override
    public CardStrings getOriginVersion() {
        return this.Origin;
    }

    @Override
    public SuperstitioKeyWord[] getWillMakeKEYWORDS() {
        if (MAKE_KEYWORDS != null && MAKE_KEYWORDS.length > 0)
            return MAKE_KEYWORDS;
        return new SuperstitioKeyWord[]{};
    }

    @Override
    public String[] getWillAddKEYWORDS_ID() {
        if (ADD_KEYWORDS_ID != null && ADD_KEYWORDS_ID.length > 0)
            return ADD_KEYWORDS_ID;
        return new String[]{};
    }
}
