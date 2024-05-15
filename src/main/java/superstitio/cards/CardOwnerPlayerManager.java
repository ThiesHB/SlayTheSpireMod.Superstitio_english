package superstitio.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.cards.maso.MasoCard;

import static superstitio.DataManager.getIdOnly;

public class CardOwnerPlayerManager {
    public static String getImgPath(final String tag, final String id) {
        return DataManager.makeImgFilesPath_Card(getIdOnly(id), tag);
    }

    public static boolean isLupaCard(AbstractCard card) {
        return card instanceof IsLupaCard && !(card instanceof IsNotLupaCard);
    }

    public static boolean isMasoCard(AbstractCard card) {
        return card instanceof IsMasoCard && !(card instanceof IsNotMasoCard);
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
