package superstitio.relics.d_uncommon

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.DexterityPower
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.relics.SuperstitioRelic
import superstitio.relics.interFace.Countup
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.setDescriptionArgs

class CumOnShoes : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND), Countup {
    override fun atBattleStart() {
        setCounter(Countup.Zero)
    }

    override fun updateDescriptionArgs() {
        setDescriptionArgs(Companion.MaxNum)
    }

    override fun onPlayCard(c: AbstractCard, m: AbstractMonster) {
        if (c is FuckJob_Card) CountAdd()
    }

    override fun onCountMax() {
        this.flash()
        this.addToBot(RelicAboveCreatureAction(AbstractDungeon.player, this))
        ActionUtility.addToBot_applyPower(DexterityPower(AbstractDungeon.player, 1))
    }

    override fun getMaxNum(): Int {
        return MaxNum
    }

    companion object {
        val ID: String = DataManager.MakeTextID(CumOnShoes::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.UNCOMMON

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT
        const val MaxNum: Int = 10
    }
}