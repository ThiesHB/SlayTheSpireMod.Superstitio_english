package superstitioapi.pet.animationSize

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.core.AbstractCreature

class AnimationUrlData(var atlasUrl: String, var skeletonUrl: String) {
    companion object {
        var urlDataMap: MutableMap<String, AnimationUrlData> = HashMap()
        private fun addUrlData(cClass: Class<out AbstractCreature>, atlasUrl: String, skeletonUrl: String) {
            urlDataMap[cClass.name] = AnimationUrlData(atlasUrl, skeletonUrl)
        }
        @SpirePatch2(
            clz = AbstractCreature::class,
            method = "loadAnimation",
            paramtypez = [String::class, String::class, Float::class]
        )
        object GetUrlDataPatch {
            @SpirePostfixPatch
        @JvmStatic
            fun Postfix(__instance: AbstractCreature, atlasUrl: String, skeletonUrl: String, scale: Float) {
                addUrlData(__instance.javaClass, atlasUrl, skeletonUrl)
            }
        }

    }
}
