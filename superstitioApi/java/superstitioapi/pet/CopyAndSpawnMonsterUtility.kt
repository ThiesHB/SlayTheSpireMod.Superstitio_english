package superstitioapi.pet

import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_S
import superstitioapi.Logger
import java.lang.Boolean
import kotlin.Any
import kotlin.Exception
import kotlin.arrayOfNulls

object CopyAndSpawnMonsterUtility {
    fun motherFuckerWhyIShouldUseThisToCopyMonster(amClass: Class<out AbstractMonster?>): AbstractMonster {
        if (amClass == AcidSlime_S::class.java) {
            return AcidSlime_S(0.0f, 0.0f, 0)
        }
        if (amClass == SpikeSlime_S::class.java) {
            return SpikeSlime_S(0.0f, 0.0f, 0)
        }
        if (amClass.name == "monsters.pet.ScapeGoatPet") {
            return ApologySlime()
        }
        val con = amClass.declaredConstructors
        if (con.size > 0) {
            val c = con[0]
            try {
                val paramCt = c.parameterCount
                val params = c.parameterTypes
                val paramz = arrayOfNulls<Any>(paramCt)
                for (i in 0 until paramCt) {
                    val param = params[i]
                    if (Integer.TYPE.isAssignableFrom(param)) {
                        paramz[i] = 1
                    } else if (Boolean.TYPE.isAssignableFrom(param)) {
                        paramz[i] = true
                    } else if (java.lang.Float.TYPE.isAssignableFrom(param)) {
                        paramz[i] = 0.0f
                    }
                }
                return c.newInstance(*paramz) as AbstractMonster
            } catch (e: Exception) {
                Logger.warning("Error occurred while trying to instantiate class: " + c.name)
                Logger.warning("Reverting to Apology Slime")
                return ApologySlime()
            }
        }
        Logger.info("Failed to create monster, returning Apology Slime")
        return ApologySlime()
    }
}
