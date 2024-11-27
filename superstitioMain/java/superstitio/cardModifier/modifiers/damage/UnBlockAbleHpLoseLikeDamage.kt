package superstitio.cardModifier.modifiers.damage

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitio.DataManager.CanOnlyDamageDamageType
import superstitio.cardModifier.modifiers.AbstractLupaDamage

class UnBlockAbleHpLoseLikeDamage : AbstractLupaDamage(ID) {
    override fun ignoresBlock(target: AbstractCreature): Boolean {
        return true
    }

    override fun atDamageGive(damage: Float, type: DamageType, target: AbstractCreature, card: AbstractCard): Float {
        return super.atDamageGive(damage, CanOnlyDamageDamageType.NoTriggerLupaAndMasoRelicHpLose, target, card)
    }

    override fun ignoresThorns(): Boolean {
        return true
    }

    override fun makeCopy(): AbstractDamageModifier {
        return UnBlockAbleHpLoseLikeDamage()
    }

    companion object {
        val ID: String = DataManager.MakeTextID(UnBlockAbleHpLoseLikeDamage::class.java)

        fun damageInfo(instigator: Any?, amount: Int): DamageInfo {
            return BindingHelper.makeInfo(
                DamageModContainer(instigator, UnBlockAbleHpLoseLikeDamage()), null, amount,
                CanOnlyDamageDamageType.NoTriggerLupaAndMasoRelicHpLose
            )
        }
    }
}