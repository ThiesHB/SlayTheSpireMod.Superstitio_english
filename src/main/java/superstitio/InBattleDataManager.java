package superstitio;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.orbs.orbgroup.OrbGroup;
import superstitio.orbs.orbgroup.SexMarkOrbGroup;
import superstitio.powers.barIndepend.RenderInBattle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InBattleDataManager {
    public static Map<UUID, Integer> costMap = new HashMap<>();
    public static boolean InOrgasm = false;

    public static int OrgasmTimesInTurn = 0;

    public static int OrgasmTimesTotal = 0;
    public static ArrayList<OrbGroup> orbGroups = new ArrayList<>();

    public static void InitializeAtStartOfBattle() {
        ResetAll();

        orbGroups.add(new SexMarkOrbGroup(AbstractDungeon.player.hb));
    }

    private static void ResetAll() {
        RenderInBattle.RENDER_IN_BATTLES.clear();
        InOrgasm = false;
        OrgasmTimesInTurn = 0;
        OrgasmTimesTotal = 0;
        costMap = new HashMap<>();
        orbGroups = new ArrayList<>();
    }


    public static void InitializeAtStartOfTurn() {
        OrgasmTimesInTurn = 0;
    }
}
