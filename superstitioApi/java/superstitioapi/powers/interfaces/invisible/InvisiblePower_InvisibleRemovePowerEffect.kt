//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package superstitioapi.powers.interfaces.invisible

import com.megacrit.cardcrawl.powers.AbstractPower

interface InvisiblePower_InvisibleRemovePowerEffect {
    fun checkShouldInvisibleRemovePowerEffect(): Boolean {
        return true
    }

    companion object {
        fun shouldInvisibleRemovePowerEffect(power: AbstractPower?): Boolean {
            if (power is InvisiblePower_InvisibleRemovePowerEffect) return (power as InvisiblePower_InvisibleRemovePowerEffect).checkShouldInvisibleRemovePowerEffect()
            return false
        }
    }
}

