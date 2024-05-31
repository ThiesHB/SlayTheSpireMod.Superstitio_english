package superstitioapi.utils;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.TheBombPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect;
import superstitioapi.Logger;
import superstitioapi.powers.interfaces.CopyAblePower;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PowerUtility {

    public static final float BubbleMessageHigher_HEIGHT = 50.0f * Settings.scale;
    private static final Class<? extends AbstractPower>[] specialCopyPower = new Class[]{TheBombPower.class};

    public static void BubbleMessage(AbstractPower power, boolean isDeBuffVer, String message) {
        BubbleMessage(power.owner.hb, isDeBuffVer, message, power.owner.animX, 0);
    }

    public static void BubbleMessage(Hitbox hitbox, boolean isDeBuffVer, String message, float XOffset, float YOffset) {
        if (isDeBuffVer) {
            AbstractDungeon.effectList.add(new PowerDebuffEffect(hitbox.cX - XOffset,
                    hitbox.cY + hitbox.height / 2.0f + YOffset, message));
        }
        else {
            AbstractDungeon.effectList.add(new PowerBuffEffect(hitbox.cX - XOffset,
                    hitbox.cY + hitbox.height / 2.0f + YOffset, message));
        }
    }

    public static void BubbleMessage(Hitbox hitbox, boolean isDeBuffVer, String message) {
        BubbleMessage(hitbox, isDeBuffVer, message, 0, 0);
    }

    public static void BubbleMessageHigher(AbstractPower power, boolean isDeBuffVer, String message) {
        BubbleMessage(power.owner.hb, isDeBuffVer, message, power.owner.animX, BubbleMessageHigher_HEIGHT);
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

    public static Optional<AbstractPower> tryCopyPower(AbstractPower power, AbstractCreature newOwner) {
        final Class<? extends AbstractPower> pClass = power.getClass();
        final Constructor<?>[] constructors = pClass.getDeclaredConstructors();
        AbstractPower instance = null;
        try {
            if (Arrays.asList(specialCopyPower).contains(pClass)) {
                if (pClass.equals(TheBombPower.class)) {
                    instance = new TheBombPower(newOwner, 0, 40);
                }
            }
            else if (power instanceof CopyAblePower) {
                instance = ((CopyAblePower) power).makeCopy(newOwner);
            }
            else {
                final int paramCount = constructors[0].getParameterCount();
                final Class<?>[] paramTypes = constructors[0].getParameterTypes();
                final Object[] paramNewInstance = new Object[paramCount];
                for (int i = 0; i < paramCount; ++i) {
                    final Class<?> param = paramTypes[i];
                    if (AbstractCreature.class.isAssignableFrom(param)) {
                        paramNewInstance[i] = newOwner;
                    }
                    else if (Integer.TYPE.isAssignableFrom(param)) {
                        paramNewInstance[i] = 0;
                    }
                    else if (String.class.isAssignableFrom(param)) {
                        paramNewInstance[i] = "";
                    }
//                    else if (AbstractCard.class.isAssignableFrom(param)) {
//                        paramNewInstance[i] = AbstractDungeon.player.cardInUse;
//                    }
                    else if (Boolean.TYPE.isAssignableFrom(param)) {
                        paramNewInstance[i] = true;
                    }
                }
                instance = (AbstractPower) constructors[0].newInstance(paramNewInstance);
                instance.amount = power.amount;
            }
        } catch (Exception e) {
            Logger.warning("Failed to copy power button for: " + pClass.getName());
        }
        if (instance != null)
            return Optional.of(instance);
        else
            return Optional.empty();
    }

}
