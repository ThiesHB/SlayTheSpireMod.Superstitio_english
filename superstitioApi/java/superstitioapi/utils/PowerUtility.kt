package superstitioapi.utils

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.powers.TheBombPower
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect
import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect
import com.megacrit.cardcrawl.vfx.combat.PowerExpireTextEffect
import superstitioapi.Logger
import superstitioapi.powers.interfaces.CopyAblePower

object PowerUtility
{
    val BubbleMessageHigher_HEIGHT: Float = 50.0f * Settings.scale
    val specialCopyPower: Array<Class<out AbstractPower>> =
        arrayOf(TheBombPower::class.java)

    @JvmStatic
    fun foreachPower(consumer: (AbstractPower) -> Unit)
    {
        AbstractDungeon.player.powers.forEach(consumer)
        AbstractDungeon.getMonsters().monsters.forEach { monster: AbstractMonster ->
            monster.powers.forEach(consumer)
        }
    }

//    @JvmStatic
//    fun testEachPower(consumer: (AbstractPower) -> Boolean) : Boolean
//    {
//        AbstractDungeon.player.powers.forEach(consumer)
//        AbstractDungeon.getMonsters().monsters.forEach { monster: AbstractMonster ->
//            monster.powers.forEach(consumer)
//        }
//    }

    @JvmStatic
    fun countPower(predicate: (AbstractPower) -> Boolean, creature: AbstractCreature): Int
    {
        var count = 0
        creature.powers.forEach {
            if (predicate(it))
                count++
        }
        return count
    }


    fun RemovePower(target: AbstractCreature, needRemovePower: AbstractPower)
    {
        AbstractDungeon.effectList.add(
            PowerExpireTextEffect(
                target.hb.cX - target.animX, target.hb.cY + target.hb.height / 2.0f,
                needRemovePower.name, needRemovePower.region128
            )
        )
        needRemovePower.onRemove()
        target.powers.remove(needRemovePower)
        AbstractDungeon.onModifyPower()
        for (orb in AbstractDungeon.player.orbs)
        {
            orb.updateDescription()
        }
    }

    fun BubbleMessage(power: AbstractPower, isDeBuffVer: Boolean, message: String?)
    {
        BubbleMessage(power.owner.hb, isDeBuffVer, message, power.owner.animX, 0f)
    }

    @JvmOverloads
    fun BubbleMessage(
        hitbox: Hitbox,
        isDeBuffVer: Boolean,
        message: String?,
        XOffset: Float = 0f,
        YOffset: Float = 0f
    )
    {
        if (isDeBuffVer)
        {
            AbstractDungeon.effectList.add(
                PowerDebuffEffect(
                    hitbox.cX - XOffset,
                    hitbox.cY + hitbox.height / 2.0f + YOffset, message
                )
            )
        }
        else
        {
            AbstractDungeon.effectList.add(
                PowerBuffEffect(
                    hitbox.cX - XOffset,
                    hitbox.cY + hitbox.height / 2.0f + YOffset, message
                )
            )
        }
    }

    fun BubbleMessageHigher(power: AbstractPower, isDeBuffVer: Boolean, message: String?)
    {
        BubbleMessage(power.owner.hb, isDeBuffVer, message, power.owner.animX, BubbleMessageHigher_HEIGHT)
    }

    fun <T> mergeAndSumMaps(map1: MutableMap<T, Int>, map2: MutableMap<T, Int>): MutableMap<T, Int>
    {
        val result: MutableMap<T, Int> = HashMap()

        // 遍历第一个映射
        for ((key, value1) in map1)
        {
            val value2 = map2.getOrDefault(key, 0) // 获取第二个映射中对应的值，如果不存在则默认为0
            result[key] = value1 + value2
        }

        // 遍历第二个映射中剩余的键值对
        for ((key, value) in map2)
        {
            if (!result.containsKey(key))
            {
                result[key] = value
            }
        }

        return result
    }
}

@Suppress("UnusedReceiverParameter")
fun AbstractPower.addToBot_removeSpecificPower(power: AbstractPower)
{
    ActionUtility.addToBot_removeSpecificPower(power, power.owner)
}

fun AbstractPower.addToBot_removeSelf()
{
    ActionUtility.addToBot_removeSpecificPower(this, this.owner)
}

internal fun AbstractPower.action_AutoRemoveOne(power: AbstractPower?): AbstractGameAction
{
    this.flash()
    return if (this.amount == 0)
    {
        RemoveSpecificPowerAction(this.owner, this.owner, power)
    }
    else
    {
        ReducePowerAction(this.owner, this.owner, power, 1)
    }
}

fun AbstractPower.addToBot_AutoRemoveOne(power: AbstractPower?) =
    this.action_AutoRemoveOne(power).addToBot()


fun AbstractPower.addToTop_AutoRemoveOne(power: AbstractPower?) =
    this.action_AutoRemoveOne(power).addToTop()


fun AbstractPower.tryCopyPower(newOwner: AbstractCreature): AbstractPower?
{
    val pClass: Class<out AbstractPower> = this.javaClass
    val constructors = pClass.declaredConstructors
    var instance: AbstractPower? = null
    try
    {
        if (PowerUtility.specialCopyPower.contains(pClass))
        {
            if (pClass == TheBombPower::class.java)
            {
                instance = TheBombPower(newOwner, 0, 40)
            }
        }
        else if (this is CopyAblePower)
        {
            instance = this.makeCopy(newOwner)
        }
        else
        {
            val paramCount = constructors[0].parameterCount
            val paramTypes = constructors[0].parameterTypes
            val paramNewInstance = arrayOfNulls<Any>(paramCount)
            for (i in 0 until paramCount)
            {
                val param = paramTypes[i]
                if (AbstractCreature::class.java.isAssignableFrom(param))
                {
                    paramNewInstance[i] = newOwner
                }
                else if (Integer.TYPE.isAssignableFrom(param))
                {
                    paramNewInstance[i] = 0
                }
                else if (String::class.java.isAssignableFrom(param))
                {
                    paramNewInstance[i] = ""
                }
                else if (java.lang.Boolean.TYPE.isAssignableFrom(param))
                {
                    paramNewInstance[i] = true
                }
            }
            instance = constructors[0].newInstance(*paramNewInstance) as AbstractPower
            instance.amount = this.amount
        }
    }
    catch (e: Exception)
    {
        Logger.warning("Failed to copy power button for: " + pClass.name)
    }
    return instance
}

fun AbstractPower.addToTop_reducePowerToOwner(powerID: String, amount: Int)
{
    ActionUtility.addToTop_reducePower(powerID, amount, this.owner, this.owner)
}
fun AbstractPower.addToTop_reducePowerToOwner(power: AbstractPower, amount: Int)
{
    ActionUtility.addToTop_reducePower(power.ID, amount, this.owner, this.owner)
}

fun AbstractPower.addToBot_reducePowerToOwner(powerID: String, amount: Int)
{
    ActionUtility.addToBot_reducePower(powerID, amount, this.owner, this.owner)
}

fun AbstractPower.addToBot_reducePowerToOwner(power: AbstractPower, amount: Int)
{
    ActionUtility.addToBot_reducePower(power.ID, amount, this.owner, this.owner)
}

fun AbstractPower.addToBot_applyPower(power: AbstractPower)
{
    ActionUtility.addToBot_applyPower(power, this.owner)
}

fun AbstractPower.addToBot_applyPowerSelf()
{
    ActionUtility.addToBot_applyPower(this, this.owner)
}