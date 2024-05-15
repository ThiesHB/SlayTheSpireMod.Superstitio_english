package superstitio.characters;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import superstitio.DataManager;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.SuperstitioCard;

import java.util.ArrayList;

@SpirePatch(clz = CardLibrary.class, method = "getCardList")
public class GetCardListSwitch {
    public static ArrayList<AbstractCard> Postfix(final ArrayList<AbstractCard> __result, final CardLibrary.LibraryType type) {
        if (BaseMod.isBaseGameCardColor(AbstractCard.CardColor.valueOf(type.name()))) return __result;
        for (AbstractCard card : CardLibrary.cards.values()) {
            if (!(card instanceof SuperstitioCard)) continue;
            if (type == DataManager.SPTT_DATA.LupaEnums.LUPA_LIBRARY && CardOwnerPlayerManager.isLupaCard(card)) {
                __result.add(card);
            }
            else if (type == DataManager.SPTT_DATA.MasoEnums.MASO_LIBRARY && CardOwnerPlayerManager.isMasoCard(card)) {
                __result.add(card);
            }
        }
        return __result;
    }
}