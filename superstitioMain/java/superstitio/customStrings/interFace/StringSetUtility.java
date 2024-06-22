package superstitio.customStrings.interFace;

import superstitio.Logger;
import superstitio.SuperstitioConfig;

import java.util.Map;

public class StringSetUtility {
    public static boolean shouldReturnSFWVersion(String sfwSting) {
        return SuperstitioConfig.isEnableSFW() && !isNullOrEmpty(sfwSting);
    }

    public static boolean shouldReturnSFWVersion() {
        return SuperstitioConfig.isEnableSFW();
    }

    public static boolean shouldReturnSFWVersion(String[] sfwStings) {
        return SuperstitioConfig.isEnableSFW() && !isNullOrEmpty(sfwStings);
    }

    public static boolean shouldReturnSFWVersion(Map<String, String> sfwStings) {
        return SuperstitioConfig.isEnableSFW() && !isNullOrEmpty(sfwStings);
    }

    public static boolean isNullOrEmpty(String sfwStings) {
        return sfwStings == null || sfwStings.isEmpty();
    }

    public static boolean isNullOrEmpty(String[] sfwStings) {
        return sfwStings == null || sfwStings.length == 0;
    }

    public static boolean isNullOrEmpty(Map<String, String> sfwStings) {
        return sfwStings == null || sfwStings.isEmpty();
    }

    public static <T extends HasDifferentVersionStringSet<?>> T getCustomStringsWithSFW(String keyName, Map<String, T> stringTMap, Class<T> tClass) {
        if (stringTMap.containsKey(keyName)) {
            return stringTMap.get(keyName);
        } else {
            Logger.debug(tClass.getSimpleName() + ": " + keyName + " not found");
            try {
                T customStringsWithSFW = tClass.newInstance();
                customStringsWithSFW.initialSelfBlack();
                customStringsWithSFW.initial();
                return customStringsWithSFW;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
