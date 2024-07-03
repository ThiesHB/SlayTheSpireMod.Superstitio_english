package superstitio.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.cards.maso.MasoCard;

public class CardOwnerPlayerManager {
    public static String getImgPath(final String tag, final String id) {
        return DataManager.makeImgPath("default", DataManager::makeImgFilesPath_Card, id, tag);
    }

    public static boolean isLupaCard(AbstractCard card) {
        return card instanceof IsLupaCard && !(card instanceof IsNotLupaCard);
    }

    public static boolean isLupaCard(Class<?> cardType) {
        return IsLupaCard.class.isAssignableFrom(cardType) && !(IsNotLupaCard.class.isAssignableFrom(cardType));
    }

    public static boolean isOnlyLupaCard(AbstractCard card) {
        return isLupaCard(card) && !isMasoCard(card);
    }

    public static boolean isOnlyMasoCard(AbstractCard card) {
        return isMasoCard(card) && !isLupaCard(card);
    }

    public static boolean isMasoCard(AbstractCard card) {
        return card instanceof IsMasoCard && !(card instanceof IsNotMasoCard);
    }

    public static boolean isMasoCard(Class<?> cardType) {
        return IsMasoCard.class.isAssignableFrom(cardType) && !(IsNotMasoCard.class.isAssignableFrom(cardType));
    }

    public static boolean isGeneralCard(AbstractCard cardType) {
        return isMasoCard(cardType) && isLupaCard(cardType);
    }

    public static boolean isGeneralCard(Class<?> cardType) {
        return isMasoCard(cardType) && isLupaCard(cardType);
    }

    public static String getCardClass(AbstractCard card) {
        String packageName = card.getClass().getPackage().getName();
        if (packageName.contains(LupaCard.class.getPackage().getName())) {
            return "Lupa";
        }
        if (packageName.contains(MasoCard.class.getPackage().getName())) {
            return "Maso";
        }
        return "General";
    }

    public interface IsMasoCard {
    }

    /**
     * 优先级更高
     */
    public interface IsNotMasoCard {
    }

    public interface IsLupaCard {
    }

    public interface IsNotLupaCard {
    }
}
