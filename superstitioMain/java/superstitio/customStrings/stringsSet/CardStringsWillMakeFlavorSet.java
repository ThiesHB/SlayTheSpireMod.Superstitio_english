package superstitio.customStrings.stringsSet;

import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import superstitio.SuperstitioConfig;
import superstitio.customStrings.SuperstitioKeyWord;
import superstitio.customStrings.interFace.*;

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
    public HasDifferentVersionStringSet<CardStrings> makeCopy() {
        CardStringsWillMakeFlavorSet clone = new CardStringsWillMakeFlavorSet();
        clone.NAME = this.NAME;
        clone.DESCRIPTION = this.DESCRIPTION;
        clone.UPGRADE_DESCRIPTION = this.UPGRADE_DESCRIPTION;
        clone.EXTENDED_DESCRIPTION = this.EXTENDED_DESCRIPTION;
        clone.FLAVOR = this.FLAVOR;
        clone.NAME_SFW = this.NAME_SFW;
        clone.DESCRIPTION_SFW = this.DESCRIPTION_SFW;
        clone.UPGRADE_DESCRIPTION_SFW = this.UPGRADE_DESCRIPTION_SFW;
        clone.EXTENDED_DESCRIPTION_SFW = this.EXTENDED_DESCRIPTION_SFW;
        clone.FLAVOR_SFW = this.FLAVOR_SFW;
        clone.MAKE_KEYWORDS = this.MAKE_KEYWORDS;
        clone.ADD_KEYWORDS_ID = this.ADD_KEYWORDS_ID;
        return clone;
    }

    @Override
    public HasDifferentVersionStringSet<CardStrings> makeSFWCopy() {
        HasDifferentVersionStringSet<CardStrings> clone = this.makeCopy();
        if (clone instanceof CardStringsWillMakeFlavorSet){
            CardStringsWillMakeFlavorSet clone1 = (CardStringsWillMakeFlavorSet) clone;
            clone1.NAME = null;
            clone1.DESCRIPTION = null;
            clone1.UPGRADE_DESCRIPTION = null;
            clone1.EXTENDED_DESCRIPTION = null;
            clone1.FLAVOR = null;
        }
        return clone;
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

        if (StringSetUtility.isNullOrEmpty(this.NAME_SFW))
            this.NAME_SFW = this.SFW.NAME;
        if (StringSetUtility.isNullOrEmpty(this.DESCRIPTION_SFW))
            this.DESCRIPTION_SFW = this.SFW.DESCRIPTION;
        if (StringSetUtility.isNullOrEmpty(this.UPGRADE_DESCRIPTION_SFW))
            this.UPGRADE_DESCRIPTION_SFW = this.SFW.UPGRADE_DESCRIPTION;
        if (StringSetUtility.isNullOrEmpty(this.EXTENDED_DESCRIPTION_SFW))
            this.EXTENDED_DESCRIPTION_SFW = this.SFW.EXTENDED_DESCRIPTION;
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
