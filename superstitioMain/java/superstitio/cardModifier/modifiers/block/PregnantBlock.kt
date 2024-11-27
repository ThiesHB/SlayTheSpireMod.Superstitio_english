package superstitio.cardModifier.modifiers.block

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.cards.DamageInfo
import superstitio.cardModifier.modifiers.AbstractLupaBlock

abstract class PregnantBlock(id: String) : AbstractLupaBlock(id)
{
    //    @Override
    //    public void onThisBlockDamaged(DamageInfo info, int lostAmount) {
    //
    //    }
    ///受到外力则为流产
    open fun removeUnNaturally(info: DamageInfo?, remainingDamage: Int): Int
    {
        return remainingDamage
    }

    ///正常顺产的效果
    open fun removeNaturally(remainingDamage: Int): Int
    {
        return remainingDamage
    }

    override fun shouldStack(): Boolean
    {
        return false
    }

    override fun amountLostAtStartOfTurn(): Int
    {
        return 0
    }

    override fun onRemove(lostByStartOfTurn: Boolean, info: DamageInfo, remainingDamage: Int): Int
    {
        var remain = remainingDamage
        remain = if (info != null) removeUnNaturally(info, remain)
        else removeNaturally(remain)
        return remain
    }

    override fun blockImageColor(): Color
    {
        return Color.GOLDENROD.cpy()
    }

    override fun priority(): Priority
    {
        return Priority.BOTTOM
    }
}