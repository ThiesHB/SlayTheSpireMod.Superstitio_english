package superstitio.characters.cardpool.poolCover;

import com.megacrit.cardcrawl.cards.AbstractCard;
import superstitio.DataManager;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.lupa.PowerCard.defend.DrinkSemenBeer;

import java.util.function.Predicate;

public class LupaPool extends AbstractCover {
    public static final String ID = DataManager.MakeTextID(LupaPool.class);

    public LupaPool() {
        super(ID, new DrinkSemenBeer());
    }

    @Override
    public Predicate<AbstractCard> getAddedCard() {
        return CardOwnerPlayerManager::isOnlyLupaCard;
    }

    @Override
    public Predicate<AbstractCard> getBanedCard() {
        return null;
    }
}
