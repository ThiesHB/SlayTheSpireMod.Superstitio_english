package superstitio.relics.e_rare

import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitio.DataManager
import superstitio.powers.SexualHeat
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onSquirt
import superstitio.relics.SuperstitioRelic
import superstitio.relics.interFace.Countdown
import superstitioapi.utils.setDescriptionArgs

class EjaculationMaster : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND), OnOrgasm_onSquirt, Countdown
{
    override fun atBattleStart()
    {
        setCounter(Countdown.Zero)
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(Companion.StarterNum, DrawAmount)
    }

    override fun onCountZero()
    {
        this.flash()
        this.addToBot(RelicAboveCreatureAction(AbstractDungeon.player, this))
        this.addToBot(DrawCardAction(DrawAmount))
    }

    override fun getStarterNum(): Int = StarterNum

    override fun onSquirt(SexualHeatPower: SexualHeat, card: AbstractCard)
    {
        CountReduce()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(EjaculationMaster::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.RARE

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT
        private const val DrawAmount = 1
        const val StarterNum: Int = 3
    }
}