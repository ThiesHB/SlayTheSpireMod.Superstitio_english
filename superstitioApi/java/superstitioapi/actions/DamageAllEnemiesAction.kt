//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package superstitioapi.actions

import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier
import kotlin.math.min

/**
 * 参数调用顺序：
 *
 * 在 [.StartAction] 中：
 * [.atStart_additionalEffects] —— [.newAttackEffectMaker]
 *
 * 在 [.EndAction] ()} 中：
 * [.atEnd_additionalEffects] —— [.newDamageInfoMaker] —— [.afterDamageConsumer]
 */
class DamageAllEnemiesAction protected constructor(
    source: AbstractCreature, targetsDamagesMap: Map<AbstractCreature, Int>, private val multiDamages: IntArray,
    attackEffect: AttackEffect?, skipWait: Boolean, duration: Float,
    newDamageInfoMaker: Function<Int, DamageInfo?>,
    newAttackEffectMaker: Function<AbstractCreature, AbstractGameEffect?>,
    atStart_additionalEffects: List<Supplier<AbstractGameEffect?>>?,
    atEnd_additionalEffects: List<Function<AbstractCreature, AbstractGameEffect?>>?,
    afterDamageConsumer: Consumer<DamageEnemiesAction?>?
) : DamageEnemiesAction(
    source, targetsDamagesMap, attackEffect, skipWait, duration, newDamageInfoMaker,
    newAttackEffectMaker, atStart_additionalEffects, atEnd_additionalEffects, afterDamageConsumer
)
{
    /**
     * [Builder.newDamageInfoMaker]: 默认值为新建一个标准DamageInfo
     * [Builder.newAttackEffectMaker]: 默认值为新建一个标准FlashAtkImgEffect
     */
    class Builder(private val multiDamages: IntArray) : DamageEnemiesAction.Builder(
        GetDamageMapFromMultiDamagesOfCard(
            multiDamages
        )
    )
    {
        override fun create(): AbstractContinuallyAction
        {
            return DamageAllEnemiesAction(
                source, targetsDamagesMap, multiDamages, attackEffectType, skipWait, duration, newDamageInfoMaker,
                newAttackEffectMaker, atStart_additionalEffects, atEnd_additionalEffects, afterDamageConsumer
            )
        }

        companion object
        {
            fun GetDamageMapFromMultiDamagesOfCard(multiDamages: IntArray): MutableMap<AbstractCreature, Int>
            {
                val monsters = AbstractDungeon.getCurrRoom().monsters.monsters
                val targetsDamagesMap: MutableMap<AbstractCreature, Int> = HashMap()
                for (i in 0 until min(monsters.size.toDouble(), multiDamages.size.toDouble()).toInt())
                {
                    targetsDamagesMap[monsters[i]] = multiDamages[i]
                }
                return targetsDamagesMap
            }
        }
    }

    override fun EndAction()
    {
        for (p in AbstractDungeon.player.powers)
        {
            p.onDamageAllEnemies(this.multiDamages)
        }

        super.EndAction()
    }

    companion object
    {
        /**
         * 详情请查看[Builder]
         */
        fun builder(damages: IntArray): DamageEnemiesAction.Builder
        {
            return Builder(damages)
        }

        /**
         * 详情请查看[Builder]
         */
        fun builder(source: AbstractCreature, damages: IntArray): DamageEnemiesAction.Builder
        {
            return builder(damages).setSource(source)
        }
    }
}
