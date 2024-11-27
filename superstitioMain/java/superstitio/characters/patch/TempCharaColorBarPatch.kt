package superstitio.characters.patch

import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import superstitio.DataManager.SPTT_DATA.GeneralEnums
import superstitio.DataManager.SPTT_DATA.TempCardEnums
import superstitio.cards.general.GeneralCardVirtualCharacter
import superstitio.cards.general.TempCardVirtualCharacter

@SpirePatch2(clz = ColorTabBarFix::class, method = "capitalizeWord")
object TempCharaColorBarPatch {
    private val generalCardVirtualCharacter: AbstractPlayer = GeneralCardVirtualCharacter(CardCrawlGame.playerName)
    private val tempCardVirtualCharacter: AbstractPlayer = TempCardVirtualCharacter(CardCrawlGame.playerName)

    @SpirePrefixPatch
        @JvmStatic
    fun Prefix(str: String): SpireReturn<String> {
        if (str.equals(GeneralEnums.GENERAL_LIBRARY.name, ignoreCase = true)) {
            return SpireReturn.Return(generalCardVirtualCharacter.title)
        }
        if (str.equals(TempCardEnums.TempCard_LIBRARY.name, ignoreCase = true)) {
            return SpireReturn.Return(tempCardVirtualCharacter.title)
        }
        return SpireReturn.Continue()
    } //    public static ExprEditor Instrument() {
    //        return new ExprEditor() {
    //
    //            public void edit(final MethodCall m) throws CannotCompileException {
    //                if (m.getClassName().equals("ColorTabBarFix") && m.getMethodName().equals("capitalizeWord")) {
    //                    m.replace("if (" + TempCharaColorBarPath.class.getName() + ".Check($1)) " +
    //                            "{\$_ =" + TempCharaColorBarPath.class.getName() + ".ReturnRightPlayer($1)}" +
    //                            "else {\$_ = $proceed($$);}");
    //
    //                }
    //            }
    //        };
    //    }
    //    pu boolean Check(String characterNameType) {
    ////        if (characterNameType.equalsIgnoreCase(DataManager.SPTT_DATA.GeneralEnums.GENERAL_LIBRARY.name())) {
    ////            return true;
    ////        }
    ////        if (characterNameType.equalsIgnoreCase(DataManager.SPTT_DATA.TempCardEnums.TempCard_LIBRARY.name())) {
    ////            return true;
    ////        }
    ////        return false;
    ////    }
    ////
    ////    public String ReturnRightPlayer(String characterNameType) {
    ////        if (characterNameType.equalsIgnoreCase(DataManager.SPTT_DATA.GeneralEnums.GENERAL_LIBRARY.name())) {
    ////            return new GeneralCardVirtualCharacter(CardCrawlGame.playerName).name;
    ////        }
    ////        if (characterNameType.equalsIgnoreCase(DataManager.SPTT_DATA.TempCardEnums.TempCard_LIBRARY.name())) {
    ////            return new TempCardVirtualCharacter(CardCrawlGame.playerName).name;
    ////        }
    ////        return new GeneralCardVirtualCharacter(CardCrawlGame.playerName).name;
    ////    }
}
