//package superstitioapi.relicToBlight.patch;
//
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.relics.AbstractRelic;
//import javassist.CannotCompileException;
//import javassist.expr.ExprEditor;
//import javassist.expr.FieldAccess;
//import superstitioapi.relicToBlight.InfoBlight;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class LoadOutPowerButtonPatch {
//    public static HashSet<Integer> ProcessRelicsToRemove(HashSet<Integer> relicsToRemove) {
//        if (relicsToRemove.stream().mapToInt(integer -> integer).max().orElse(0) < AbstractDungeon.player.relics.size())
//            return relicsToRemove;
//        List<Integer> biggerRelics = relicsToRemove.stream().filter(integer -> integer >= AbstractDungeon.player.relics.size())
//                .map(integer -> integer - (AbstractDungeon.player.relics.size() - 1)).collect(Collectors.toList());
//        biggerRelics.forEach(relicsToRemove::remove);
//        biggerRelics.forEach(i -> AbstractDungeon.player.blights.remove((int) i));
//        return relicsToRemove;
//    }
//
//    @SpirePatch2(
//            cls = "loadout.relics.TrashBin",
//            method = "getPlayerRelicCopy",
//            optional = true
//    )
//    public static class LoadOutTrashBin {
//        @SpirePostfixPatch
//        public static void Postfix(ArrayList<AbstractRelic> __result) {
//            __result.addAll(InfoBlight.getAllInfoBlights(InfoBlight.class).stream()
//                    .map(infoBlight -> infoBlight.relic).collect(Collectors.toList()));
//        }
//    }
//
//    @SpirePatch(
//            cls = "loadout.LoadoutMod",
//            method = "modifyPlayerRelics",
//            optional = true
//    )
//    public static class LoadOutRelicsToRemove {
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                private int count = 0;
//
//                public void edit(FieldAccess m) throws CannotCompileException {
//                    if (!m.getFieldName().equals("relics") || !m.isReader()) return;
//                    if (count > 0) return;
//                    m.replace(" { loadout.LoadoutMod.relicsToRemove = " + LoadOutPowerButtonPatch.class.getName() + ".ProcessRelicsToRemove(loadout.LoadoutMod.relicsToRemove)"
//                            + " ; " +
//                            " $_ = $proceed($$);}");
//                    count++;
//                }
//            };
//        }
//    }
//}
