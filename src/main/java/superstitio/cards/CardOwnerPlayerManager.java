package superstitio.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import superstitio.DataManager;

public class CardOwnerPlayerManager {
    public static String getImgPath(AbstractCard card, final String tag, final String id) {
        if (card instanceof IsLupaCard)
            return DataManager.makeImgPath("default", DataManager::makeImgFilesPath_LupaCard, tag, id);
        if (card instanceof IsMasoCard)
            return DataManager.makeImgPath("default", DataManager::makeImgFilesPath_MasoCard, tag, id);
        return DataManager.makeImgPath("default", DataManager::makeImgFilesPath_GeneralCard, tag, id);
    }

    public static boolean isLupaCard(AbstractCard card) {
        return card instanceof IsLupaCard && !(card instanceof IsNotLupaCard);
    }

    public static boolean isMasoCard(AbstractCard card) {
        return card instanceof IsMasoCard && !(card instanceof IsNotMasoCard);
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
