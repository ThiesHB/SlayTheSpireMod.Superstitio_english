package superstitio.customStrings.stringsSet;

import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import superstitio.customStrings.interFace.HasDifferentVersionStringSet;
import superstitio.customStrings.interFace.HasOriginAndSFWVersion;
import superstitio.customStrings.interFace.StringSetUtility;
import superstitio.customStrings.interFace.WordReplace;

import java.util.List;
import java.util.Map;

public class UIStringsSet implements HasOriginAndSFWVersion<UIStrings> {
    private final UIStrings Origin = new UIStrings();
    private final UIStrings SFW = new UIStrings();
    private String[] TEXT;
    private String[] TEXT_SFW;
    private String[] EXTRA_TEXT;
    private String[] EXTRA_TEXT_SFW;
    private Map<String, String> TEXT_DICT;
    private Map<String, String> TEXT_DICT_SFW;

    public UIStringsSet() {
    }

    public String[] getTEXT() {
        return getArrayFromRightVersion(strings -> strings.TEXT);
    }

    public String[] getEXTRA_TEXT() {
        return getArrayFromRightVersion(strings -> strings.EXTRA_TEXT);
    }

    @Override
    public void initialSelfBlack() {
        this.TEXT = LocalizedStrings.createMockStringArray(1);
        this.EXTRA_TEXT = LocalizedStrings.createMockStringArray(1);
//        this.TEXT_DICT = LocalizedStrings.createMockStringArray(1);
    }

    @Override
    public void initialOrigin(UIStrings origin) {
        origin.TEXT = TEXT;
        origin.EXTRA_TEXT = EXTRA_TEXT;
        origin.TEXT_DICT = TEXT_DICT;
    }

    @Override
    public void initialSFW(UIStrings sfw) {
        sfw.TEXT = TEXT_SFW;
        sfw.EXTRA_TEXT = EXTRA_TEXT_SFW;
        sfw.TEXT_DICT = TEXT_DICT_SFW;
    }

    @Override
    public Class<UIStrings> getSubClass() {
        return UIStrings.class;
    }

    @Override
    public HasDifferentVersionStringSet<UIStrings> makeCopy() {
        UIStringsSet clone = new UIStringsSet();
        clone.TEXT = this.TEXT;
        clone.TEXT_SFW = this.TEXT_SFW;
        clone.EXTRA_TEXT = this.EXTRA_TEXT;
        clone.EXTRA_TEXT_SFW = this.EXTRA_TEXT_SFW;
        clone.TEXT_DICT = this.TEXT_DICT;
        clone.TEXT_DICT_SFW = this.TEXT_DICT_SFW;
        return clone;
    }

    //    public Map<String, String> getUPGRADE_DESCRIPTION() {
//        return getMapFromRightVersion(strings -> strings.TEXT_DICT);
//    }
    @Override
    public HasDifferentVersionStringSet<UIStrings> makeSFWCopy() {
        HasDifferentVersionStringSet<UIStrings> clone = this.makeCopy();
        if (clone instanceof UIStringsSet) {
            UIStringsSet clone1 = (UIStringsSet) clone;
            clone1.TEXT = null;
            clone1.EXTRA_TEXT = null;
            clone1.TEXT_DICT = null;
        }
        return clone;
    }

    @Override
    public void setupSFWStringByWordReplace(List<WordReplace> replaceRules) {
//        this.DESCRIPTION_SFW = WordReplace.replaceWord(this.getDESCRIPTION(), replaceRules);
//        if (this.getUPGRADE_DESCRIPTION() != null)
//            this.UPGRADE_DESCRIPTION_SFW = WordReplace.replaceWord(this.getUPGRADE_DESCRIPTION(), replaceRules);
    }

//    private WordReplace toCardNameReplaceRule() {
//        return new WordReplace(this.TEXT[0], this.TEXT_SFW[0]);
//    }

    @Override
    public UIStrings getSFWVersion() {
        return SFW;
    }

    @Override
    public UIStrings getOriginVersion() {
        return Origin;
    }

    @Override
    public UIStrings getRightVersion() {
        if (StringSetUtility.shouldReturnSFWVersion(SFW.TEXT))
            return SFW;
        return Origin;
    }
}
