package superstitio.cards.lupa

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.cards.NormalCard
import superstitio.cards.general.FuckJob_Card
import superstitio.powers.lupaOnly.FloorSemen
import superstitio.powers.lupaOnly.InsideSemen
import superstitio.powers.lupaOnly.OutsideSemen
import superstitio.powers.lupaOnly.SemenPower
import superstitioapi.actions.AutoDoneInstantAction

/**
 * @see NormalCard
 */
abstract class LupaCard(
    id: String,
    cardType: CardType,
    cost: Int,
    cardRarity: CardRarity,
    cardTarget: CardTarget,
    imgSubFolder: String = CardTypeToString(cardType),
) :
    NormalCard(id, cardType, cost, cardRarity, cardTarget, imgSubFolder)
{

    protected fun addToBot_useSemenAndAutoRemove(valueNeed: Int)
    {
        if (!hasEnoughSemen(valueNeed))
        {
            for (power in AbstractDungeon.player.powers)
            {
                if (power is InsideSemen || power is OutsideSemen || power is FloorSemen)
                {
                    addToBot_removeSpecificPower(power)
                }
            }
            return
        }
        if (valueNeed <= 0) return
        var valueNeedRemain = valueNeed
        val semenPowers = sortedSemenList()
        val smartCheapUse = sortedSemenList(valueNeed)
        if (semenPowers.isEmpty())
            return
        val semenPower = if (smartCheapUse.isEmpty())
            semenPowers[0]
        else
            smartCheapUse[0]

        //        if (semenPower.getTotalValue() >= valueNeedRemain) {
//            semenPower.addToBot_UseValue(valueNeedRemain);
////            valueNeedRemain = 0;
//            return;
//        }
        semenPower.addToBot_UseValue(semenPower.getSemenValue())
        valueNeedRemain -= semenPower.getSemenValue()
        val finalValueNeedRemain = valueNeedRemain
        AutoDoneInstantAction.addToBotAbstract { addToBot_useSemenAndAutoRemove(finalValueNeedRemain) }
    }

    companion object
    {
        @JvmStatic
        protected val totalSemenValue: Int
            get()
            {
                val insideSemenValue: Int = (AbstractDungeon.player.powers
                    .filterIsInstance<InsideSemen>().map(AbstractPower::amount)
                    .firstOrNull() ?: 0) * FuckJob_Card.InsideSemenRate
                val outsideSemenValue: Int = (AbstractDungeon.player.powers
                    .filterIsInstance<OutsideSemen>().map(AbstractPower::amount)
                    .firstOrNull() ?: 0) * FuckJob_Card.OutsideSemenRate
                val floorSemenValue: Int = (AbstractDungeon.player.powers
                    .filterIsInstance<FloorSemen>().map(AbstractPower::amount)
                    .firstOrNull() ?: 0) * FuckJob_Card.FloorSemenRate
                return insideSemenValue + outsideSemenValue + floorSemenValue
            }

        @JvmStatic
        protected fun sortedSemenList(): List<SemenPower>
        {
            val collect = AbstractDungeon.player.powers
                .filterIsInstance<SemenPower>()
                .sortedWith(SemenPower::compareTo)
                .toMutableList()
            collect.reverse()
            return collect
        }

        @JvmStatic
        protected fun sortedSemenList(maxValue: Int): List<SemenPower>
        {
            val collect = AbstractDungeon.player.powers
                .filterIsInstance<SemenPower>()
                .filter { semenPower: SemenPower -> semenPower.getSemenValue() <= maxValue }
                .sortedWith(SemenPower::compareTo)
                .toMutableList()
            collect.reverse()
            return collect
        }

        @JvmStatic
        protected fun hasEnoughSemen(amount: Int): Boolean
        {
            return amount <= totalSemenValue
        }
    }

}
