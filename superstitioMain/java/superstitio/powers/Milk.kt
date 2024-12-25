package superstitio.powers

import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitio.DataManager
import superstitioapi.actions.SuperFastAddTempHPAction
import superstitioapi.utils.addToBot_removeSelf
import superstitioapi.utils.setDescriptionArgs

class Milk(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(POWER_ID, owner, amount)
{
    override fun updateDescriptionArgs()
    {
        this.setDescriptionArgs(amount, REMOVE_EACH_TIME)
    }

    override fun onAttack(info: DamageInfo, damageAmount: Int, target: AbstractCreature)
    {
        if (info.type != DamageType.NORMAL) return
        //        if (damageAmount <= 0) return;
        this.flash()
        //        addToBot(new SuperFastAddTempHPAction(target, owner, this.amount));
        addToBot(SuperFastAddTempHPAction(AbstractDungeon.player, owner, this.amount))
        addToBot_removeSelf()
        //        addToBot_reducePowerToOwner(Milk.POWER_ID, this.amount / 2);
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(Milk::class.java)
        const val REMOVE_EACH_TIME: Int = 1
    }
}
