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

class AllCardCostModifier_PerCard(
    owner: AbstractCreature,
    decreasedCost: Int,
    useTimes: Int,
    holder: HasAllCardCostModifyEffect
) : AllCardCostModifier(
    POWER_ID, owner, decreasedCost, useTimes, holder
) {
    init {
        amount = useTimes
    }

    fun isCostDecreased(card: AbstractCard): Boolean {
        return InBattleDataManager.costMap.containsKey(card.uuid) && getOriginCost(card) < card.costForTurn && !card.freeToPlay()
    }

    override fun updateDescriptionArgs() {
        setDescriptionArgs(decreasedCost, amount, (if (!isActive) powerStrings.DESCRIPTIONS[1] else ""))
    }

    override fun getDescriptionStrings(): String {
        return powerStrings.DESCRIPTIONS[0]
    }

    override fun onPlayCard(card: AbstractCard, m: AbstractMonster?) {
        if (!isActive) return
        if (!isCostDecreased(card)) return
        AutoDoneInstantAction.addToBotAbstract(VoidSupplier {
            this.flash()
            amount--
            this.amount = Integer.max(0, amount)
            if (amount == 0) removeSelf()
        })
    }

    companion object {
        val POWER_ID: String = DataUtility.MakeTextID(AllCardCostModifier_PerCard::class.java)
        var powerStrings: PowerStrings = getPowerStrings(POWER_ID)
    }
}
