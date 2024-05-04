package SuperstitioMod.customStrings;

import SuperstitioMod.Logger;
import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.WordReplace;

import java.util.Map;

public interface HasSFWVersion {
    static boolean ifReturnSFWVersion(String sfwSting) {
        return SuperstitioModSetup.enableSFW && !isEmptyOrNull(sfwSting);
    }

    static boolean ifReturnSFWVersion(String[] sfwStings) {
        return SuperstitioModSetup.enableSFW && !isEmptyOrNull(sfwStings);
    }

    static boolean isEmptyOrNull(String sfwStings) {
        return sfwStings != null && !sfwStings.isEmpty();
    }

    static boolean isEmptyOrNull(String[] sfwStings) {
        return sfwStings != null && sfwStings.length != 0;
    }

    static <T extends HasSFWVersion> T getCustomStringsWithSFW(String keyName, Map<String, T> stringTMap, Class<T> tClass) {
        if (stringTMap.containsKey(keyName)) {
            return stringTMap.get(keyName);
        } else {
            Logger.warning(tClass.getSimpleName() + ": " + keyName + " not found");
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

    void setupSFWStringByWordReplace(WordReplace replaceRule);
}

