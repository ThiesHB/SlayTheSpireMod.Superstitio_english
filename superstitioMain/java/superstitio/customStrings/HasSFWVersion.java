package superstitio.customStrings;

import superstitio.Logger;
import superstitio.SuperstitioConfig;

import java.util.List;
import java.util.Map;

public interface HasSFWVersion {
    static boolean shouldReturnSFWVersion(String sfwSting) {
        return SuperstitioConfig.isEnableSFW() && !isNullOrEmpty(sfwSting);
    }

    static boolean shouldReturnSFWVersion(String[] sfwStings) {
        return SuperstitioConfig.isEnableSFW() && !isNullOrEmpty(sfwStings);
    }

    static boolean isNullOrEmpty(String sfwStings) {
        return sfwStings == null || sfwStings.isEmpty();
    }

    static boolean isNullOrEmpty(String[] sfwStings) {
        return sfwStings == null || sfwStings.length == 0;
    }

    static <T extends HasSFWVersion> T getCustomStringsWithSFW(String keyName, Map<String, T> stringTMap, Class<T> tClass) {
        if (stringTMap.containsKey(keyName)) {
            return stringTMap.get(keyName);
        }
        else {
            Logger.debug(tClass.getSimpleName() + ": " + keyName + " not found");
            try {
                T customStringsWithSFW = tClass.newInstance();
                customStringsWithSFW.initialSelfBlack();
                customStringsWithSFW.initialOrigin();
                return customStringsWithSFW;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    default void initialOrigin() {
    }

    void initialSelfBlack();

    void setupSFWStringByWordReplace(List<WordReplace> replaceRules);
}

