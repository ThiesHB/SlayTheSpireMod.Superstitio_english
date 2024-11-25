package superstitio.delayHpLose

import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import superstitio.DataManager
import superstitio.cardModifier.modifiers.AbstractLupaBlock

class DelayRemoveDelayHpLoseBlock : AbstractLupaBlock(ID) {
    //    @Override
    //    public float onModifyBlock(float block, AbstractCard card) {
    //        return super.onModifyBlock(block, card);
    //    }
    override fun makeCopy(): AbstractBlockModifier {
        return DelayRemoveDelayHpLoseBlock()
    }

    override fun blockImageColor(): Color {
        return Color.PINK.cpy()
    }

    companion object {
        val ID: String = DataManager.MakeTextID(DelayRemoveDelayHpLoseBlock::class.java)
    }
}