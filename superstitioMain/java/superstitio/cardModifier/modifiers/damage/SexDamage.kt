package superstitio.cardModifier.modifiers.damage

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitio.cardModifier.modifiers.AbstractLupaDamage
import superstitio.powers.SexualDamage

class SexDamage : AbstractLupaDamage(ID) {
    //    public SexDamage(String id) {
    //        super(id);
    //    }
    // 在这里使用onAttackToChangeDamage将获取本应造成的伤害，并允许我们修改它，返回0
    // 由于我们在这一函数处能获取伤害量，我们也可以简单地将等于这个量的power应用于目标，或进行其他操作
    override fun onAttackToChangeDamage(info: DamageInfo, damageAmount: Int, target: AbstractCreature): Int {
        if (damageAmount <= OnlyDealDamage) return damageAmount
        if (target is AbstractPlayer) return damageAmount / 2
        this.addToTop(
            ApplyPowerAction(
                target,
                info.owner,
                SexualDamage(target, damageAmount - OnlyDealDamage, info.owner)
            )
        )
        return OnlyDealDamage
    }

    override fun makeCopy(): AbstractDamageModifier {
        return SexDamage()
    }

    companion object {
        val ID: String = DataManager.MakeTextID(SexDamage::class.java)
        private const val OnlyDealDamage = 1
    }
}