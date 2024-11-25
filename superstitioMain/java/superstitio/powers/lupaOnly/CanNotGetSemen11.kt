package superstitio.powers.lupaOnly

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitio.SuperstitioImg.NoNeedImg
import superstitio.cards.lupa.OnAddSemenPower
import superstitio.powers.AbstractSuperstitioPower
import superstitioapi.powers.AllCardCostModifier_PerEnergy

@NoNeedImg
class CanNotGetSemen : AbstractSuperstitioPower(POWER_ID, AbstractDungeon.player, -1), OnAddSemenPower {
    override fun updateDescriptionArgs() {
    }

    override fun onAddSemen_shouldApply(power: AbstractPower?): Boolean {
        return false
    }

    companion object {
        val POWER_ID: String = DataManager.MakeTextID(AllCardCostModifier_PerEnergy::class.java)
    }
}
