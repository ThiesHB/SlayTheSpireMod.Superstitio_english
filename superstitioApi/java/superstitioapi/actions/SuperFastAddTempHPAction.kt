package superstitioapi.actions

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.combat.HealEffect

class SuperFastAddTempHPAction(target: AbstractCreature, source: AbstractCreature, amount: Int) :
    AutoDoneInstantAction() {
    init {
        this.setValues(target, source, amount)
        this.actionType = ActionType.HEAL
    }

    override fun autoDoneUpdate() {
        TempHPField.tempHp[target] = TempHPField.tempHp[target] + this.amount
        if (this.amount > 0) {
            AbstractDungeon.effectsQueue.add(
                HealEffect(
                    target.hb.cX - target.animX,
                    target.hb.cY, this.amount
                )
            )
            target.healthBarUpdatedEvent()
        }
    }
}
