package superstitio;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.orbs.orbgroup.HangUpCardGroup;
import superstitio.orbs.orbgroup.OrbGroup;
import superstitio.orbs.orbgroup.SexMarkOrbGroup;
import superstitio.powers.barIndepend.BarRenderGroup;
import superstitio.powers.barIndepend.RenderInBattle;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class InBattleDataManager {
    public static Map<UUID, Integer> costMap = new HashMap<>();
    public static boolean InOrgasm = false;

    public static int OrgasmTimesInTurn = 0;

    public static int OrgasmTimesTotal = 0;
    public static ArrayList<OrbGroup> orbGroups = new ArrayList<>();
    public static ArrayList<BarRenderGroup> barGroups = new ArrayList<>();

    public static ArrayList<ArrayList<ISubscriber>> subscribeManageGroups = new ArrayList<>();

    public static void InitializeAtStartOfBattle() {
        ResetAll();
        SetupList(orbGroups, new SexMarkOrbGroup(AbstractDungeon.player.hb), new HangUpCardGroup(AbstractDungeon.player.hb));
        SetupList(barGroups, new BarRenderGroup(AbstractDungeon.player));
        subscribeManageGroups.forEach(groups -> groups.forEach(InBattleDataManager::trySubScribe));
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
        subscribeManageGroups.forEach(groups -> groups.forEach(InBattleDataManager::tryUnSubScribe));
        subscribeManageGroups.clear();
    }

    public static <T extends ISubscriber> void ApplyAll(Consumer<T> subscriberConsumer, Class<T> tClass) {
        subscribeManageGroups.forEach(objects -> {
            for (ISubscriber object : objects) {
                if (objects.getClass().isAssignableFrom(tClass)) {
                    subscriberConsumer.accept((T) object);
                }
            }
        });
    }


    public static void InitializeAtStartOfTurn() {
        OrgasmTimesInTurn = 0;
    }

    @SafeVarargs
    public static <T extends ISubscriber> void SetupList(ArrayList<T> listTarget, T... objectToAdd) {
        listTarget.clear();
        listTarget.addAll(Arrays.asList(objectToAdd));
        subscribeManageGroups.add((ArrayList<ISubscriber>) listTarget.stream().map(object -> (ISubscriber) object).collect(Collectors.toList()));
    }

    private static void tryUnSubScribe(Object object) {
        if (!(object instanceof ISubscriber)) return;
        ISubscriber iSubscriber = (ISubscriber) object;
        BaseMod.unsubscribe(iSubscriber);
    }

    private static void trySubScribe(Object object) {
        if (!(object instanceof ISubscriber)) return;
        ISubscriber iSubscriber = (ISubscriber) object;
        BaseMod.subscribe(iSubscriber);
    }
}
