package superstitio.cardModifier.modifiers.card;

import com.megacrit.cardcrawl.cards.AbstractCard;
import superstitio.DataManager;
import superstitio.customStrings.UIStringsSet;

public class OutsideEjaculationTag extends AbstractCardTagModifier {
    public static final String ID = DataManager.MakeTextID(OutsideEjaculationTag.class);
    private static final UIStringsSet uiStrings = OutsideEjaculationTag.getUIStringsWithSFW(ID);

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
        return DataManager.CardTagsType.OutsideEjaculation;
    }
}
