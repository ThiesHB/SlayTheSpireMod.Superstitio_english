package superstitioapi.pet.animationSize;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.List;

import static superstitioapi.utils.CardUtility.getRandomFromList;

public class AnimationSize {

    public static void reloadAnimation(AbstractCreature creature, float scale) {
        if (!AnimationUrlData.urlDataMap.containsKey(creature.getClass().getName())) return;
        AnimationUrlData urlData = AnimationUrlData.urlDataMap.get(creature.getClass().getName());
        ReflectionHacks.privateMethod(AbstractCreature.class, "loadAnimation", String.class, String.class, float.class)
                .invoke(creature, urlData.atlasUrl, urlData.skeletonUrl, scale);
        AnimationState.TrackEntry trackEntry;
        List<Animation> animations = new ArrayList<>();
        creature.state.getData().getSkeletonData().getAnimations().forEach(animation -> {
            if (animation.getName().contains("Idle") || animation.getName().contains("idle")) {
                animations.add(animation);
            }
        });
        if (animations.isEmpty())
            trackEntry = creature.state.setAnimation(0, getRandomFromList(creature.state.getData().getSkeletonData().getAnimations(), new Random()), true);
        else
            trackEntry = creature.state.setAnimation(0, getRandomFromList(animations, new Random()), true);
        AnimationStateDataParam.tryReload(creature);
        trackEntry.setTime(trackEntry.getEndTime() * MathUtils.random());
    }
}
