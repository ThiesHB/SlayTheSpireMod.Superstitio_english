//package superstitio.orbs.actions;
//
//import com.megacrit.cardcrawl.cards.CardQueueItem;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import superstitio.actions.AutoDoneInstantAction;
//import superstitio.cards.general.TempCard.GangBang;
//import superstitio.orbs.SexMarkOrb;
//import superstitio.orbs.orbgroup.SexMarkOrbGroup;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class EvokeSexMark extends AutoDoneInstantAction {
//    private final SexMarkOrbGroup sexMarkOrbGroup;
//
//    public EvokeSexMark(SexMarkOrbGroup sexMarkOrbGroup) {
//
//        this.sexMarkOrbGroup = sexMarkOrbGroup;
//    }
//
//    @Override
//    public void autoDoneUpdate() {
//        AtomicInteger attackAmount = new AtomicInteger();
//        AtomicInteger blockAmount = new AtomicInteger();
//        sexMarkOrbGroup.orbs.forEach(orb -> {
//            if (sexMarkOrbGroup.isEmptySlot(orb)) return;
//            if (orb instanceof SexMarkOrb) {
//                SexMarkOrb sexMarkOrb = (SexMarkOrb) orb;
//                attackAmount.addAndGet(sexMarkOrb.attack());
//                blockAmount.addAndGet(sexMarkOrb.block());
//            }
//        });
//        GangBang gangBang = new GangBang(attackAmount.get(), blockAmount.get(),sexMarkOrbGroup.);
//        AutoDoneInstantAction.addToBotAbstract(() -> AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(gangBang, null, 0, true,
//                true), true));
//        AutoDoneInstantAction.addToBotAbstract(() -> {
//            int bound = sexMarkOrbGroup.orbs.size();
//            for (int i = 0; i < bound; i++) {
//                sexMarkOrbGroup.evokeOrbAndNotFill(i);
//            }
//        });
//    }
//}
