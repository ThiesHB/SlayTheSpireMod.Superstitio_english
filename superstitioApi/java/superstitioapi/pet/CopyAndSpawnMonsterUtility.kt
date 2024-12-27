package superstitioapi.pet

import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_S
import superstitioapi.Logger
import java.lang.Boolean
import java.lang.Float
import kotlin.Any
import kotlin.Exception
import kotlin.arrayOfNulls

object CopyAndSpawnMonsterUtility
{
    fun motherFuckerWhyIShouldUseThisToCopyMonster(amClass: Class<out AbstractMonster?>): AbstractMonster
    {
        fun tryMakeMonster(): AbstractMonster
        {
            val constructors = amClass.declaredConstructors
            if (constructors.isEmpty())
            {
                Logger.info("Failed to create monster, returning Apology Slime")
                return ApologySlime()
            }
            val c = constructors[0]
            try
            {
                val paramCt = c.parameterCount
                val params = c.parameterTypes
                val paramz = arrayOfNulls<Any>(paramCt)
                for (i in 0 until paramCt)
                {
                    val param = params[i]
                    if (Integer.TYPE.isAssignableFrom(param))
                        paramz[i] = 1
                    else if (Boolean.TYPE.isAssignableFrom(param))
                        paramz[i] = true
                    else if (Float.TYPE.isAssignableFrom(param))
                        paramz[i] = 0.0f
                }
                return c.newInstance(*paramz) as AbstractMonster
            }
            catch (e: Exception)
            {
                Logger.warning("Error occurred while trying to instantiate class: " + c.name)
                Logger.warning("Reverting to Apology Slime")
                return ApologySlime()
            }
        }
        return when
        {
            amClass == AcidSlime_S::class.java          -> AcidSlime_S(0.0f, 0.0f, 0)
            amClass == SpikeSlime_S::class.java         -> SpikeSlime_S(0.0f, 0.0f, 0)
            amClass.name == "monsters.pet.ScapeGoatPet" -> ApologySlime()
            else                                        -> tryMakeMonster()
        }
    }
}
