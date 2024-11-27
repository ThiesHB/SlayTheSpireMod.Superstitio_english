package superstitio.delayHpLose

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.cards.AbstractCard

@SpirePatch2(clz = AbstractCard::class, method = "<class>")
object GainBlockTypeFields
{
    @JvmField
    var ifReduceDelayHpLose: SpireField<Boolean?> = SpireField<Boolean?> { false }

    @JvmField
    var ifDelayReduceDelayHpLose: SpireField<Boolean?> = SpireField<Boolean?> { false }
}