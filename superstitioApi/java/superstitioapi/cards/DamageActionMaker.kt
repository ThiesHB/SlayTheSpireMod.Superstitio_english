package superstitioapi.cards

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitioapi.SuperstitioApiSetup
import superstitioapi.SuperstitioApiSetup.DamageEffect
import superstitioapi.actions.*
import superstitioapi.shader.ShaderUtility
import superstitioapi.shader.heart.HeartMultiAtOneShader.HeartMultiAtOneEffect
import java.util.*
import java.util.stream.Collectors

/**
 * 这个类其实已经是过度包装了，它本来是原始[DamageAction]的builder，但我已经重写了一套新的代码（并且附带了builder）
 *
 * 现在它唯一的用处就是实现[SuperstitioApiSetup.DamageEffect.HeartMultiInOne] 一类的特效，算是个可有可无的玩意
 */
class DamageActionMaker {
    private val builder: DamageEnemiesAction.Builder
    private var effect: AttackEffect? = DamageEffect.HeartMultiInOne

    /**
     * 产生一个AOE伤害
     */
    protected constructor(multiDamage: IntArray) {
        this.builder = DamageAllEnemiesAction.builder(multiDamage)
    }

    protected constructor(creatureDamageMap: MutableMap<AbstractCreature, Int>) {
        this.builder = DamageEnemiesAction.builder(creatureDamageMap)
    }

    protected constructor(multiDamage: IntArray, vararg targets: AbstractMonster) {
        this.builder = DamageEnemiesAction.builder(
            multiDamage,
            Arrays.stream(targets).collect(Collectors.toList())
        )
    }

    protected constructor(damageAmount: Int, target: AbstractCreature) {
        this.builder = DamageEnemiesAction.builder(damageAmount, target)
    }

    fun createAction(): AbstractContinuallyAction {
        if (effect == DamageEffect.HeartMultiInOne) {
            trySetHeartEffect(this.builder)
        }
        return builder.create()
    }

    fun addToBot() {
        createAction().addToBot()
    }

    fun addToTop() {
        createAction().addToTop()
    }

    fun setSource(source: AbstractCreature): DamageActionMaker {
        builder.setSource(source)
        return this
    }

    fun setDamageType(damageType: DamageType): DamageActionMaker {
        builder.setDamageType(damageType)
        return this
    }

    fun setEffect(effect: AttackEffect): DamageActionMaker {
        this.effect = effect
        builder.setAttackEffectType(effect)
        return this
    }

    fun setExampleCard(card: AbstractCard?): DamageActionMaker {
        return this.setDamageModifier(
            card, *DamageModifierManager.modifiers(card).toTypedArray()
        )
    }

    fun setSkipWait(skipWait: Boolean): DamageActionMaker {
        builder.setSkipWait(skipWait)
        return this
    }

    fun setDamageModifier(instigator: Any?, vararg damageModifier: AbstractDamageModifier?): DamageActionMaker {
        if (damageModifier.size > 0) builder.setDamageModifier(instigator, *damageModifier)
        return this
    }

    companion object {
        fun maker(damageAmount: Int, target: AbstractCreature): DamageActionMaker {
            return DamageActionMaker(damageAmount, target)
        }

        fun makeDamages(exampleCard: AbstractCard, vararg targets: AbstractMonster): DamageActionMaker {
            return DamageActionMaker(exampleCard.multiDamage, *targets)
        }

        fun makeDamages(creatureDamageMap: MutableMap<AbstractCreature, Int>): DamageActionMaker {
            return DamageActionMaker(creatureDamageMap)
        }

        fun makeAoeDamage(exampleCard: AbstractCard): DamageActionMaker {
            return DamageActionMaker(exampleCard.multiDamage)
                .setDamageModifier(
                    exampleCard, *DamageModifierManager.modifiers(exampleCard).toTypedArray()
                )
        }

        fun trySetHeartEffect(builder: DamageEnemiesAction.Builder) {
            if (ShaderUtility.canUseShader) builder.setNewAttackEffectMaker { creature: AbstractCreature ->
                HeartMultiAtOneEffect(
                    creature.hb
                )
            }
            else builder.setAttackEffectType(AttackEffect.BLUNT_LIGHT)
        }
    }
}
