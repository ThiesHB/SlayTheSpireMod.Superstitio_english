package superstitio

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitio.characters.BaseCharacter
import superstitio.orbs.orbgroup.SexMarkOrbGroup
import superstitio.powers.SexualHeat
import superstitio.powers.SexualHeat.Orgasm
import superstitioapi.renderManager.inBattleManager.InBattleDataManager
import superstitioapi.utils.CreatureUtility

object InBattleDataManager {
    //    public static boolean InOrgasm = false;
    var OrgasmTimesInTurn: Int = 0

    var NailExtractionPlayedInTurn: Int = 0

    var OrgasmTimesTotal: Int = 0
    @JvmStatic
    fun InitializeAtStartOfBattle() {
        ResetAll()
        if (AbstractDungeon.player is BaseCharacter) SexualHeat.addToBot_addSexualHeat(
            AbstractDungeon.player,
            0
        )
    }
    @JvmStatic
    fun ClearOnEndOfBattle() {
        ResetAll()
    }

    fun getSexMarkOrbGroup(): SexMarkOrbGroup? {
        return InBattleDataManager.subscribeManageGroups
            .filterIsInstance<SexMarkOrbGroup>().firstOrNull()
    }
    @JvmStatic
    fun InitializeAtStartOfTurn() {
        OrgasmTimesInTurn = 0
        NailExtractionPlayedInTurn = 0
    }

    private fun ResetAll() {
        CreatureUtility.forPlayerAndEachMonsters(Orgasm::endOrgasm)

        OrgasmTimesInTurn = 0
        OrgasmTimesTotal = 0
        NailExtractionPlayedInTurn = 0
    }
}
