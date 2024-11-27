package superstitio.relics.d_uncommon

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.LoseStrengthPower
import com.megacrit.cardcrawl.powers.StrengthPower
import superstitio.DataManager
import superstitio.powers.SexualHeat
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm
import superstitio.relics.SuperstitioRelic
import superstitioapi.utils.setDescriptionArgs

class NippleRings : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND), OnOrgasm_onOrgasm
{
    override fun atBattleStart()
    {
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(Amount)
    }

    override fun onOrgasm(SexualHeatPower: SexualHeat)
    {
        this.flash()
        this.addToBot(RelicAboveCreatureAction(AbstractDungeon.player, this))
        this.addToTop(
            ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                StrengthPower(AbstractDungeon.player, Amount)
            )
        )
        this.addToTop(
            ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                LoseStrengthPower(AbstractDungeon.player, Amount)
            )
        )
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(NippleRings::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.UNCOMMON

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT
        private const val Amount = 1
    }
}