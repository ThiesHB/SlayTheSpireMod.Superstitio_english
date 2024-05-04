package SuperstitioMod;


import SuperstitioMod.customStrings.*;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class DataManager {
    public static Map<String, CardStringsWithSFWAndFlavor> cards = new HashMap<>();
    public static Map<String, PowerStringsSet> powers = new HashMap<>();
    public static Map<String, ModifierStringsWithSFW> modifiers = new HashMap<>();
    public LUPA_DATA lupaData = new LUPA_DATA();

    static String makeLocalizationPath(Settings.GameLanguage language, String filename) {
        String ret = "localization/";
        switch (language) {
            case ZHS:
                ret = ret + "zhs/";
                break;
            case KOR:
                ret = ret + "kor/";
                break;
            default:
                ret = ret + "eng/";
                break;
        }

        return getResourcesFilesPath() + ret + filename + ".json";
    }

    static String getModID() {
        return SuperstitioModSetup.MOD_NAME + "Mod";
    }

    private static String getResourcesFilesPath() {
        return getModID() + "Resources/";
    }

    private static String getImgFilesPath() {
        if (!SuperstitioModSetup.enableSFW)
            return getResourcesFilesPath() + "img";
        else
            return getResourcesFilesPath() + "imgSFW";
    }

    public static String makeImgFilesPath(String... resourcePaths) {
        return getImgFilesPath() + makeTotalString(resourcePaths) + ".png";
    }

    public static String makeTotalString(String... strings) {
        StringBuilder totalString = new StringBuilder();
        for (String string : strings)
            totalString.append("/").append(string);
        return totalString.toString();
    }

    public static String makeImgFilesPath_LupaCard(String... resourcePaths) {
        return makeImgFilesPath("cards_Lupa", makeTotalString(resourcePaths));
    }

    public static String makeImgFilesPath_Relic(String... resourcePaths) {
        return makeImgFilesPath("relics", makeTotalString(resourcePaths));
    }

    public static String makeImgFilesPath_UI(String... resourcePaths) {
        return makeImgFilesPath("UI", makeTotalString(resourcePaths));
    }

    public static String makeImgFilesPath_Character_Lupa(String... resourcePaths) {
        return makeImgFilesPath("character_lupa", makeTotalString(resourcePaths));
    }

    public static String makeImgFilesPath_RelicOutline(String... resourcePaths) {
        return makeImgFilesPath("relics/outline", makeTotalString(resourcePaths));
    }

    public static String makeImgFilesPath_Orb(String... resourcePaths) {
        return makeImgFilesPath("orbs", makeTotalString(resourcePaths));
    }

    public static String makeImgFilesPath_Power(String... resourcePaths) {
        return makeImgFilesPath("powers", makeTotalString(resourcePaths));
    }

    public static String makeImgFilesPath_Event(String... resourcePaths) {
        return makeImgFilesPath("events", makeTotalString(resourcePaths));
    }


    static <T extends HasSFWVersion> void loadCustomStringsFile(String fileName, Map<String, T> target, Class<T> tSetClass) {
        Logger.debug("loadJsonStrings: " + tSetClass.getTypeName());
        String jsonString = Gdx.files.internal(DataManager.makeLocalizationPath(Settings.language, fileName))
                .readString(String.valueOf(StandardCharsets.UTF_8));
        ParameterizedType typeToken =
                GetTypeOfMapByAComplexFunctionBecauseTheMotherfuckerGenericProgrammingWayTheFuckingJavaUse(tSetClass).orElse(null);
        Gson gson = new Gson();
        Map<String, T> map = gson.fromJson(jsonString, typeToken);
        map.forEach((id, strings) -> {
            strings.initialOrigin();
            if (strings instanceof HasTextID)
                ((HasTextID) strings).setTextID(id);
        });
        target.putAll(map);
//        if (typeToken == null) return;
//        Map<String, Object> baseModStringMap = new HashMap<>();
//        try {
//            if (tSetClass.newInstance() instanceof HasSFWVersionWithT) {
//                map.forEach((string, value) -> baseModStringMap.put(string, ((HasSFWVersionWithT<?>) value).getRightVersion()));
//                setJsonStrings(((HasSFWVersionWithT<?>) tSetClass.newInstance()).getTClass(), baseModStringMap);
//            }
//        } catch (InstantiationException | IllegalAccessException e) {
//            Logger.error(e);
//        }
    }

    private static <T> Optional<ParameterizedType> GetTypeOfMapByAComplexFunctionBecauseTheMotherfuckerGenericProgrammingWayTheFuckingJavaUse(
            Class<T> tClass) {
        Field[] var0 = DataManager.class.getDeclaredFields();

        for (Field f : var0) {
            Type type = f.getGenericType();
            if (!(type instanceof ParameterizedType)) continue;
            ParameterizedType pType = (ParameterizedType) type;
            Type[] typeArgs = pType.getActualTypeArguments();
            if (typeArgs.length == 2 && typeArgs[0].equals(String.class) && typeArgs[1].equals(tClass))
                return Optional.of($Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, typeArgs[1]));
        }
        return Optional.empty();
    }

    public static String MakeTextID(String idText) {
        return getModID() + ":" + idText;
    }


    /**
     * 只输出后面的id，不携带模组信息
     *
     * @param complexIds 带有本模组信息的ID，“SuperstitionMod:xxx”
     * @return 去除前面的部分
     */
    public static String[] getIdOnly(String... complexIds) {
        return Arrays.stream(complexIds)
                .map(complexId -> complexId.replace(DataManager.MakeTextID(""), ""))
                .toArray(String[]::new);
    }

    static String replaceString(WordReplace wordReplace, String string) {
        if (string != null && string.contains(wordReplace.WordOrigin))
            return string.replace(wordReplace.WordOrigin, wordReplace.WordReplace);
        return string;
    }

    public static String makeImgPath(String defaultFileName, Function<String[], String> PathFinder, String... id) {
        String path;
        path = PathFinder.apply(DataManager.getIdOnly(id));
        if (Gdx.files.internal(path).exists())
            return path;
        Logger.warning("Can't find " + Arrays.toString(DataManager.getIdOnly(id)) + ". Use default img instead.");
        return PathFinder.apply(new String[]{defaultFileName});
    }

    static <T> T makeJsonStringFromFile(String fileName, Class<T> objectClass) {
        Gson gson = new Gson();
        String json = Gdx.files.internal(DataManager.makeLocalizationPath(Settings.language, fileName))
                .readString(String.valueOf(StandardCharsets.UTF_8));
        return gson.fromJson(json, objectClass);
    }

    static void replaceStringsInObj(Object obj, WordReplace wordReplace) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(obj) instanceof String) {
                    String string = (String) field.get(obj);
                    string = replaceString(wordReplace, string);
                    field.set(obj, string);
                } else if (field.get(obj) instanceof String[]) {
                    String[] values = (String[]) field.get(obj);
                    if (values == null || values.length == 0)
                        continue;
                    String[] list = new String[values.length];
                    for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
                        String string = values[i];
                        String apply = replaceString(wordReplace, string);
                        list[i] = apply;
                    }

                    field.set(obj, list);
                }
            } catch (IllegalAccessException e) {
                Logger.error(e);
            }
        }
    }

    public static <T> Optional<String> getTypeMapFromLocalizedStrings(Class<T> tClass) {
        Logger.run("initializeTypeMaps");
        for (Field f : LocalizedStrings.class.getDeclaredFields()) {
            Type type = f.getGenericType();
            if (type instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) type;
                Type[] typeArgs = pType.getActualTypeArguments();
                if (typeArgs.length == 2 && typeArgs[0] == String.class && typeArgs[1] == tClass)
                    return Optional.of(f.getName());
            }
        }
        return Optional.empty();
    }

    public static void setJsonStrings(Class<?> tClass, Map<String, Object> GivenMap) {
        String mapName = getTypeMapFromLocalizedStrings(tClass).orElse("");
        if (mapName.isEmpty()) return;
        Map<String, Object> localizationStrings = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, mapName);
        localizationStrings.putAll(GivenMap);
        Logger.temp("loadJsonStrings: " + tClass.getTypeName());
        ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, mapName, localizationStrings);
    }


    //    @SpireInitializer
    public static class LUPA_DATA {

        public static final Color LUPA_COLOR = new Color(250.0F / 255.0F, 20.0F / 255.0F, 147.0F / 255.0F, 1.0F);
        // 在卡牌和遗物描述中的能量图标
        public String SMALL_ORB = makeImgFilesPath_Character_Lupa("small_orb");
        // 在卡牌预览界面的能量图标
        public String BIG_ORB = makeImgFilesPath_Character_Lupa("card_orb");
        // 小尺寸的能量图标（战斗中，牌堆预览）
        public String ENERGY_ORB = makeImgFilesPath_Character_Lupa("cost_orb");
        // 攻击牌的背景（小尺寸）
        public String BG_ATTACK_512 = makeImgFilesPath("512", "bg_attack_512");
        // 能力牌的背景（小尺寸）
        public String BG_POWER_512 = makeImgFilesPath("512", "bg_power_512");
        // 技能牌的背景（小尺寸）
        public String BG_SKILL_512 = makeImgFilesPath("512", "bg_skill_512");
        // 攻击牌的背景（大尺寸）
        public String BG_ATTACK_1024 = makeImgFilesPath("1024", "bg_attack");
        // 能力牌的背景（大尺寸）
        public String BG_POWER_1024 = makeImgFilesPath("1024", "bg_power");
        // 技能牌的背景（大尺寸）
        public String BG_SKILL_1024 = makeImgFilesPath("1024", "bg_skill");
        //选英雄界面的角色图标、选英雄时的背景图片
        public String LUPA_CHARACTER_BUTTON = makeImgFilesPath_Character_Lupa("Character_Button");
        // 人物选择界面的立绘
        public String LUPA_CHARACTER_PORTRAIT = makeImgFilesPath_Character_Lupa("Character_Portrait");

    }
}
