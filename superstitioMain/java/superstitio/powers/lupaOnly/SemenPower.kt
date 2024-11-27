package superstitio.powers.lupaOnly

import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitioapi.utils.ActionUtility
import java.util.function.Function

interface SemenPower
{
    fun getSemenValue(): Int

    val self: AbstractPower
        get() = this as AbstractPower

    /*
    * 3精液价值为3-4腐朽移除，4-5格挡
    */
    fun compareTo(other: SemenPower): Int
    {
        return Integer.compare(this.getSemenValue(), other.getSemenValue())
    }

    fun addToBot_UseValue(valueUse: Int)
    {
        ActionUtility.addToBot_reducePower(
            self.ID, MathUtils.ceil(valueUse.toFloat() / this.getSemenValue()), AbstractDungeon.player,
            AbstractDungeon.player
        )
    }

    fun transToOtherSemen(toOtherSemen: Function<Int, AbstractPower>)
    {
        val SemenTo = toOtherSemen.apply(self.amount)
        if (SemenTo !is SemenPower) return
        ActionUtility.addToBot_removeSpecificPower(self.ID, self.owner, self.owner)
        ActionUtility.addToBot_applyPower(SemenTo)
    }

    fun getTotalValue(): Int = getSemenValue() * self.amount
}
