package superstitioapi.actions

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer
import com.megacrit.cardcrawl.actions.utility.WaitAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect
import superstitioapi.utils.ActionUtility.TriFunction
import java.util.*
import java.util.function.*
import java.util.function.Function
import kotlin.math.min

open class DamageEnemiesAction protected constructor(
    source: AbstractCreature, targetsDamagesMap: Map<AbstractCreature, Int>, effect: AttackEffect?,
    skipWait: Boolean, duration: Float, newDamageInfoMaker: Function<Int, DamageInfo?>,
    newAttackEffectMaker: Function<AbstractCreature, AbstractGameEffect?>,
    atStart_additionalEffects: List<Supplier<AbstractGameEffect?>>?,
    atEnd_additionalEffects: List<Function<AbstractCreature, AbstractGameEffect?>>?,
    afterDamageConsumer: Consumer<DamageEnemiesAction?>?
) : AbstractContinuallyAction(ActionType.DAMAGE, duration)
{
    protected val newDamageInfoMaker: Function<Int, DamageInfo?>
    protected val skipWait: Boolean
    protected val afterDamageConsumer: Consumer<DamageEnemiesAction?>?
    protected val atStart_additionalEffects: MutableList<Supplier<AbstractGameEffect?>> = ArrayList()
    protected val atEnd_additionalEffects: MutableList<Function<AbstractCreature, AbstractGameEffect?>> = ArrayList()
    protected val newAttackEffectMaker: Function<AbstractCreature, AbstractGameEffect?>
    protected val targetsDamagesMap: Map<AbstractCreature, Int>

    open class Builder
    {
        protected val atStart_additionalEffects: MutableList<Supplier<AbstractGameEffect?>> = ArrayList()
        protected val atEnd_additionalEffects: MutableList<Function<AbstractCreature, AbstractGameEffect?>> =
            ArrayList()
        protected val targetsDamagesMap: MutableMap<AbstractCreature, Int>
        protected var source: AbstractCreature
        protected var damageType: DamageType
        protected var skipWait: Boolean = false
        protected var duration: Float = DURATION
        protected var newDamageInfoMaker: Function<Int, DamageInfo?>
        protected var newAttackEffectMaker: Function<AbstractCreature, AbstractGameEffect?>
        protected var afterDamageConsumer: Consumer<DamageEnemiesAction?>? = null
        protected var attackEffectType: AttackEffect = AttackEffect.NONE

        /**
         * @param calculatedDamage 计算后的伤害值
         * @param target           单一目标
         */
        constructor(calculatedDamage: Int, target: AbstractCreature)
        {
            this.source = AbstractDungeon.player
            this.damageType = DamageType.NORMAL
            this.newDamageInfoMaker = Function { damage: Int -> DamageInfo(this.source, damage, this.damageType) }
            this.newAttackEffectMaker = Function { creature: AbstractCreature ->
                FlashAtkImgEffect(
                    creature.hb.cX,
                    creature.hb.cY,
                    attackEffectType
                )
            }
            this.targetsDamagesMap = HashMap()
            targetsDamagesMap[target] = calculatedDamage
        }

        /**
         * 本初始化只能对 [AbstractMonster]起作用
         *
         * @param multiDamages   计算后的复数伤害值，考虑到兼容性，可以直接拿[AbstractCard.multiDamage]
         * @param targetMonsters 复数目标
         */
        constructor(multiDamages: IntArray, targetMonsters: List<AbstractMonster?>)
        {
            this.source = AbstractDungeon.player
            this.damageType = DamageType.NORMAL
            this.newDamageInfoMaker = Function { damage: Int -> DamageInfo(this.source, damage, this.damageType) }
            this.newAttackEffectMaker = Function { creature: AbstractCreature ->
                FlashAtkImgEffect(
                    creature.hb.cX,
                    creature.hb.cY,
                    attackEffectType
                )
            }
            this.targetsDamagesMap = GetDamageMapFromMultiDamagesOfCard(targetMonsters, multiDamages)
        }

        /**
         * @param targetsDamagesMap 计算后的伤害值
         */
        constructor(targetsDamagesMap: MutableMap<AbstractCreature, Int>)
        {
            this.source = AbstractDungeon.player
            this.damageType = DamageType.NORMAL
            this.newDamageInfoMaker = Function { damage: Int -> DamageInfo(this.source, damage, this.damageType) }
            this.newAttackEffectMaker = Function { creature: AbstractCreature ->
                FlashAtkImgEffect(
                    creature.hb.cX,
                    creature.hb.cY,
                    attackEffectType
                )
            }
            this.targetsDamagesMap = targetsDamagesMap
        }

        open fun create(): AbstractContinuallyAction
        {
            return DamageEnemiesAction(
                source, targetsDamagesMap, attackEffectType, skipWait, duration, newDamageInfoMaker,
                newAttackEffectMaker, atStart_additionalEffects, atEnd_additionalEffects, afterDamageConsumer
            )
        }

        fun setSource(source: AbstractCreature): Builder
        {
            this.source = source
            return this
        }

        fun setNewAttackEffectMakerAsMuteSfxFlashAtkImgEffect(): Builder
        {
            this.newAttackEffectMaker = Function { creature: AbstractCreature ->
                FlashAtkImgEffect(
                    creature.hb.cX,
                    creature.hb.cY,
                    attackEffectType,
                    true
                )
            }
            return this
        }

        fun setDamageType(damageType: DamageType): Builder
        {
            this.damageType = damageType
            return this
        }

        fun setSkipWait(skipWait: Boolean): Builder
        {
            this.skipWait = skipWait
            return this
        }


        fun setAfterDamageConsumer(afterDamageConsumer: Consumer<DamageEnemiesAction?>?): Builder
        {
            this.afterDamageConsumer = afterDamageConsumer
            return this
        }

        fun setAttackEffectType(attackEffectType: AttackEffect): Builder
        {
            this.attackEffectType = attackEffectType
            return this
        }

        fun setNewAttackEffectMaker(newAttackEffectMaker: Function<AbstractCreature, AbstractGameEffect?>): Builder
        {
            this.newAttackEffectMaker = newAttackEffectMaker
            return this
        }

        @SafeVarargs
        fun setAtStart_additionalEffects(vararg atStart_additionalEffects: Supplier<AbstractGameEffect?>): Builder
        {
            this.atStart_additionalEffects.addAll(atStart_additionalEffects)
            return this
        }

        @SafeVarargs
        fun setAtEnd_additionalEffects(vararg atEnd_additionalEffects: Function<AbstractCreature, AbstractGameEffect?>): Builder
        {
            this.atEnd_additionalEffects.addAll(atEnd_additionalEffects)
            return this
        }

        fun setFast(): Builder
        {
            this.duration = Settings.ACTION_DUR_XFAST
            return this
        }

        fun setnewDamageInfoMaker(newDamageInfoMaker: Function<Int, DamageInfo?>): Builder
        {
            this.newDamageInfoMaker = newDamageInfoMaker
            return this
        }

        fun setDuration(duration: Float): Builder
        {
            this.duration = duration
            return this
        }

        fun setDamageModifier(instigator: Any?, vararg damageModifiers: AbstractDamageModifier?): Builder
        {
            this.newDamageInfoMaker = Function { damageAmount: Int ->
                BindingHelper.makeInfo(
                    DamageModContainer(instigator, *damageModifiers), source,
                    damageAmount, damageType
                )
            }
            return this
        }

        fun addToBot()
        {
            create().addToBot()
        }

        fun addToTop()
        {
            create().addToTop()
        }

        companion object
        {
            fun GetDamageMapFromMultiDamagesOfCard(
                targetMonsters: List<AbstractMonster?>,
                multiDamages: IntArray
            ): MutableMap<AbstractCreature, Int>
            {
                val monsters = AbstractDungeon.getCurrRoom().monsters.monsters
                val targetsDamagesMap: MutableMap<AbstractCreature, Int> = HashMap()
                for (i in 0 until min(monsters.size, multiDamages.size))
                {
                    if (targetMonsters.contains(monsters[i])) targetsDamagesMap[monsters[i]] = multiDamages[i]
                }
                return targetsDamagesMap
            }

            fun DefaultDamageCalculator(target: AbstractCreature, source: AbstractCreature, baseDamage: Int): Int
            {
                val info = DamageInfo(source, baseDamage)
                info.applyPowers(source, target)
                return info.output
            }

            fun DoNotCalculateDamageCalculator(target: AbstractCreature, source: AbstractCreature, damage: Int): Int
            {
                return damage
            }

            fun createDamageMatrix(
                baseDamage: Int, source: AbstractCreature, targets: List<AbstractCreature>,
                damageCalculator: TriFunction<AbstractCreature, AbstractCreature, Int, Int>
            ): Map<AbstractCreature, Int>
            {
                val targetsDamagesMap: MutableMap<AbstractCreature, Int> = HashMap()
                for (creature in targets)
                {
                    targetsDamagesMap[creature] = damageCalculator.apply(creature, source, baseDamage)
                }
                return targetsDamagesMap
            }
        }
    }

    init
    {
        this.targetsDamagesMap = Collections.unmodifiableMap(targetsDamagesMap)
        if (targetsDamagesMap.size == 1) this.target = targetsDamagesMap.keys.stream().findAny().get()
        this.source = source
        this.skipWait = skipWait
        this.newDamageInfoMaker = newDamageInfoMaker
        this.afterDamageConsumer = afterDamageConsumer
        this.newAttackEffectMaker = newAttackEffectMaker
        this.atStart_additionalEffects.addAll(atStart_additionalEffects!!)
        this.atEnd_additionalEffects.addAll(atEnd_additionalEffects!!)
        this.attackEffect = effect
    }

    override fun StartAction()
    {
        for (additionalEffect in atStart_additionalEffects) AbstractDungeon.effectList.add(additionalEffect.get())
        targetsDamagesMap.forEach { (creature: AbstractCreature, damage: Int) ->
            AbstractDungeon.effectList.add(newAttackEffectMaker.apply(creature))
        }
    }


    override fun RunAction()
    {
    }

    override fun EndAction()
    {
        targetsDamagesMap.forEach { (creature: AbstractCreature, damage: Int) ->
            for (action in atEnd_additionalEffects) AbstractDungeon.effectList.add(action.apply(creature))
            creature.damage(newDamageInfoMaker.apply(damage))
        }

        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) AbstractDungeon.actionManager.clearPostCombatActions()

        afterDamageConsumer?.accept(this)

        if (!this.skipWait && !Settings.FAST_MODE) this.addToTop(WaitAction(POST_ATTACK_WAIT_DUR))
    }


    override val isDoneCheck: Boolean
        get()
        {
            if (this.damageType == DamageType.THORNS) return false
            return this.source == null || source.isDying || source.halfDead
        }

    companion object
    {
        protected const val DURATION: Float = 0.1f
        protected const val POST_ATTACK_WAIT_DUR: Float = 0.1f

        /**
         * 详情请查看[Builder.Builder]
         */
        fun builder(damage: Int, target: AbstractCreature): Builder
        {
            return Builder(damage, target)
        }

        /**
         * 详情请查看[Builder.Builder]
         */
        fun builder(source: AbstractCreature, damage: Int, target: AbstractCreature): Builder
        {
            return builder(damage, target).setSource(source)
        }

        /**
         * 详情请查看[Builder.Builder]
         */
        fun builder(targetsDamagesMap: MutableMap<AbstractCreature, Int>): Builder
        {
            return Builder(targetsDamagesMap)
        }

        /**
         * 详情请查看[Builder.Builder]
         */
        fun builder(source: AbstractCreature, targetsDamagesMap: MutableMap<AbstractCreature, Int>): Builder
        {
            return builder(targetsDamagesMap).setSource(source)
        }

        /**
         * 详情请查看[Builder.Builder]
         */
        fun builder(multiDamages: IntArray, targetMonsters: List<AbstractMonster?>): Builder
        {
            return Builder(multiDamages, targetMonsters)
        }

        /**
         * 详情请查看[Builder.Builder]
         */
        fun builder(
            source: AbstractCreature,
            multiDamages: IntArray,
            targetMonsters: List<AbstractMonster?>
        ): Builder
        {
            return builder(multiDamages, targetMonsters).setSource(source)
        }
    }
}
