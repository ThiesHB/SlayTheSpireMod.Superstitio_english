package superstitio.cardModifier.modifiers.block

import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.megacrit.cardcrawl.cards.DamageInfo
import superstitio.DataManager
import superstitio.cardModifier.modifiers.AbstractLupaBlock

class DrySemenBlock : AbstractLupaBlock(ID)
{
    override fun onThisBlockDamaged(info: DamageInfo?, lostAmount: Int)
    {
    }

    override fun makeCopy(): AbstractBlockModifier
    {
        return DrySemenBlock()
    }

    override fun priority(): Priority
    {
        return Priority.TOP
    }

    override fun subPriority(): Short
    {
        return 223
    } //Kokoro

    override fun blockImageColor(): Color
    {
        return Color.LIGHT_GRAY.cpy()
    }

    override fun amountLostAtStartOfTurn(): Int
    {
        return 0
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(DrySemenBlock::class.java)
    }
}