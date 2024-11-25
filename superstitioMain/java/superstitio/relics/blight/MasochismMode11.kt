package superstitio.relics.blight

import basemod.AutoAdd.Seen
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitio.DataManager
import superstitio.DataManager.CanOnlyDamageDamageType
import superstitio.delayHpLose.DelayHpLosePower
import superstitio.powers.AbstractSuperstitioPower
import superstitio.powers.SexualHeat
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasmFirst
import superstitio.relics.SuperstitioRelic
import superstitio.relics.blight.MasochismMode.MasochismModePower
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.relicToBlight.InfoBlight
import superstitioapi.relicToBlight.InfoBlight.BecomeInfoBlight
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.ActionUtility.VoidSupplier
import java.util.function.Consumer

@Seen
class MasochismMode : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND), BecomeInfoBlight, OnOrgasm_onOrgasmFirst {
    override fun onOrgasmFirst(SexualHeatPower: SexualHeat) {
        DelayHpLosePower.addToBot_removePower(ReduceDelayHpLose, AbstractDungeon.player, true)
    }

    override fun updateDescriptionArgs() {
    }

    override fun getDescriptionStrings(): String {
        return String.format(DESCRIPTIONS[0], MasochismModeDamageNeed, MasochismModeSexualHeatRate, ReduceDelayHpLose)
    }

    override fun atBattleStart() {
        ActionUtility.addToBot_applyPower(MasochismModePower(AbstractDungeon.player))
    }

    override fun obtain() {
        InfoBlight.obtain(this)
    }

    override fun instantObtain(p: AbstractPlayer, slot: Int, callOnEquip: Boolean) {
        InfoBlight.instanceObtain(this, callOnEquip)
    }

    override fun instantObtain() {
        InfoBlight.instanceObtain(this, true)
    }

    private class MasochismModePower(owner: AbstractCreature) : AbstractSuperstitioPower(POWER_ID, owner, -1),
        InvisiblePower {
        override fun wasHPLost(info: DamageInfo, damageAmount: Int) {
            if (ActionUtility.isNotInBattle) return
            if (info.type == CanOnlyDamageDamageType.UnBlockAbleDamageType) return
            if (info.type == CanOnlyDamageDamageType.NoTriggerLupaAndMasoRelicHpLose) return
            this.flash()
            if (damageAmount < MasochismModeDamageNeed) return
            AddSexualHeat(damageAmount / MasochismModeDamageNeed * MasochismModeSexualHeatRate)
        }

        override fun updateDescriptionArgs() {
        }

        companion object {
            val POWER_ID: String = DataManager.MakeTextID(MasochismModePower::class.java)
        }
    }

    companion object {
        val ID: String = DataManager.MakeTextID(MasochismMode::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.SPECIAL

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT
        private const val MasochismModeDamageNeed = 3

        private const val MasochismModeSexualHeatRate = 2
        private const val ReduceDelayHpLose = 5

        private fun AddSexualHeat(amount: Int) {
            SexualHeat.useConsumer_addSexualHeat(
                AbstractDungeon.player,
                amount,
                Consumer<VoidSupplier>(AutoDoneInstantAction.Companion::addToTopAbstract)
            )
        }
    }
}