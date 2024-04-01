package SuperstitioMod.cards.Lupa;

import com.megacrit.cardcrawl.localization.CardStrings;

public class CardStringsWithFlavor {
    public String NAME;
    public String DESCRIPTION;
    public String UPGRADE_DESCRIPTION;
    public String[] EXTENDED_DESCRIPTION;
    public String FLAVOR;

    public CardStringsWithFlavor() {
    }

    public static CardStringsWithFlavor getMockCardStringWithFlavor() {
        CardStringsWithFlavor retVal = new CardStringsWithFlavor();
        retVal.NAME = "[MISSING_TITLE]";
        retVal.DESCRIPTION = "[MISSING_DESCRIPTION]";
        retVal.UPGRADE_DESCRIPTION = "[MISSING_DESCRIPTION+]";
        retVal.EXTENDED_DESCRIPTION = new String[]{"[MISSING_0]", "[MISSING_1]", "[MISSING_2]"};
        retVal.FLAVOR = "[MISSING_FLAVOR]";
        return retVal;
    }
}
