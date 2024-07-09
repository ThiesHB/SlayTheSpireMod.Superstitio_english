package superstitioapi;


import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpireInitializer
public class DataUtility {

    public static final Map<String, Boolean> isPathExist = new HashMap<>();

    public static void initialize() {
    }

    public static String makeLocalizationPath(Settings.GameLanguage language, String filename) {
        String ret = "localization/";
        switch (language) {
            case ZHS:
                ret = ret + "zhs/";
                break;
            case ENG:
                ret = ret + "eng/";
                break;
            default:
                ret = ret + "eng/";
                break;
        }

        return getResourcesFilesPath() + ret + filename + ".json";
    }

    public static String makeShaderPath(String filename) {
        return getResourcesFilesPath() + "shader/" + filename;
    }

    public static String getModID() {
        return SuperstitioApiSetup.MOD_NAME + "Mod";
    }

    public static String makeImgFilesPath(String fileName, String... folderPaths) {
        return getImgFolderPath(makeFolderTotalString(folderPaths)) + "/" + fileName + ".png";
    }

    public static String makeFolderTotalString(String... strings) {
        if (strings.length == 0) return "";
        StringBuilder totalString = new StringBuilder();
        for (String string : strings)
            totalString.append("/").append(string);
        return totalString.toString();
    }

    public static String makeImgFilesPath_Card(String fileName, String... subFolder) {
        return makeImgFilesPath(fileName, "cards", makeFolderTotalString(subFolder));
    }

    public static String makeImgFilesPath_Relic(String fileName, String... subFolder) {
        return makeImgFilesPath(fileName, "relics", makeFolderTotalString(subFolder));
    }

    public static String makeImgFilesPath_UI(String fileName, String... subFolder) {
        return makeImgFilesPath(fileName, "ui", makeFolderTotalString(subFolder));
    }

    public static String makeImgFilesPath_Character_Lupa(String fileName, String... subFolder) {
        return makeImgFilesPath(fileName, "character", makeFolderTotalString(subFolder));
    }

    public static String makeImgFilesPath_RelicOutline(String fileName, String... subFolder) {
        return makeImgFilesPath(fileName, "relics/outline", makeFolderTotalString(subFolder));
    }

    public static String makeImgFilesPath_RelicLarge(String fileName, String... subFolder) {
        return makeImgFilesPath(fileName, "relics/large", makeFolderTotalString(subFolder));
    }

    public static String makeImgFilesPath_Orb(String fileName, String... subFolder) {
        return makeImgFilesPath(fileName, "orbs", makeFolderTotalString(subFolder));
    }

    public static String makeImgFilesPath_Power(String fileName, String... subFolder) {
        return makeImgFilesPath(fileName, "powers", makeFolderTotalString(subFolder));
    }

    public static String makeImgFilesPath_Event(String fileName, String... subFolder) {
        return makeImgFilesPath(fileName, "events", makeFolderTotalString(subFolder));
    }

    public static String MakeTextID(String idText) {
        return getModID() + ":" + idText;
    }

    public static String MakeTextID(Class<?> idClass) {
        return getModID() + ":" + idClass.getSimpleName();
    }

    public static String makeImgPath(String defaultFileName, BiFunction<String, String[], String> PathFinder, String fileName, String... subFolder) {
        return DataUtility.makeImgPath((string) -> {
        }, defaultFileName, PathFinder, fileName, subFolder);
    }

    public static String makeImgPath(Consumer<String> actionIfNoImg, String defaultFileName, BiFunction<String, String[], String> PathFinder,
                                     String fileName, String... subFolder) {
        String path;
        String idOnlyNames = DataUtility.getIdOnly(fileName);
        path = PathFinder.apply(idOnlyNames, subFolder);

        if (isPathExist.containsKey(path)) {
            if (isPathExist.get(path))
                return path;
            else
                return makeDefaultPath(defaultFileName, PathFinder);
        } else if (Gdx.files.internal(path).exists()) {
            isPathExist.put(path, true);
            return path;
        } else {
            isPathExist.put(path, false);
            Logger.warning("Can't find " + path + ". Use default img instead.");
            actionIfNoImg.accept(path);

            return makeDefaultPath(defaultFileName, PathFinder);
        }

    }

    public static String makeDefaultPath(String defaultFileName, BiFunction<String, String[], String> PathFinder) {
        return PathFinder.apply(defaultFileName, new String[]{""});
    }

    /**
     * 只输出后面的id，不携带模组信息
     *
     * @param complexIds 带有本模组信息的ID，“SuperstitionMod:xxx”
     * @return 去除前面的部分
     */
    public static String[] getIdOnly(String... complexIds) {
        return Arrays.stream(complexIds).map(DataUtility::getIdOnly).collect(Collectors.toList()).toArray(new String[]{});
    }

    public static String getIdOnly(String complexIds) {
        // 定义正则表达式，匹配最后一个冒号及其后面的所有字符
        Pattern pattern = Pattern.compile("(.*?):([^:]*)$");
        Matcher matcher = pattern.matcher(complexIds);

        // 如果匹配成功
        if (matcher.find()) {
            // 返回冒号后的内容
            return matcher.group(2);
        }
        // 如果没有匹配到冒号，则返回原字符串
        return complexIds;
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
        ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, mapName, localizationStrings);
    }

    public static ArrayList<hasUuid> removeMatch(ArrayList<hasUuid> listReturn, ArrayList<hasUuid> listInfo) {
        listReturn.removeIf(a -> listInfo.stream()
                .map(hasUuid::getUuid)
                .collect(Collectors.toList())
                .contains(a.getUuid()));
        return listReturn;
    }

    static <T> T makeJsonStringFromFile(String fileName, Class<T> objectClass) {
        Gson gson = new Gson();
        String json = Gdx.files.internal(DataUtility.makeLocalizationPath(Settings.language, fileName))
                .readString(String.valueOf(StandardCharsets.UTF_8));
        return gson.fromJson(json, objectClass);
    }

    private static String getResourcesFilesPath() {
        return getModID() + "Resources/";
    }

    private static String getImgFolderPath(String path) {
        return getResourcesFilesPath() + "img" + path;
    }

    private static <T> Optional<ParameterizedType> GetTypeOfMapByAComplexFunctionBecauseTheMotherfuckerGenericProgrammingWayTheFuckingJavaUse(
            Class<T> tClass) {
        Field[] var0 = DataUtility.class.getDeclaredFields();

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

    public interface hasUuid {
        String getUuid();
    }
}
