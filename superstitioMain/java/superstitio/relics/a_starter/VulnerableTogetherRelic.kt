package superstitio.relics.a_starter

import basemod.AutoAdd.Seen
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitio.DataManager
import superstitio.cards.general.TempCard.VulnerableTogether
import superstitio.relics.SuperstitioRelic
import superstitioapi.utils.ActionUtility

@Seen
class VulnerableTogetherRelic : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND) {
    override fun atBattleStart() {
        this.flash()
        ActionUtility.addToBot(RelicAboveCreatureAction(AbstractDungeon.player, this))
        ActionUtility.addToBot_makeTempCardInBattle(VulnerableTogether(), ActionUtility.BattleCardPlace.Hand)
    }

    override fun updateDescriptionArgs() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(VulnerableTogetherRelic::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.STARTER

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT
    }
}