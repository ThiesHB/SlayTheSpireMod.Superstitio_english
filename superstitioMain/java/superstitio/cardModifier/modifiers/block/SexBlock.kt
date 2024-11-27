package superstitio.cardModifier.modifiers.block

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.megacrit.cardcrawl.cards.DamageInfo
import superstitio.DataManager
import superstitio.cardModifier.modifiers.AbstractLupaBlock

class SexBlock : AbstractLupaBlock(ID)
{
    override fun onThisBlockDamaged(info: DamageInfo, lostAmount: Int)
    {
    }

    override fun priority(): Priority
    {
        return Priority.NORMAL
    }

    override fun makeCopy(): AbstractBlockModifier
    {
        return SexBlock()
    } //    @Override
    //    public Color blockImageColor() {
    //        return Color.PINK.cpy();
    //    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(SexBlock::class.java)
    }
}