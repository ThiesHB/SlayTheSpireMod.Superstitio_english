package superstitio.cardModifier.modifiers.card;

import com.megacrit.cardcrawl.cards.AbstractCard;
import superstitio.DataManager;
import superstitio.customStrings.stringsSet.UIStringsSet;

public class InsideEjaculationTag extends AbstractCardTagModifier {
    public static final String ID = DataManager.MakeTextID(InsideEjaculationTag.class);
    private static final UIStringsSet uiStrings = InsideEjaculationTag.getUIStringsWithSFW(ID);

    public static AbstractCard.CardTags getInsideEjaculationTag() {
        return DataManager.CardTagsType.InsideEjaculation;
    }

    @Override
    protected UIStringsSet getUiStrings() {
        return uiStrings;
    }

    @Override
    protected String getID() {
        return ID;
    }

    @Override
    protected AbstractCard.CardTags getTag() {
        return getInsideEjaculationTag();
    }
}
