package superstitioapi.powers

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitioapi.DataUtility
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.powers.interfaces.HasAllCardCostModifyEffect
import superstitioapi.renderManager.inBattleManager.InBattleDataManager
import superstitioapi.utils.ActionUtility.VoidSupplier
import superstitioapi.utils.setDescriptionArgs
import kotlin.math.min

class AllCardCostModifier_PerEnergy(
    owner: AbstractCreature,
    decreasedCost: Int,
    totalEnergy: Int,
    holder: HasAllCardCostModifyEffect
) : AllCardCostModifier(
    POWER_ID, owner, decreasedCost, totalEnergy, holder
) {
    init {
        amount = totalEnergy
    }

    fun totalCostDecreased(card: AbstractCard): Int {
        if (!InBattleDataManager.costMap.containsKey(card.uuid) || getOriginCost(card) <= card.costForTurn || card.freeToPlay()) {
            return 0
        }
        return getOriginCost(card) - card.costForTurn
    }

    override fun updateDescriptionArgs() {
        setDescriptionArgs(
            min(decreasedCost.toDouble(), amount.toDouble()),
            amount,
            (if (!isActive) powerStrings.DESCRIPTIONS[1] else "")
        )
    }

    override fun getDescriptionStrings(): String {
        return powerStrings.DESCRIPTIONS[0]
    }

    override fun onPlayCard(card: AbstractCard, m: AbstractMonster) {
        if (!isActive) return
        if (totalCostDecreased(card) == 0) return
        AutoDoneInstantAction.addToBotAbstract(VoidSupplier {
            this.flash()
            this.amount -= totalCostDecreased(card)
            this.amount = Integer.max(0, amount)
            if (amount < decreasedCost) {
                this.decreasedCost = amount
                this.CostToOriginAllCards()
                if (amount == 0) this.removeSelf()
                else this.tryUseEffect()
            }
        })
    }

    companion object {
        val POWER_ID: String = DataUtility.MakeTextID(AllCardCostModifier_PerEnergy::class.java)
        var powerStrings: PowerStrings = getPowerStrings(POWER_ID)
    }
}
