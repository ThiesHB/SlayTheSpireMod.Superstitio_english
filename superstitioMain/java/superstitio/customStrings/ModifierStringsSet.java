package superstitio.customStrings;

import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import superstitio.DataManager;
import superstitio.customStrings.interFace.HasOriginAndSFWVersion;
import superstitio.customStrings.interFace.HasTextID;
import superstitio.customStrings.interFace.StringSetUtility;
import superstitio.customStrings.interFace.WordReplace;

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

    private String getBasicInfo_Pure() {
        return getFromRightVersion(strings -> strings.BASIC_INFO);
    }

    public String[] getEXTENDED_DESCRIPTION() {
        return getArrayFromRightVersion(strings -> strings.EXTENDED_DESCRIPTION);
    }

    private WordReplace toModifierNameReplaceRule() {
        return new WordReplace(this.NAME, this.NAME_SFW);
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
        this.SFW.DESCRIPTION = WordReplace.replaceWord(this.getDESCRIPTION(), replaceRules);
        this.SFW.BASIC_INFO = WordReplace.replaceWord(this.getBasicInfo_Pure(), replaceRules);
    }

    public Keyword ToKeyWord() {
        Keyword keyword = new Keyword();
        keyword.PROPER_NAME = this.getBasicInfo();
        keyword.NAMES = new String[]{this.getNAME(), this.getBasicInfo_Pure()};
        keyword.DESCRIPTION = this.getDESCRIPTION();
        return keyword;
    }

    @Override
    public String getTextID() {
        return this.textID;
    }

    @Override
    public void setTextID(String textID) {
        this.textID = textID;
    }

    public static class ModifierStrings {
        private String NAME;
        private String BASIC_INFO;
        private String DESCRIPTION;
        private String[] EXTENDED_DESCRIPTION;
    }
}
