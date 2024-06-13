package superstitioapi.pet.animationSize;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class AnimationSize {

    public static void reloadAnimation(AbstractCreature creature, float scale) {
        if (!AnimationUrlData.urlDataMap.containsKey(creature.getClass().getName())) return;
        AnimationState.TrackEntry oldTrackEntry = creature.state.getTracks().first();
        if (oldTrackEntry == null) return;
        AnimationUrlData urlData = AnimationUrlData.urlDataMap.get(creature.getClass().getName());
        ReflectionHacks.privateMethod(AbstractCreature.class, "loadAnimation", String.class, String.class, float.class)
                .invoke(creature, urlData.atlasUrl, urlData.skeletonUrl, scale);
        AnimationState.TrackEntry trackEntry;
        trackEntry = creature.state.setAnimation(0, oldTrackEntry.getAnimation().getName(), oldTrackEntry.getLoop());
        AnimationStateDataParam.tryReload(creature);
        trackEntry.setTime(trackEntry.getEndTime() * MathUtils.random());
    }
}
