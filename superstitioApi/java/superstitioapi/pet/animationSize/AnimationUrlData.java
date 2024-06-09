package superstitioapi.pet.animationSize;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.HashMap;
import java.util.Map;

public class AnimationUrlData {
    public static Map<String, AnimationUrlData> urlDataMap = new HashMap<>();
    public String atlasUrl;
    public String skeletonUrl;

    public AnimationUrlData(String atlasUrl, String skeletonUrl) {
        this.atlasUrl = atlasUrl;
        this.skeletonUrl = skeletonUrl;
    }

    private static void addUrlData(Class<? extends AbstractCreature> cClass, String atlasUrl, String skeletonUrl) {
        urlDataMap.put(cClass.getName(), new AnimationUrlData(atlasUrl, skeletonUrl));
    }

    @SpirePatch2(
            clz = AbstractCreature.class,
            method = "loadAnimation",
            paramtypez = {String.class, String.class, float.class})
    public static class GetUrlDataPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCreature __instance, String atlasUrl, String skeletonUrl, float scale) {
            AnimationUrlData.addUrlData(__instance.getClass(), atlasUrl, skeletonUrl);
        }
    }
}
