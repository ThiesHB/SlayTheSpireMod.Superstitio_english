package superstitio.customStrings;

import superstitio.DataManager;
import superstitio.WordReplace;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import java.util.List;
import java.util.stream.Collectors;

public class ModifierStringsWithSFW implements HasTextID, HasSFWVersion {
    private String NAME;
    private String NAME_SFW;
    private String BASIC_INFO;
    private String BASIC_INFO_SFW;
    private String DESCRIPTION;
    private String DESCRIPTION_SFW;
    private String[] EXTENDED_DESCRIPTION;
    private String[] EXTENDED_DESCRIPTION_SFW;
    private String textID;

    public ModifierStringsWithSFW() {
    }

    public static List<WordReplace> makeModifierNameReplaceRules(List<ModifierStringsWithSFW> cards) {
        return cards.stream().map(ModifierStringsWithSFW::toModifierNameReplaceRule).collect(Collectors.toList());
    }

    public static List<WordReplace> makeDamageNameReplaceRules(List<ModifierStringsWithSFW> cards) {
        return cards.stream().map(ModifierStringsWithSFW::toDamageNameReplaceRule).collect(Collectors.toList());
    }

    public static Keyword[] MakeKeyWords() {
        return DataManager.modifiers.values().stream().map(ModifierStringsWithSFW::ToKeyWord).toArray(Keyword[]::new);
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
        if (this.textID.endsWith("Block"))
            return CardCrawlGame.languagePack.getUIString(DataManager.MakeTextID("BlockModifier")).TEXT[0] + getBasicInfo_Pure();
        if (this.textID.endsWith("Damage"))
            return CardCrawlGame.languagePack.getUIString(DataManager.MakeTextID("DamageModifier")).TEXT[0] + getBasicInfo_Pure();
        return getBasicInfo_Pure();
    }

    private String getBasicInfo_Pure() {
        if (HasSFWVersion.ifReturnSFWVersion(BASIC_INFO_SFW))
            return BASIC_INFO_SFW;
        return BASIC_INFO;
    }

    public String[] getEXTENDED_DESCRIPTION() {
        if (HasSFWVersion.ifReturnSFWVersion(EXTENDED_DESCRIPTION_SFW))
            return EXTENDED_DESCRIPTION_SFW;
        return EXTENDED_DESCRIPTION;
    }

    private WordReplace toModifierNameReplaceRule() {
        return new WordReplace(this.NAME, this.NAME_SFW);
    }

    @Override
    public void setupSFWStringByWordReplace(WordReplace replaceRule) {
        this.DESCRIPTION_SFW = WordReplace.replaceWord(this.getDESCRIPTION(), replaceRule);
        this.BASIC_INFO_SFW = WordReplace.replaceWord(this.getBasicInfo_Pure(), replaceRule);
    }

    public WordReplace toDamageNameReplaceRule() {
        return new WordReplace(this.NAME, this.NAME_SFW);
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
}
