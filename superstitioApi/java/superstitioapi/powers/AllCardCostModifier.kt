package superstitioapi.powers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitioapi.Logger
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.powers.interfaces.HasAllCardCostModifyEffect
import superstitioapi.powers.interfaces.OnPostApplyThisPower
import superstitioapi.renderManager.inBattleManager.InBattleDataManager
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CardUtility
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import kotlin.math.max
import kotlin.math.min

abstract class AllCardCostModifier(
    id: String,
    owner: AbstractCreature,
    var decreasedCost: Int,
    useAmount: Int,
    private val holder: HasAllCardCostModifyEffect
) : SuperstitioApiPower(id, owner, useAmount, if (owner.isPlayer) PowerType.BUFF else PowerType.DEBUFF),
    NonStackablePower, OnPostApplyThisPower<AllCardCostModifier>
{
    var order: Int = 0
    var isActive: Boolean = false
        private set
    private var checkTimer = CHECK_TIME

    fun ifIsTheMinOrder(): Boolean
    {
        return all.none { power: AllCardCostModifier -> power.amount != 0 && power.order > this.order }
    }

    /**
     * 可以随意使用
     */
    fun tryUseEffect()
    {
        if (!this.isActive) return
        if (this.amount == 0) return
        CheaperAllCards()
    }

    fun removeSelf()
    {
        Logger.debug("remove CostModifier")
        if (this.isActive && this.amount != 0)
        {
            CostToOriginAllCards()
        }
        this.addToBot(RemoveSpecificPowerAction(this.owner, this.owner, this))
        this.amount = 0
        this.isActive = false
        addToBot_TryActivateLowestOrder()
    }

    fun getOriginCost(card: AbstractCard?): Int
    {
        if (card == null) return 0
        if (InBattleDataManager.costMap[card.uuid] == null) return card.cost
        return InBattleDataManager.costMap[card.uuid]!!
    }

    protected fun CostToOriginAllCards()
    {
        CardUtility.AllCardInBattle().forEach(Consumer(this::CostToOriginOneCard))
        InBattleDataManager.costMap.clear()
    }

    private fun activateEffect()
    {
        Logger.debug("add CostModifier")
        this.setActive()
        tryUseEffect()
        updateDescription()
    }

    /**
     * 无可用性检测
     */
    private fun CheaperAllCards()
    {
        CardUtility.AllCardInBattle().forEach(Consumer(this::CheaperOneCard))
    }

    private fun CheaperOneCard(card: AbstractCard?)
    {
        if (card == null) return
        if (card.costForTurn <= 0) return
        if (InBattleDataManager.costMap.keys.stream()
                .noneMatch { uuidInMap: UUID? -> card.uuid === uuidInMap }
        ) InBattleDataManager.costMap[card.uuid] = card.costForTurn
        val newCost = getOriginCost(card) - this.amount
        if (card.costForTurn == newCost) return
        card.setCostForTurn(max(newCost, 0))
        card.isCostModifiedForTurn = true
        CardUtility.flashIfInHand(card)
    }

    private fun CostToOriginOneCard(card: AbstractCard?)
    {
        if (card == null) return
        if (getOriginCost(card) < card.costForTurn) return
        if (!InBattleDataManager.costMap.containsKey(card.uuid)) return
        CardUtility.flashIfInHand(card)

        card.setCostForTurn(getOriginCost(card))
        card.isCostModifiedForTurn = false
    }

    private fun setActive()
    {
        this.isActive = true
    }

    override fun renderAmount(sb: SpriteBatch, x: Float, y: Float, c: Color)
    {
        super.renderAmount(sb, x, y, c)
        renderAmount2(sb, x, y, c, decreasedCost)
    }

    override fun onApplyPower(power: AbstractPower, target: AbstractCreature, source: AbstractCreature)
    {
        super.onApplyPower(power, target, source)
    }

    override fun update(slot: Int)
    {
        super.update(slot)
        checkTimer -= Gdx.graphics.deltaTime
        if (checkTimer <= 0.0f)
        {
            tryUseEffect()
            checkTimer = CHECK_TIME
        }
    }

    override fun onCardDraw(card: AbstractCard)
    {
        if (!this.isActive) return

        this.CheaperOneCard(card)
    }

    override fun InitializePostApplyThisPower(addedPower: AllCardCostModifier)
    {
        this.order = all.minOfOrNull { it.order } ?: 0
        addToBot_TryActivateLowestOrder()
    }

    companion object
    {
        private const val CHECK_TIME = 1.0f
        fun RemoveAllByHolder(aimHolder: HasAllCardCostModifyEffect)
        {
            getAllByHolder(aimHolder).forEach { obj: AllCardCostModifier? -> obj!!.removeSelf() }
        }

        fun getAllByHolder(aimHolder: HasAllCardCostModifyEffect): Collection<AllCardCostModifier>
        {
            return all.filter { it.holder.IDAsHolder() == aimHolder.IDAsHolder() }
        }

        val all: Collection<AllCardCostModifier>
            get() = AbstractDungeon.player.powers
                .filter { power: AbstractPower -> power is AllCardCostModifier && power.amount > 0 }
                .map { power: AbstractPower? -> power as AllCardCostModifier }

        val activateOne: AllCardCostModifier?
            get() = all.firstOrNull { obj: AllCardCostModifier? -> obj!!.isActive }

        fun addToBot_TryActivateLowestOrder()
        {
            AutoDoneInstantAction.addToBotAbstract {
                all.firstOrNull { obj: AllCardCostModifier? -> obj?.ifIsTheMinOrder() == true }?.activateEffect()
            }
        }

        @Throws(InstantiationException::class, IllegalAccessException::class, InvocationTargetException::class)
        fun <T : AllCardCostModifier>
                addToTop_AddNew(
            holder: HasAllCardCostModifyEffect?,
            decreasedCost: Int,
            canUseAmount: Int,
            powerType: Constructor<T>
        )
        {
            ActionUtility.addToTop_applyPower(
                powerType.newInstance(
                    AbstractDungeon.player,
                    decreasedCost,
                    canUseAmount,
                    holder
                )
            )
        }

        @Throws(InvocationTargetException::class, InstantiationException::class, IllegalAccessException::class)
        fun <T : AllCardCostModifier>
                addTo_Bot_EditAmount_Top_FirstByHolder(
            holder: HasAllCardCostModifyEffect,
            decreasedCost: Int,
            canUseAmount: Int,
            powerType: Constructor<T>
        )
        {
            addTo_Bot_EditAmount_Top_FirstByHolder(
                holder,
                decreasedCost,
                { canUseAmount },
                powerType
            )
        }

        @Throws(InvocationTargetException::class, InstantiationException::class, IllegalAccessException::class)
        fun <T : AllCardCostModifier> addTo_Bot_EditAmount_Top_FirstByHolder(
            holder: HasAllCardCostModifyEffect,
            decreasedCost: Int,
            canUseAmountAdder: Function<AllCardCostModifier?, Int>,
            powerType: Constructor<T>
        )
        {
            val allCardCostModifier = getAllByHolder(holder).firstOrNull()
            if (allCardCostModifier == null)
            {
                addToTop_AddNew(holder, decreasedCost, canUseAmountAdder.apply(null), powerType)
                return
            }
            val finalAllCardCostModifierPower: AllCardCostModifier = allCardCostModifier
            AutoDoneInstantAction.addToBotAbstract {
                finalAllCardCostModifierPower.amount += canUseAmountAdder.apply(finalAllCardCostModifierPower)
                finalAllCardCostModifierPower.decreasedCost =
                    min(decreasedCost, finalAllCardCostModifierPower.amount)
                finalAllCardCostModifierPower.updateDescription()
            }
        }

        fun <T : AllCardCostModifier?> CombineAllByHolder(aimHolder: HasAllCardCostModifyEffect, tClass: Class<T>)
        {
            val cardCostModifier =
                getAllByHolder(aimHolder).filter { power: AllCardCostModifier -> power.amount >= 0 }
                    .filter { power: AllCardCostModifier -> power.javaClass == tClass }
            val minOrderByHolder =
                cardCostModifier.minOfOrNull(AllCardCostModifier::order) ?: 0
            cardCostModifier.firstOrNull { power: AllCardCostModifier -> power.order == minOrderByHolder }
                ?.let { power: AllCardCostModifier ->
                    val decreasedCost =
                        cardCostModifier.maxOfOrNull(AllCardCostModifier::decreasedCost) ?: 0
                    val totalEnergy =
                        cardCostModifier.sumOf(AllCardCostModifier::amount)
                    power.decreasedCost = decreasedCost
                    power.amount = totalEnergy
                    cardCostModifier.filter { power2: AllCardCostModifier -> power2.order != power.order }
                        .forEach(AllCardCostModifier::removeSelf)
                }
        }
    }
}
