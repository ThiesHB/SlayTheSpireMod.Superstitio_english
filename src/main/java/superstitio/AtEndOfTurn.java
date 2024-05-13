//package superstitio;
//
//import basemod.interfaces.ISubscriber;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.megacrit.cardcrawl.rooms.AbstractRoom;
//import superstitio.utils.CardUtility;
//
//public class AtEndOfTurn {
//    public interface EndOfTurnSubscriber extends ISubscriber {
//        void atEndOfTurn();
//    }
//
//    @SpirePatch(clz = AbstractRoom.class, method = "endTurn")
//    public static class EndOfTurnPatch {
//        public static void Postfix(final AbstractRoom _inst) {
//            if (CardUtility.isNotInBattle()) return;
//            SuperstitioModSubscriber.receiveAtEndOfTurn();
//        }
//    }
//}
