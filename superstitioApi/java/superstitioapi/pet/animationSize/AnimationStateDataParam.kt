package superstitioapi.pet.animationSize

import basemod.ReflectionHacks
import com.esotericsoftware.spine.AnimationStateData
import com.esotericsoftware.spine.Skeleton
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.core.AbstractCreature

class AnimationStateDataParam(var fromName: String, var toName: String, var duration: Float)
{

    companion object
    {
        var SkeletonName_DataParamListMap: MutableMap<String, MutableList<AnimationStateDataParam>> = HashMap()
        fun tryReload(creature: AbstractCreature)
        {
            val skeleton = ReflectionHacks.getPrivate<Skeleton>(creature, AbstractCreature::class.java, "skeleton")
            val key = skeleton.data.hash
            if (!SkeletonName_DataParamListMap.containsKey(key)) return
            SkeletonName_DataParamListMap[key]!!.forEach { dataParam: AnimationStateDataParam ->
                val from = skeleton.data.findAnimation(dataParam.fromName) ?: return@forEach
                val to = skeleton.data.findAnimation(dataParam.toName) ?: return@forEach
                creature.state.data.setMix(from, to, dataParam.duration)
            }
        }

        private fun addData(skeletonDataName: String, fromName: String, toName: String, duration: Float)
        {
            val dataParam = AnimationStateDataParam(fromName, toName, duration)
            if (SkeletonName_DataParamListMap.containsKey(skeletonDataName)) SkeletonName_DataParamListMap[skeletonDataName]!!
                .add(dataParam)
            else SkeletonName_DataParamListMap[skeletonDataName] = ArrayList(listOf(dataParam))
        }

        @SpirePatch2(
            clz = AnimationStateData::class,
            method = "setMix",
            paramtypez = [String::class, String::class, Float::class]
        )
        object AnimationStateDataPatch
        {
            @SpirePostfixPatch
            @JvmStatic
            fun Postfix(__instance: AnimationStateData, fromName: String, toName: String, duration: Float)
            {
                addData(__instance.skeletonData.hash, fromName, toName, duration)
            }
        }

    }
}
