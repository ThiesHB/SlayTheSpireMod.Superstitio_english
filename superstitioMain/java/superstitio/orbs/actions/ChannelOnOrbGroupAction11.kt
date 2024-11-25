package superstitio.orbs.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.orbs.AbstractOrb
import superstitio.orbs.orbgroup.OrbGroup

class ChannelOnOrbGroupAction(target: OrbGroup, var orb: AbstractOrb) : AbstractGameAction() {
    var targetOrbGroup: OrbGroup = target

    init {
        this.duration = Settings.ACTION_DUR_FAST
    }

    override fun update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            targetOrbGroup.channelOrb(this.orb)
            this.isDone = true
        }
    }
}
