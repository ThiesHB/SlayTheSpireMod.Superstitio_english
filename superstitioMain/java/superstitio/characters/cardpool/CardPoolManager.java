package superstitio.characters.cardpool;

import basemod.IUIElement;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import superstitioapi.cardPool.BaseCardPool;

import java.util.ArrayList;
import java.util.function.Predicate;

import static superstitioapi.cardPool.BaseCardPool.*;

public class CardPoolManager implements IUIElement {
    public static final CardPoolManager instance = new CardPoolManager();
    public final ArrayList<BaseCardPool> cardPools = new ArrayList<>();

    public CardPoolManager() {
        cardPools.add(new GeneralCardPool());
        cardPools.add(new LupaCardPool());
        cardPools.add(new MasoCardPool());

        float x = Settings.WIDTH * 0.2f;
        float y = Settings.HEIGHT * 0.8f;
        for (BaseCardPool cardPool : cardPools) {
            cardPool.transportTo(x, y);
            x += HB_W_CARD * COVER_DRAW_SCALE * COVER_DRAW_HOVER_SCALE_RATE;
        }
    }

    public Predicate<AbstractCard> getAddedCard() {
        Predicate<AbstractCard> cardPredicate = card -> false;
        for (BaseCardPool cardPool : cardPools) {
            cardPredicate = cardPredicate.or(cardPool.getAddedCard());
        }
        return cardPredicate;
    }

    public Predicate<AbstractCard> getBanedCard() {
        Predicate<AbstractCard> cardPredicate = card -> false;
        for (BaseCardPool cardPool : cardPools) {
            cardPredicate = cardPredicate.or(cardPool.getBanedCard());
        }
        return cardPredicate;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        cardPools.forEach(cardPool -> cardPool.render(spriteBatch));
    }

    @Override
    public void update() {
        cardPools.forEach(BaseCardPool::update);
    }

    @Override
    public int renderLayer() {
        return 0;
    }

    @Override
    public int updateOrder() {
        return 0;
    }
}
