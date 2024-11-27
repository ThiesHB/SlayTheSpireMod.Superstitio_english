package superstitioapi.powers.interfaces.invisible

import com.megacrit.cardcrawl.powers.AbstractPower

interface InvisiblePower_InvisibleIconAndAmount {
    fun checkShouldInvisibleIcon(): Boolean {
        return true
    }

    fun checkShouldInvisibleAmount(): Boolean {
        return true
    }

    companion object {
        fun shouldInvisibleIcon(power: AbstractPower?): Boolean {
            if (power is InvisiblePower_InvisibleIconAndAmount) return (power as InvisiblePower_InvisibleIconAndAmount).checkShouldInvisibleIcon()
            return false
        }

        fun shouldInvisibleAmount(power: AbstractPower?): Boolean {
            if (power is InvisiblePower_InvisibleIconAndAmount) return (power as InvisiblePower_InvisibleIconAndAmount).checkShouldInvisibleAmount()
            return false
        }
    }
}
