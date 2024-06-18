package superstitio.characters.patch;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import superstitio.DataManager;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.SuperstitioCard;

import java.util.ArrayList;
import java.util.Objects;

@SpirePatch(clz = CardLibrary.class, method = "getCardList")
public class GetCardListSwitchPatch {
    public static ArrayList<AbstractCard> Postfix(final ArrayList<AbstractCard> __result, final CardLibrary.LibraryType type) {
        if (BaseMod.isBaseGameCardColor(AbstractCard.CardColor.valueOf(type.name()))) return __result;
        for (AbstractCard card : CardLibrary.cards.values()) {
            if (!(card instanceof SuperstitioCard)) continue;
            if (__result.stream().anyMatch(c -> Objects.equals(c.cardID, card.cardID))) continue;
            if (type == DataManager.SPTT_DATA.LupaEnums.LUPA_LIBRARY && CardOwnerPlayerManager.isLupaCard(card)) {
                __result.add(card.makeCopy());
            }
            else if (type == DataManager.SPTT_DATA.MasoEnums.MASO_LIBRARY && CardOwnerPlayerManager.isMasoCard(card)) {
                __result.add(card.makeCopy());
            }
        }
//        for (AbstractCard card:CardLibrary.cards.values()){
//            if (!(card instanceof SuperstitioCard)) continue;
//            if (SuperstitioModSetup.getEnableGuroCharacter()) continue;
//            if (card instanceof MasoCard)
//                __result.removeIf(c-> Objects.equals(c.cardID, card.cardID));
//        }
        return __result;
    }
}