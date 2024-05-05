package superstitio.utils;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect;

import java.util.HashMap;
import java.util.Map;

public class PowerUtility {

    private static final float BubbleMessageHigher_HEIGHT = 50.0f * Settings.scale;

    public static void BubbleMessage(AbstractPower power, boolean isDeBuffVer, String message) {
        if (isDeBuffVer) {
            AbstractDungeon.effectList.add(new PowerDebuffEffect(power.owner.hb.cX - power.owner.animX,
                    power.owner.hb.cY + power.owner.hb.height / 2.0f, message));
        } else {
            AbstractDungeon.effectList.add(new PowerBuffEffect(power.owner.hb.cX - power.owner.animX,
                    power.owner.hb.cY + power.owner.hb.height / 2.0f, message));
        }
    }

    public static void BubbleMessageHigher(AbstractPower power, boolean isDeBuffVer, String message) {
        if (isDeBuffVer) {
            AbstractDungeon.effectList.add(new PowerDebuffEffect(power.owner.hb.cX - power.owner.animX,
                    power.owner.hb.cY + power.owner.hb.height / 2.0f + BubbleMessageHigher_HEIGHT, message));
        } else {
            AbstractDungeon.effectList.add(new PowerBuffEffect(power.owner.hb.cX - power.owner.animX,
                    power.owner.hb.cY + power.owner.hb.height / 2.0f + BubbleMessageHigher_HEIGHT, message));
        }
    }

    public static <T> Map<T, Integer> mergeAndSumMaps(Map<T, Integer> map1, Map<T, Integer> map2) {
        Map<T, Integer> result = new HashMap<>();

        // 遍历第一个映射
        for (Map.Entry<T, Integer> entry : map1.entrySet()) {
            T key = entry.getKey();
            int value1 = entry.getValue();
            int value2 = map2.getOrDefault(key, 0); // 获取第二个映射中对应的值，如果不存在则默认为0
            result.put(key, value1 + value2);
        }

        // 遍历第二个映射中剩余的键值对
        for (Map.Entry<T, Integer> entry : map2.entrySet()) {
            T key = entry.getKey();
            if (!result.containsKey(key)) {
                result.put(key, entry.getValue());
            }
        }

        return result;
    }

}
