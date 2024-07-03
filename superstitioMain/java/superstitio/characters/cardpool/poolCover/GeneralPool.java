package superstitio.characters.cardpool.poolCover;

import com.megacrit.cardcrawl.cards.AbstractCard;
import superstitio.DataManager;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.general.BaseCard.Masturbate;

import java.util.function.Predicate;

public class GeneralPool extends AbstractCover {
    public static final String ID = DataManager.MakeTextID(GeneralPool.class);

    public GeneralPool() {
        super(ID, new Masturbate());
    }

    @Override
    public Predicate<AbstractCard> getAddedCard() {
        return CardOwnerPlayerManager::isGeneralCard;
    }

    @Override
    public Predicate<AbstractCard> getBanedCard() {
        return null;
    }

}
