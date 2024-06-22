package superstitio.cardModifier.modifiers.card;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardTags;
import com.megacrit.cardcrawl.cards.AbstractCard;
import superstitio.DataManager;
import superstitio.customStrings.interFace.StringSetUtility;
import superstitio.customStrings.stringsSet.UIStringsSet;

public abstract class AbstractCardTagModifier extends AbstractCardModifier {
    public static UIStringsSet getUIStringsWithSFW(String uiID) {
        return StringSetUtility.getCustomStringsWithSFW(uiID, DataManager.uiStrings, UIStringsSet.class);
    }

    protected abstract UIStringsSet getUiStrings();

    protected abstract String getID();

//    @Override
//    public boolean isInherent(AbstractCard card) {
//        return true;
//    }

    protected abstract AbstractCard.CardTags getTag();

    public boolean shouldApply(AbstractCard card) {
        return !card.hasTag(getTag());
    }

    public void onInitialApplication(AbstractCard card) {
        CardTags.addTags(card, getTag());
    }

    public void onRemove(AbstractCard card) {
        CardTags.removeTags(card, getTag());
    }

    @Override
    public String identifier(AbstractCard card) {
        return getID();
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return String.format(getUiStrings().getTEXT()[0], super.modifyName(cardName, card));
    }

    public AbstractCardModifier makeCopy() {
        try {
            return this.getClass().newInstance();
        } catch (IllegalAccessException | InstantiationException var2) {
            throw new RuntimeException("SuperstitioApi failed to auto-generate makeCopy for cardModifier: " + this.getID());
        }
    }
}
