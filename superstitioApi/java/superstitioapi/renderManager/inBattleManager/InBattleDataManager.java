package superstitioapi.renderManager.inBattleManager;

import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.hangUpCard.HangUpCardGroup;
import superstitioapi.pet.PetManager;
import superstitioapi.powers.barIndepend.BarRenderManager;

import java.util.*;
import java.util.function.Consumer;

public class InBattleDataManager {
    public static Map<UUID, Integer> costMap = new HashMap<>();
    public static ArrayList<ISubscriber> subscribeManageGroups = new ArrayList<>();

    public static void InitializeAtStartOfBattle() {
        ResetAll();
        AutoDoneInstantAction.addToTopAbstract(() ->
        {
            Subscribe(new HangUpCardGroup(AbstractDungeon.player.hb));
            Subscribe(new BarRenderManager());
            Subscribe(new PetManager());
        });
    }


    public static void ClearOnEndOfBattle() {
        ResetAll();
    }

    private static void ResetAll() {
        RenderInBattle.clearAll();
        costMap = new HashMap<>();
        subscribeManageGroups.clear();
    }

    public static <T extends ISubscriber> void ApplyAll(Consumer<T> subscriberConsumer, Class<T> tClass) {
        subscribeManageGroups.stream()
                .filter(tClass::isInstance)
                .map(object -> (T) object)
                .forEach(subscriberConsumer);
    }

//    public static <T extends ISubscriber> void ApplyAllForPower(Consumer<T> subscriberConsumer, Class<T> tClass) {
//        Manager.stream()
//                .filter(tClass::isInstance)
//                .map(object -> (T) object)
//                .forEach(subscriberConsumer);
//    }


    public static void InitializeAtStartOfTurn() {
    }

    @SafeVarargs
    public static <T extends ISubscriber> void Subscribe(T... objectToAdd) {
        subscribeManageGroups.addAll(Arrays.asList(objectToAdd));
    }

    @SafeVarargs
    public static <T extends ISubscriber> void UnSubscribe(T... objectToAdd) {
        subscribeManageGroups.removeAll(Arrays.asList(objectToAdd));
    }

    public static Optional<HangUpCardGroup> getHangUpCardOrbGroup() {
        return InBattleDataManager.subscribeManageGroups.stream()
                .filter(orbGroup -> orbGroup instanceof HangUpCardGroup)
                .map(orbGroup -> (HangUpCardGroup) orbGroup).findAny();
    }

    public static Optional<BarRenderManager> getBarRenderManager() {
        return InBattleDataManager.subscribeManageGroups.stream()
                .filter(barGroup -> barGroup instanceof BarRenderManager)
                .map(barGroup -> (BarRenderManager) barGroup).findAny();
    }

    public static Optional<PetManager> getPetManager() {
        return InBattleDataManager.subscribeManageGroups.stream()
                .filter(petGroup -> petGroup instanceof PetManager)
                .map(petGroup -> (PetManager) petGroup).findAny();
    }
}
