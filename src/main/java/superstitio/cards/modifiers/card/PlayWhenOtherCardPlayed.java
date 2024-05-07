/*
package superstitio.cards.modifiers.card;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import superstitio.DataManager;

*/
/*

public class PlayWhenOtherCardPlayed extends AbstractCardModifier {

    public static final String ID = DataManager.MakeTextID(PlayWhenOtherCardPlayed.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private int amount;

    public PlayWhenOtherCardPlayed() {
        amount = 1;
    }

    public PlayWhenOtherCardPlayed setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return String.format(uiStrings.TEXT[0], rawDescription);
    }
//
//    public boolean shouldApply(AbstractCard card) {
//        return !CardModifierManager.hasModifier(card, PlayWhenOtherCardPlayed.ID);
//    }

    public String identifier(AbstractCard card) {
        return String.valueOf(this.amount);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new PlayWhenOtherCardPlayed().setAmount(amount);
    }
}
*/
