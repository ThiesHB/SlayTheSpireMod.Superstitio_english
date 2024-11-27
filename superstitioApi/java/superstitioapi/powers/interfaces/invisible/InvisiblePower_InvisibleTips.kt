//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package superstitioapi.powers.interfaces.invisible

import com.megacrit.cardcrawl.powers.AbstractPower

interface InvisiblePower_InvisibleTips
{
    fun checkShouldInvisibleTips(): Boolean
    {
        return true
    }

    companion object
    {
        fun shouldInvisibleTips(power: AbstractPower): Boolean
        {
            if (power is InvisiblePower_InvisibleTips)
                return power.checkShouldInvisibleTips()
            return false
        }
    }
}

