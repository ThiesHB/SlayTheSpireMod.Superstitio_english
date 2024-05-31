package superstitio.characters;

import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCardVirtualCharacter;
import superstitio.cards.general.TempCardVirtualCharacter;

@SpirePatch(clz = ColorTabBarFix.class, method = "capitalizeWord")
public class TempCharaColorBarPath {

    private static final AbstractPlayer generalCardVirtualCharacter = new GeneralCardVirtualCharacter(CardCrawlGame.playerName);
    private static final AbstractPlayer tempCardVirtualCharacter = new TempCardVirtualCharacter(CardCrawlGame.playerName);

    @SpirePrefixPatch
    public static SpireReturn<String> Prefix(String str) {
        if (str.equalsIgnoreCase(DataManager.SPTT_DATA.GeneralEnums.GENERAL_LIBRARY.name())) {
            return SpireReturn.Return(generalCardVirtualCharacter.title);
        }
        if (str.equalsIgnoreCase(DataManager.SPTT_DATA.TempCardEnums.TempCard_LIBRARY.name())) {
            return SpireReturn.Return(tempCardVirtualCharacter.title);
        }
        return SpireReturn.Continue();
    }


//    public static ExprEditor Instrument() {
//        return new ExprEditor() {
//
//            public void edit(final MethodCall m) throws CannotCompileException {
//                if (m.getClassName().equals("ColorTabBarFix") && m.getMethodName().equals("capitalizeWord")) {
//                    m.replace("if (" + TempCharaColorBarPath.class.getName() + ".Check($1)) " +
//                            "{$_ =" + TempCharaColorBarPath.class.getName() + ".ReturnRightPlayer($1)}" +
//                            "else {$_ = $proceed($$);}");
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
