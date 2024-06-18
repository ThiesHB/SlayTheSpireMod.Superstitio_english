package superstitio;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.characters.BaseCharacter;
import superstitio.orbs.orbgroup.SexMarkOrbGroup;
import superstitio.powers.SexualHeat;
import superstitioapi.utils.CreatureUtility;

import java.util.Optional;

public class InBattleDataManager {
//    public static boolean InOrgasm = false;

    public static int OrgasmTimesInTurn = 0;

    public static int NailExtractionPlayedInTurn = 0;

    public static int OrgasmTimesTotal = 0;

    public static void InitializeAtStartOfBattle() {
        ResetAll();
        if (AbstractDungeon.player instanceof BaseCharacter)
            SexualHeat.addToBot_addSexualHeat(AbstractDungeon.player, 0);
    }

    public static void ClearOnEndOfBattle() {
        ResetAll();
    }

    private static void ResetAll() {
        CreatureUtility.forPlayerAndEachMonsters(SexualHeat.Orgasm::endOrgasm);

        OrgasmTimesInTurn = 0;
        OrgasmTimesTotal = 0;
        NailExtractionPlayedInTurn = 0;
    }

    public static Optional<SexMarkOrbGroup> getSexMarkOrbGroup() {
        return superstitioapi.InBattleDataManager.subscribeManageGroups.stream()
                .filter(orbGroup -> orbGroup instanceof SexMarkOrbGroup)
                .map(orbGroup -> (SexMarkOrbGroup) orbGroup).findAny();
    }


    public static void InitializeAtStartOfTurn() {
        OrgasmTimesInTurn = 0;
        NailExtractionPlayedInTurn = 0;
    }
}
