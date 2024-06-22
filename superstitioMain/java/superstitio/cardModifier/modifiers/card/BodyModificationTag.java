package superstitio.cardModifier.modifiers.card;

import com.megacrit.cardcrawl.cards.AbstractCard;
import superstitio.DataManager;
import superstitio.customStrings.UIStringsSet;

public class BodyModificationTag extends AbstractCardTagModifier {
    public static final String ID = DataManager.MakeTextID(BodyModificationTag.class);
    private static final UIStringsSet uiStrings = BodyModificationTag.getUIStringsWithSFW(ID);

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
        return DataManager.CardTagsType.BodyModification;
    }
}
