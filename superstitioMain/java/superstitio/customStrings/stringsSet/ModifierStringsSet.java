package superstitio.customStrings.stringsSet;

import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import superstitio.DataManager;
import superstitio.Logger;
import superstitio.customStrings.interFace.*;

import java.util.List;
import java.util.stream.Collectors;

public class ModifierStringsSet implements HasTextID, HasOriginAndSFWVersion<ModifierStringsSet.ModifierStrings> {


    private final ModifierStrings SFW = new ModifierStrings();
    private final ModifierStrings Origin = new ModifierStrings();
    private String NAME;
    private String NAME_SFW;
    private String BASIC_INFO;
    private String BASIC_INFO_SFW;
    private String DESCRIPTION;
    private String DESCRIPTION_SFW;
    private String[] EXTENDED_DESCRIPTION;
    private String[] EXTENDED_DESCRIPTION_SFW;
    private String textID;

    public ModifierStringsSet() {
    }

    public static List<WordReplace> makeModifierNameReplaceRules(List<ModifierStringsSet> cards) {
        return cards.stream().map(ModifierStringsSet::toModifierNameReplaceRule).collect(Collectors.toList());
    }

    public static Keyword[] MakeKeyWords() {
        return DataManager.modifiers.values().stream().map(ModifierStringsSet::ToKeyWord).toArray(Keyword[]::new);
    }

    public String getNAME() {
        return getFromRightVersion(strings -> strings.NAME);
    }

    public String getDESCRIPTION() {
        return getFromRightVersion(strings -> strings.DESCRIPTION);
    }

    public String getBasicInfo() {
        if (this.textID.endsWith("Block"))
            return CardCrawlGame.languagePack.getUIString(DataManager.MakeTextID("BlockModifier")).TEXT[0] + getBasicInfo_Pure();
        if (this.textID.endsWith("Damage"))
            return CardCrawlGame.languagePack.getUIString(DataManager.MakeTextID("DamageModifier")).TEXT[0] + getBasicInfo_Pure();
        return getBasicInfo_Pure();
    }

    public String getEXTENDED_DESCRIPTION(int index) {
        String[] EXTENDED_DESCRIPTION = getArrayFromRightVersion(strings -> strings.EXTENDED_DESCRIPTION);
        if (index < EXTENDED_DESCRIPTION.length)
            return EXTENDED_DESCRIPTION[index];
        else {
            Logger.warning("Can't find the index " + index + " in the EXTENDED_DESCRIPTION array of" + this.NAME);
            return "";
        }
//        return getArrayFromRightVersion(strings -> strings.EXTENDED_DESCRIPTION);
    }

    public Keyword ToKeyWord() {
        Keyword keyword = new Keyword();
        keyword.PROPER_NAME = this.getBasicInfo();
        keyword.NAMES = new String[]{this.getNAME(), this.getBasicInfo_Pure()};
        keyword.DESCRIPTION = this.getDESCRIPTION();
        return keyword;
    }

    private String getBasicInfo_Pure() {
        return getFromRightVersion(strings -> strings.BASIC_INFO);
    }

    private WordReplace toModifierNameReplaceRule() {
        return new WordReplace(this.NAME, this.NAME_SFW);
    }

    @Override
    public void initialSelfBlack() {
        this.NAME = "[MISSING_TITLE]";
        this.DESCRIPTION = "[MISSING_DESCRIPTION]";
        this.BASIC_INFO = "[MISSING_DESCRIPTION+]";
        this.EXTENDED_DESCRIPTION = LocalizedStrings.createMockStringArray(10);
    }

    @Override
    public void initialOrigin(ModifierStrings origin) {
        origin.NAME = NAME;
        origin.BASIC_INFO = BASIC_INFO;
        origin.DESCRIPTION = DESCRIPTION;
        origin.EXTENDED_DESCRIPTION = EXTENDED_DESCRIPTION;
    }

    @Override
    public void initialSFW(ModifierStrings sfw) {
        sfw.NAME = NAME_SFW;
        sfw.BASIC_INFO = BASIC_INFO_SFW;
        sfw.DESCRIPTION = DESCRIPTION_SFW;
        sfw.EXTENDED_DESCRIPTION = EXTENDED_DESCRIPTION_SFW;
    }

    public ModifierStrings getRightVersion() {
        if (StringSetUtility.shouldReturnSFWVersion(SFW.NAME))
            return SFW;
        return Origin;
    }

    @Override
    public ModifierStrings getSFWVersion() {
        return SFW;
    }

    @Override
    public ModifierStrings getOriginVersion() {
        return Origin;
    }

    @Override
    public Class<ModifierStrings> getSubClass() {
        return ModifierStrings.class;
    }

    @Override
    public void setupSFWStringByWordReplace(List<WordReplace> replaceRules) {
        if (StringSetUtility.isNullOrEmpty(this.SFW.NAME))
            this.SFW.NAME = WordReplace.replaceWord(this.Origin.NAME, replaceRules);
        if (StringSetUtility.isNullOrEmpty(this.SFW.DESCRIPTION))
            this.SFW.DESCRIPTION = WordReplace.replaceWord(this.Origin.DESCRIPTION, replaceRules);
        if (StringSetUtility.isNullOrEmpty(this.SFW.BASIC_INFO))
            this.SFW.BASIC_INFO = WordReplace.replaceWord(this.Origin.BASIC_INFO, replaceRules);
        if (StringSetUtility.isNullOrEmpty(this.SFW.EXTENDED_DESCRIPTION))
            this.SFW.EXTENDED_DESCRIPTION = WordReplace.replaceWord(this.Origin.EXTENDED_DESCRIPTION, replaceRules);

        if (StringSetUtility.isNullOrEmpty(this.NAME_SFW))
            this.NAME_SFW = this.SFW.NAME;
        if (StringSetUtility.isNullOrEmpty(this.DESCRIPTION_SFW))
            this.DESCRIPTION_SFW = this.SFW.DESCRIPTION;
        if (StringSetUtility.isNullOrEmpty(this.BASIC_INFO_SFW))
            this.BASIC_INFO_SFW = this.SFW.BASIC_INFO;
        if (StringSetUtility.isNullOrEmpty(this.EXTENDED_DESCRIPTION_SFW))
            this.EXTENDED_DESCRIPTION_SFW = this.SFW.EXTENDED_DESCRIPTION;
    }

    @Override
    public String getTextID() {
        return this.textID;
    }

    @Override
    public void setTextID(String textID) {
        this.textID = textID;
    }
    @Override
    public HasDifferentVersionStringSet<ModifierStrings> makeCopy() {
        ModifierStringsSet clone =  new ModifierStringsSet();
        clone.NAME = this.NAME;
        clone.DESCRIPTION = this.DESCRIPTION;
        clone.BASIC_INFO = this.BASIC_INFO;
        clone.EXTENDED_DESCRIPTION = this.EXTENDED_DESCRIPTION;
        clone.NAME_SFW = this.NAME_SFW;
        clone.DESCRIPTION_SFW = this.DESCRIPTION_SFW;
        clone.BASIC_INFO_SFW = this.BASIC_INFO_SFW;
        clone.EXTENDED_DESCRIPTION_SFW = this.EXTENDED_DESCRIPTION_SFW;
        return clone;
    }

    @Override
    public HasDifferentVersionStringSet<ModifierStrings> makeSFWCopy() {
        HasDifferentVersionStringSet<ModifierStrings> clone = this.makeCopy();
        if (clone instanceof ModifierStringsSet){
            ModifierStringsSet clone1 = (ModifierStringsSet) clone;
            clone1.NAME = null;
            clone1.DESCRIPTION = null;
            clone1.BASIC_INFO = null;
            clone1.EXTENDED_DESCRIPTION = null;
        }
        return clone;
    }

    public static class ModifierStrings {
        private String NAME;
        private String BASIC_INFO;
        private String DESCRIPTION;
        private String[] EXTENDED_DESCRIPTION;
    }
}
