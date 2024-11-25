package superstitioapi.relicToBlight

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.blights.AbstractBlight
import com.megacrit.cardcrawl.helpers.BlightHelper

object CustomBlightPatch {
    val myCustomBlight: MutableSet<AbstractBlight> = HashSet()

    /**
     * 需要在初始化时就添加好，使用InfoBlight的话，只需要用它的注册函数就行了，不需要用这个
     */
    fun Assign(addedBlight: AbstractBlight) {
        if (myCustomBlight
                .any { blight: AbstractBlight -> blight.blightID == addedBlight.blightID }
        ) return
        myCustomBlight.add(addedBlight)
    }

    /**
     * 为InfoBlight写了特殊判定，如果要生成多个遗物的话会有用
     */
    @SpirePatch2(clz = BlightHelper::class, method = "getBlight")
    object AddMyCustomBlight {
        @SpirePrefixPatch
        @JvmStatic
        fun Map(id: String): SpireReturn<AbstractBlight> {
            val blightOptional = myCustomBlight.firstOrNull { blight: AbstractBlight -> blight.blightID == id }
            if (blightOptional!=null) {
                if (blightOptional is InfoBlight) return SpireReturn.Return(blightOptional.makeCopy())
                return SpireReturn.Return(blightOptional)
            }
            return SpireReturn.Continue()
        }
    }
}
