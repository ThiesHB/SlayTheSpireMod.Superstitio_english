package superstitio.delayHpLose

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.combat.HealEffect
import superstitio.DataManager

class DelayHpLosePower_HealOnVictory(owner: AbstractCreature, amount: Int) : DelayHpLosePower_ApplyAtEndOfRound(
    POWER_ID, owner, amount
)
{
    override fun onVictory()
    {
        this.isRemovedForApplyDamage = true
        //        AutoDoneInstantAction.addToTopAbstract(() -> {
        AbstractDungeon.effectsQueue.add(HealEffect(owner.hb.cX - owner.animX, owner.hb.cY, amount))
        playRemoveEffect()
        this.amount = 0
        //        });
//        addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(DelayHpLosePower_HealOnVictory::class.java)
    }
}
