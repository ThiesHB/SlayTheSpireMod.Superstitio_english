package superstitio;

import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.orbs.orbgroup.HangUpCardGroup;
import superstitio.orbs.orbgroup.SexMarkOrbGroup;
import superstitio.powers.patchAndInterface.barIndepend.BarRenderGroup;
import superstitio.powers.patchAndInterface.barIndepend.RenderInBattle;

import java.util.*;
import java.util.function.Consumer;

public class InBattleDataManager {
    public static Map<UUID, Integer> costMap = new HashMap<>();
    public static boolean InOrgasm = false;

    public static int OrgasmTimesInTurn = 0;

    public static int OrgasmTimesTotal = 0;
    //    public static ArrayList<OrbGroup> orbGroups = new ArrayList<>();
//    public static ArrayList<BarRenderGroup> barGroups = new ArrayList<>();
    public static ArrayList<ISubscriber> subscribeManageGroups = new ArrayList<>();

    public static void InitializeAtStartOfBattle() {
        ResetAll();
        Subscribe(new HangUpCardGroup(AbstractDungeon.player.hb));
        Subscribe(new SexMarkOrbGroup(AbstractDungeon.player.hb));
        Subscribe(new BarRenderGroup(AbstractDungeon.player));
//        subscribeManageGroups.forEach(groups -> groups.forEach(InBattleDataManager::trySubScribe));
    }

    public static void ClearOnEndOfBattle() {
        ResetAll();
    }

    private static void ResetAll() {
        RenderInBattle.RENDER_IN_BATTLES.clear();
        InOrgasm = false;
        OrgasmTimesInTurn = 0;
        OrgasmTimesTotal = 0;
        costMap = new HashMap<>();
        subscribeManageGroups.clear();
    }

    public static <T extends ISubscriber> void ApplyAll(Consumer<T> subscriberConsumer, Class<T> tClass) {
        subscribeManageGroups.stream()
                .filter(tClass::isInstance)
                .map(object -> (T) object)
                .forEach(subscriberConsumer);
    }

    public static Optional<HangUpCardGroup> getHangUpCardOrbGroup() {
        return InBattleDataManager.subscribeManageGroups.stream()
                .filter(orbGroup -> orbGroup instanceof HangUpCardGroup)
                .map(orbGroup -> (HangUpCardGroup) orbGroup).findAny();
    }

    public static Optional<SexMarkOrbGroup> getSexMarkOrbGroup() {
        return InBattleDataManager.subscribeManageGroups.stream()
                .filter(orbGroup -> orbGroup instanceof SexMarkOrbGroup)
                .map(orbGroup -> (SexMarkOrbGroup) orbGroup).findAny();
    }


    public static void InitializeAtStartOfTurn() {
        OrgasmTimesInTurn = 0;
    }

    @SafeVarargs
    public static <T extends ISubscriber> void Subscribe(T... objectToAdd) {
        subscribeManageGroups.addAll(Arrays.asList(objectToAdd));
    }

    @SafeVarargs
    public static <T extends ISubscriber> void UnSubscribe(T... objectToAdd) {
        subscribeManageGroups.removeAll(Arrays.asList(objectToAdd));
    }
}
