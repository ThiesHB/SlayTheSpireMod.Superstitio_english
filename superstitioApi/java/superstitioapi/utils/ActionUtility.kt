package superstitioapi.utils

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.*
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.powers.AllCardCostModifier
import superstitioapi.utils.ActionUtility.VoidSupplier

object ActionUtility
{
    fun addToBot_applyPower(power: AbstractPower?)
    {
        AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(power?.owner, AbstractDungeon.player, power))
    }

    fun addToBot_applyPower(power: AbstractPower?, source: AbstractCreature)
    {
        AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(power?.owner, source, power))
    }

    fun addToTop_applyPower(power: AbstractPower?)
    {
        AbstractDungeon.actionManager.addToTop(ApplyPowerAction(power?.owner, AbstractDungeon.player, power))
    }

    //    public static void addToBot_reducePower(final AbstractPower power, final AbstractCreature source) {
    //        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(power.owner, source, power, power.amount));
    //    }
    //不知道为什么这段代码不起作用，开摆
    //但是：this.addToBot(new ReducePowerAction(this.owner, this.owner, power, 1));这段代码就有用
    fun addToBot_reducePower(
        powerId: String, amount: Int, target: AbstractCreature, source: AbstractCreature
    )
    {
        AbstractDungeon.actionManager.addToBottom(ReducePowerAction(target, source, powerId, amount))
    }

    fun addToTop_reducePower(
        powerId: String, amount: Int, target: AbstractCreature, source: AbstractCreature
    )
    {
        AbstractDungeon.actionManager.addToTop(ReducePowerAction(target, source, powerId, amount))
    }

    fun addToBot_removeSpecificPower(power: AbstractPower, source: AbstractCreature)
    {
        AbstractDungeon.actionManager.addToBottom(RemoveSpecificPowerAction(power.owner, source, power))
    }

    fun addToBot_removeSpecificPower(
        powerId: String, target: AbstractCreature, source: AbstractCreature
    )
    {
        AbstractDungeon.actionManager.addToBottom(RemoveSpecificPowerAction(target, source, powerId))
    }

    @JvmOverloads
    fun addToBot_makeTempCardInBattle(
        card: AbstractCard, battleCardPlace: BattleCardPlace, amount: Int = 1,
        upgrade: Boolean = false
    )
    {
        if (upgrade && !card.upgraded) card.upgrade()
        val gameAction = getMakeTempCardAction(card, battleCardPlace, amount)
        addToBot(gameAction)

        AutoDoneInstantAction.addToBotAbstract {
            val power: AllCardCostModifier? = AllCardCostModifier.activateOne
            power?.let { obj: AllCardCostModifier? -> obj!!.tryUseEffect() }
        }
    }

    fun addToTop_makeTempCardInBattle(
        card: AbstractCard, battleCardPlace: BattleCardPlace, amount: Int,
        upgrade: Boolean
    )
    {
        if (upgrade) card.upgrade()
        val gameAction = getMakeTempCardAction(card, battleCardPlace, amount)
        AutoDoneInstantAction.addToTopAbstract {
            val power: AllCardCostModifier? = AllCardCostModifier.activateOne
            power?.let { obj: AllCardCostModifier? -> obj!!.tryUseEffect() }
        }
        addToTop(gameAction)
    }

    fun addToBot_makeTempCardInBattle(card: AbstractCard, battleCardPlace: BattleCardPlace, upgrade: Boolean)
    {
        addToBot_makeTempCardInBattle(card, battleCardPlace, 1, upgrade)
    }

    fun addEffect(effect: AbstractGameEffect)
    {
        AbstractDungeon.effectList.add(effect)
    }

    fun addToBot(action: AbstractGameAction)
    {
        AbstractDungeon.actionManager.addToBottom(action)
    }

    fun addToTop(action: AbstractGameAction)
    {
        AbstractDungeon.actionManager.addToTop(action)
    }

    val isNotInBattle: Boolean
        get()
        {
            if (AbstractDungeon.currMapNode == null) return true
            if (AbstractDungeon.getCurrRoom() == null) return true
            if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) return true
            if (AbstractDungeon.getCurrRoom().monsters == null) return true
            if (AbstractDungeon.getCurrRoom().monsters.monsters == null) return true
            return AbstractDungeon.getCurrRoom().monsters.monsters.stream()
                .allMatch { obj: AbstractMonster -> obj.isDeadOrEscaped }
        }

    private fun getMakeTempCardAction(
        card: AbstractCard,
        battleCardPlace: BattleCardPlace,
        amount: Int
    ): AbstractGameAction
    {
        val gameAction = when (battleCardPlace)
        {
            BattleCardPlace.Hand     -> MakeTempCardInHandAction(card, amount)
            BattleCardPlace.DrawPile -> MakeTempCardInDrawPileAction(card, amount, true, true)
            BattleCardPlace.Discard  -> MakeTempCardInDiscardAction(card, amount)
            else                     -> MakeTempCardInDiscardAction(card, amount)
        }
        return gameAction
    }

    sealed class BattleCardPlace
    {
        data object Hand : BattleCardPlace()
        data object DrawPile : BattleCardPlace()
        data object Discard : BattleCardPlace()
        companion object
        {
            fun values(): Array<BattleCardPlace>
            {
                return arrayOf(Hand, DrawPile, Discard)
            }

            fun valueOf(value: String): BattleCardPlace
            {
                return when (value)
                {
                    "Hand"     -> Hand
                    "DrawPile" -> DrawPile
                    "Discard"  -> Discard
                    else       -> throw IllegalArgumentException("No object superstitioapi.utils.ActionUtility.BattleCardPlace.$value")
                }
            }
        }
    }

    fun interface VoidSupplier
    {
        fun get()

        /**
         * 在之后添加
         * 旧的不改变，只是返回一个新的
         */
        fun additionActionToPost(action: VoidSupplier): VoidSupplier
        {
            val self = this
            return VoidSupplier {
                self.get()
                action.get()
            }
        }

        /**
         * 在之前添加
         * 旧的不改变，只是返回一个新的
         */
        fun additionActionToPrev(action: VoidSupplier): VoidSupplier
        {
            val self = this
            return VoidSupplier {
                action.get()
                self.get()
            }
        }

        fun addToBotAsAbstractAction()
        {
            AutoDoneInstantAction.addToBotAbstract(this)
        }

        fun addToBotAsAbstractAction(time: Int)
        {
            AutoDoneInstantAction.addToBotAbstract(this, time)
        }

        companion object
        {
            val Empty: VoidSupplier = VoidSupplier {}
        }
    }

    fun interface TriFunction<T1, T2, T3, R>
    {
        fun apply(t1: T1, t2: T2, t3: T3): R
    }

    fun interface FunctionReturnSelfType
    {
        fun get(): FunctionReturnSelfType?
    }
}

fun AbstractGameAction.addToBot()
{
    ActionUtility.addToBot(this)
}

fun AbstractGameAction.addToTop()
{
    ActionUtility.addToTop(this)
}
