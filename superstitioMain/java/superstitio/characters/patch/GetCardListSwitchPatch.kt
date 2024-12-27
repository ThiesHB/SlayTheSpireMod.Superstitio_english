package superstitio.characters.patch

import basemod.BaseMod
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor
import com.megacrit.cardcrawl.helpers.CardLibrary
import com.megacrit.cardcrawl.helpers.CardLibrary.LibraryType
import superstitio.DataManager.SPTT_DATA.LupaEnums
import superstitio.DataManager.SPTT_DATA.MasoEnums
import superstitio.SuperstitioConfig
import superstitio.cards.CardOwnerPlayerManager
import superstitio.cards.SuperstitioCard

@SpirePatch2(clz = CardLibrary::class, method = "getCardList")
object GetCardListSwitchPatch
{
    @SpirePostfixPatch
    @JvmStatic
    fun Postfix(__result: MutableList<AbstractCard>, type: LibraryType): MutableList<AbstractCard>
    {
        if (BaseMod.isBaseGameCardColor(CardColor.valueOf(type.name)))
            return __result
        if (SuperstitioConfig.isEnableOnlyShowCardNotGeneral)
            return __result
        for (card in CardLibrary.cards.values)
        {
            if (card !is SuperstitioCard)
                continue
            if (__result.any { it.cardID == card.cardID })
                continue
            when
            {
                type == LupaEnums.LUPA_LIBRARY && CardOwnerPlayerManager.isLupaCard(card) ->
                    __result.add(card.makeCopy())
                type == MasoEnums.MASO_LIBRARY && CardOwnerPlayerManager.isMasoCard(card) ->
                    __result.add(card.makeCopy())
            }
        }
        //        for (AbstractCard card:CardLibrary.cards.values()){
//            if (!(card instanceof SuperstitioCard)) continue;
//            if (SuperstitioModSetup.getEnableGuroCharacter()) continue;
//            if (card instanceof MasoCard)
//                __result.removeIf(c-> Objects.equals(c.cardID, card.cardID));
//        }
        return __result
    }
}