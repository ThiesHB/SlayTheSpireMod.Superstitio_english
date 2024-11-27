package superstitio.cardModifier.modifiers.damage

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitio.cardModifier.modifiers.AbstractLupaDamage
import superstitio.powers.SexualDamage

class UnBlockAbleSexDamage : AbstractLupaDamage(ID)
{
    override fun ignoresBlock(target: AbstractCreature): Boolean
    {
        return true
    }

    override fun onAttackToChangeDamage(info: DamageInfo, damageAmount: Int, target: AbstractCreature): Int
    {
        if (damageAmount > 0)
        {
            this.addToTop(ApplyPowerAction(target, info.owner, SexualDamage(target, damageAmount, info.owner)))
        }
        return 0
    }

    override fun makeCopy(): AbstractDamageModifier
    {
        return UnBlockAbleSexDamage()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(UnBlockAbleSexDamage::class.java)
        private const val OnlyDealDamage = 1
    }
}