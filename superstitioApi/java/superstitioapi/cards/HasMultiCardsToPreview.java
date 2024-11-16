package superstitioapi.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.List;

public interface HasMultiCardsToPreview {

    float SHOW_TIME = 1.0f;

    List<AbstractCard> getMultiCardsToPreview();

    float getCardsToPreviewTimer();

    void setCardsToPreviewTimer(float cardsToPreviewTimer);

    void setCardsToPreview(AbstractCard cardsToPreview);

    default void update() {
        if (getMultiCardsToPreview().isEmpty()) return;
        float timer = getCardsToPreviewTimer();
        if (timer < 0) {
            setCardsToPreviewTimer(getMultiCardsToPreview().size() * SHOW_TIME);
        }
        int index = MathUtils.floor(timer / SHOW_TIME);
        if (index < 0)
            index = 0;
        if (index > getMultiCardsToPreview().size() - 1)
            index = getMultiCardsToPreview().size() - 1;
        AbstractCard newCardsToPreview = getMultiCardsToPreview().get(index);
        setCardsToPreview(newCardsToPreview);

        setCardsToPreviewTimer(getCardsToPreviewTimer() - Gdx.graphics.getDeltaTime());
    }
}
