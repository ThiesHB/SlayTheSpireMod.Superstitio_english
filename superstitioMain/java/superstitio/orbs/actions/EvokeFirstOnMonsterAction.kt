package superstitio.orbs.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.core.Settings
import superstitio.orbs.orbgroup.OrbGroup

class EvokeFirstOnMonsterAction(target: OrbGroup, amt: Int) : AbstractGameAction() {
    var targetOrbGroup: OrbGroup = target

    init {
        this.amount = amt
        this.duration = Settings.ACTION_DUR_XFAST
    }

    override fun update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            for (i in 0 until this.amount) {
                targetOrbGroup.evokeFirstOrb()
            }
        }
        this.tickDuration()
    }
}
