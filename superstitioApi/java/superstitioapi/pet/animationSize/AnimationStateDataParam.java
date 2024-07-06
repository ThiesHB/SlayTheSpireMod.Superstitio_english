package superstitioapi.pet.animationSize;

import basemod.ReflectionHacks;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.*;

public class AnimationStateDataParam {
    public static Map<String, List<AnimationStateDataParam>> SkeletonName_DataParamListMap = new HashMap<>();
    public String fromName;
    public String toName;
    public float duration;

    public AnimationStateDataParam(String fromName, String toName, float duration) {
        this.fromName = fromName;
        this.toName = toName;
        this.duration = duration;
    }

    public static void tryReload(AbstractCreature creature) {
        Skeleton skeleton = ReflectionHacks.getPrivate(creature, AbstractCreature.class, "skeleton");
        String key = skeleton.getData().getHash();
        if (!SkeletonName_DataParamListMap.containsKey(key)) return;
        SkeletonName_DataParamListMap.get(key).forEach(dataParam -> {
            Animation from = skeleton.getData().findAnimation(dataParam.fromName);
            if (from == null) return;
            Animation to = skeleton.getData().findAnimation(dataParam.toName);
            if (to == null) return;
            creature.state.getData().setMix(from, to, dataParam.duration);
        });
    }

    private static void addData(String skeletonDataName, String fromName, String toName, float duration) {
        AnimationStateDataParam dataParam = new AnimationStateDataParam(fromName, toName, duration);
        if (SkeletonName_DataParamListMap.containsKey(skeletonDataName))
            SkeletonName_DataParamListMap.get(skeletonDataName).add(dataParam);
        else
            SkeletonName_DataParamListMap.put(skeletonDataName, new ArrayList<>(Collections.singletonList(dataParam)));
    }

    @SpirePatch2(
            clz = AnimationStateData.class,
            method = "setMix",
            paramtypez = {String.class, String.class, float.class})
    public static class AnimationStateDataPatch {
        @SpirePostfixPatch
        public static void Postfix(AnimationStateData __instance, String fromName, String toName, float duration) {
            addData(__instance.getSkeletonData().getHash(), fromName, toName, duration);
        }
    }
}
