package superstitioapi.pet.animationSize

import basemod.ReflectionHacks
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.core.AbstractCreature

object AnimationSize
{
    fun reloadAnimation(creature: AbstractCreature, scale: Float)
    {
        if (!AnimationUrlData.urlDataMap.containsKey(creature.javaClass.name)) return
        val oldTrackEntry = creature.state.tracks.first() ?: return
        val urlData: AnimationUrlData = AnimationUrlData.urlDataMap.get(creature.javaClass.name)!!
        ReflectionHacks.privateMethod(
            AbstractCreature::class.java,
            "loadAnimation",
            String::class.java,
            String::class.java,
            Float::class.javaPrimitiveType
        )
            .invoke<Any>(creature, urlData.atlasUrl, urlData.skeletonUrl, scale)
        val trackEntry = creature.state.setAnimation(0, oldTrackEntry.animation.name, oldTrackEntry.loop)
        AnimationStateDataParam.tryReload(creature)
        trackEntry.time = trackEntry.endTime * MathUtils.random()
    }
}
