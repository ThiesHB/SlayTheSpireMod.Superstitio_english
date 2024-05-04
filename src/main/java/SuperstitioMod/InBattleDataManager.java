package SuperstitioMod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InBattleDataManager {
    public static Map<UUID, Integer> costMap = new HashMap<>();
    public static boolean InOrgasm = false;

    public static int OrgasmTimesInTurn = 0;

    public static int OrgasmTimesTotal = 0;

    public static void InitializeAtStartOfBattle() {
        InOrgasm = false;
        OrgasmTimesInTurn = 0;
        OrgasmTimesTotal = 0;
        costMap = new HashMap<>();
    }

    public static void InitializeAtStartOfTurn() {
        OrgasmTimesInTurn = 0;
    }
}
