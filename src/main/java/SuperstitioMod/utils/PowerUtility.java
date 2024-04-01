package SuperstitioMod.utils;

import SuperstitioMod.SuperstitioModSetup;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class PowerUtility {
    public static void BubbleMessage(AbstractPower power, boolean isDeBuffVer, String message) {
        if (isDeBuffVer) {
            AbstractDungeon.effectList.add(new PowerDebuffEffect(power.owner.hb.cX - power.owner.animX,
                    power.owner.hb.cY + power.owner.hb.height / 2.0f, message));
        } else {
            AbstractDungeon.effectList.add(new PowerBuffEffect(power.owner.hb.cX - power.owner.animX,
                    power.owner.hb.cY + power.owner.hb.height / 2.0f, message));
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

//    public static <T> T copyObject(T originalObject) throws
//            IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
//        Class<?> originalClass = originalObject.getClass();
//
//        SuperstitioModSetup.logger.info("tryCopy" + originalObject);
//
//        // 获取默认构造器来创建新实例
//        Constructor<?> constructor = originalClass.getDeclaredConstructor();
//        @SuppressWarnings("unchecked")
//        T copiedObject = (T) constructor.newInstance();
//
//        // 获取所有字段，包括私有字段
//        Field[] fields = originalClass.getDeclaredFields();
//
//        for (Field field : fields) {
//            field.setAccessible(true); // 使私有字段可访问
//            // 将原对象的字段值复制到新对象
//            field.set(copiedObject, field.get(originalObject));
//        }
//
//        return copiedObject;
//    }
    public static <T> T copyObject(T obj, Type type){
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(obj), type);
    }

//    public static Object copyObject(Object obj) throws Exception {
//        //获得传递过来的对象的属性和构造器
//        Class<?> class1 = obj.getClass();
//        Field[] fields = class1.getDeclaredFields();
//        Constructor<?> constructor = class1.getConstructor();
//        Object instance = constructor.newInstance();
//        for (Field f : fields) {
//            //获得属性名字
//            String name = f.getName();
//            //获得属性类型
//            Class<?> type = f.getType();
//            //获得属性对应的set方法
//            String setMethodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
//            String getMethodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
//            //获得get方法
//            Method gmethod = class1.getDeclaredMethod(getMethodName);
//            //调用get方法获得被复制对象的一个属性值
//            Object gresult = gmethod.invoke(obj);
//            //get方法的参数类型是string,返回值类型也是
//            Method smethod = class1.getDeclaredMethod(setMethodName, type);
//            //            Method smethod = class1.getDeclaredMethod(setMethodName,gmethod.getReturnType());
//            smethod.invoke(instance, gresult);
//
//        }
//        return instance;
//    }

}
