package superstitio.cardModifier.modifiers.damage

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitio.cardModifier.modifiers.AbstractLupaDamage

class SelfDamage : AbstractLupaDamage(ID)
{
    override fun onAttackToChangeDamage(info: DamageInfo?, damageAmount: Int, target: AbstractCreature): Int
    {
        if (target is AbstractPlayer) return super.onAttackToChangeDamage(info, damageAmount, target) / 2
        return super.onAttackToChangeDamage(info, damageAmount, target)
    }

    override fun makeCopy(): AbstractDamageModifier
    {
        return SelfDamage()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(SelfDamage::class.java)
    }
}