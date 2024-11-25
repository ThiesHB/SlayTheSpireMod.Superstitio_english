package superstitio.delayHpLose

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect
import superstitio.DataManager.CanOnlyDamageDamageType
import superstitio.Logger
import superstitio.SuperstitioImg.NoNeedImg
import superstitio.powers.AbstractSuperstitioPower
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.cards.DamageActionMaker
import superstitioapi.powers.interfaces.DecreaseHealthBarNumberPower
import superstitioapi.powers.interfaces.OnPostApplyThisPower
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleApplyPowerEffect
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleRemovePowerEffect
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleTips
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.setDescriptionArgs
import java.lang.reflect.Modifier
import java.util.stream.Stream

//import static superstitio.modTips.TipsManager.DELAY_HP_LOSE_TIP;
@NoNeedImg
abstract class DelayHpLosePower(id: String, owner: AbstractCreature, amount: Int) :
    AbstractSuperstitioPower(id, owner, amount, PowerType.BUFF, false),
    HealthBarRenderPower, DecreaseHealthBarNumberPower,
    InvisiblePower_InvisibleIconAndAmount, InvisiblePower_InvisibleTips,
    InvisiblePower_InvisibleApplyPowerEffect, InvisiblePower_InvisibleRemovePowerEffect,
    OnPostApplyThisPower<DelayHpLosePower> {
    protected var isRemovedForApplyDamage: Boolean = false

    init {
        this.updateDescription()
    }

    /**
     * @param amount      需要移除的值
     * @param removeOther 是否移除其他的
     * @return 剩余的amount
     */
    protected abstract fun addToBot_removeDelayHpLoss(amount: Int, removeOther: Boolean): Int

    protected fun playRemoveEffect() {
        AbstractDungeon.effectList.add(
            PowerBuffEffect(
                owner.hb.cX - owner.animX,
                owner.hb.cY + owner.hb.height / 2.0f,
                pureName() + CardCrawlGame.languagePack.getUIString("ApplyPowerAction").TEXT[0]
            )
        )
    }

    protected fun pureName(): String {
        return name.replace("#r", "")
    }

    protected fun addToBot_applyDamage() {
        this.isRemovedForApplyDamage = true
        val self: AbstractPower = this
        AutoDoneInstantAction.addToBotAbstract {
            immediate_applyDamage(self)
        }
        addToBot_removeSpecificPower(this)
    }

    protected fun immediate_applyDamage(self: AbstractPower) {
        if (self.amount <= 0) return
        PowerUtility.BubbleMessage(self, true, pureName())
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05f)
        AbstractDungeon.player.damage(UnBlockAbleIgnoresTempHPDamage.damageInfo(this, self.amount))
        self.amount = 0
    }

    private val delayHpLoseDamageActionMaker: DamageActionMaker
        get() = DamageActionMaker.maker(this.amount, this.owner)
            .setDamageModifier(this, UnBlockAbleIgnoresTempHPDamage())
            .setEffect(AttackEffect.LIGHTNING)
            .setDamageType(CanOnlyDamageDamageType.UnBlockAbleDamageType)

    override fun InitializePostApplyThisPower(addedPower: DelayHpLosePower) {
//        TipsManager.tryShowTip(DELAY_HP_LOSE_TIP);
    }

    override fun renderAmount(sb: SpriteBatch, x: Float, y: Float, c: Color) {
    }

    override fun reducePower(reduceAmount: Int) {
        super.reducePower(reduceAmount)
        if (!isRemovedForApplyDamage) playRemoveEffect()
    }

    override fun onRemove() {
        super.onRemove()
        if (!isRemovedForApplyDamage) playRemoveEffect()
    }

    override fun updateDescriptionArgs() {
        setDescriptionArgs(this.amount)
    }

    override fun getHealthBarAmount(): Int {
        return this.amount
    }

    override fun getColor(): Color {
        return OriginColor
    }

    override fun getDecreaseAmount(): Int {
        return this.amount
    }

    override fun setDecreaseAmount(value: Int) {
        this.amount = value
    }

    companion object {
        private val OriginColor = Color(1.0f, 0.85f, 0.90f, 1.0f)
        fun <T : DelayHpLosePower?> findAll(target: AbstractCreature, tClass: Class<T>): Stream<T> {
            return target.powers.stream()
                .filter(tClass::isInstance)
                .map(tClass::cast)
        }

        /**
         * 只会对每个子类的移除方法调用一次
         *
         * @param powerClass  优先移除的类
         * @param amount      移除数量
         * @param removeOther 是否移除其他的
         */
        fun <T : DelayHpLosePower?> addToBot_removePower(
            powerClass: Class<T>, amount: Int, owner: AbstractCreature,
            removeOther: Boolean
        ) {
            if (amount <= 0) return
            var lastAmount = 0
            if (Modifier.isAbstract(powerClass.modifiers)) {
                Logger.warning("class " + powerClass.simpleName + " is abstract, pls check usages of DelayHpLosePower.addToBot_removePower()")
            }
            val hasRemovedClass = ArrayList<Class<out DelayHpLosePower>>()
            for (power in owner.powers) {
                if (!(powerClass.isInstance(power))) continue
                val delayHpLosePower = (power as DelayHpLosePower)
                lastAmount = delayHpLosePower.addToBot_removeDelayHpLoss(amount, removeOther)
                hasRemovedClass.add(delayHpLosePower.javaClass)
                break
            }
            if (lastAmount <= 0) return
            if (!removeOther) return

            for (power in owner.powers) {
                if (power !is DelayHpLosePower) continue
                if (hasRemovedClass.stream()
                        .anyMatch { tClass: Class<out DelayHpLosePower> -> tClass.isInstance(power) }
                ) continue
                val delayHpLosePower = power
                lastAmount = delayHpLosePower.addToBot_removeDelayHpLoss(lastAmount, removeOther)
                hasRemovedClass.add(delayHpLosePower.javaClass)
                if (lastAmount <= 0) break
            }
        }

        fun addToBot_removePower(amount: Int, owner: AbstractCreature, removeOther: Boolean) {
            if (amount <= 0) return
            var lastAmount = amount
            val hasRemovedClass = ArrayList<Class<out DelayHpLosePower>>()
            for (power in owner.powers) {
                if (power !is DelayHpLosePower) continue
                if (hasRemovedClass.stream()
                        .anyMatch { tClass: Class<out DelayHpLosePower> -> tClass.isInstance(power) }
                ) continue
                val delayHpLosePower = power
                lastAmount = delayHpLosePower.addToBot_removeDelayHpLoss(lastAmount, removeOther)
                hasRemovedClass.add(delayHpLosePower.javaClass)
                if (!removeOther) break
                if (lastAmount <= 0) break
            }
        }
    }
}
