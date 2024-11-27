package superstitio.relics.c_common

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.RainingGoldEffect
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.relics.SuperstitioRelic
import superstitio.relics.interFace.Countup
import superstitioapi.utils.setDescriptionArgs

class Prostitution : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND), Countup {
    override fun atBattleStart() {
        setCounter(Countup.Zero)
    }

    override fun onUseCard(targetCard: AbstractCard?, useCardAction: UseCardAction?) {
        if (targetCard is FuckJob_Card)
            CountAdd()
    }

    override fun updateDescriptionArgs() {
        setDescriptionArgs(MaxNum, CashAmount)
    }

    override fun onCountMax() {
        this.flash()
        AbstractDungeon.effectList.add(RainingGoldEffect(CashAmount * 2, true))
        this.addToBot(RelicAboveCreatureAction(AbstractDungeon.player, this))
        AbstractDungeon.player.gainGold(CashAmount)
    }

    override fun getMaxNum(): Int = MaxNum

    companion object {
        val ID: String = DataManager.MakeTextID(Prostitution::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.COMMON

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT
        const val MaxNum: Int = 5
        private const val CashAmount = 1
    }
}