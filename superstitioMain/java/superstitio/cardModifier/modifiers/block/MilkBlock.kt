package superstitio.cardModifier.modifiers.block

import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.megacrit.cardcrawl.cards.DamageInfo
import superstitio.DataManager
import superstitio.cardModifier.modifiers.AbstractLupaBlock

class MilkBlock : AbstractLupaBlock(ID)
{
    override fun onThisBlockDamaged(info: DamageInfo, lostAmount: Int)
    {
    }

    override fun makeCopy(): AbstractBlockModifier
    {
        return MilkBlock()
    }

    override fun blockImageColor(): Color
    {
        return Color.WHITE.cpy()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(MilkBlock::class.java)
    }
}