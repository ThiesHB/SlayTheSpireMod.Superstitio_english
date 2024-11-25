package superstitioapi.cardModifier

import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockInstance

interface RenderAsBlockPower {
    fun getBlockInstance(): BlockInstance

    var _blockInstance: BlockInstance?
}
