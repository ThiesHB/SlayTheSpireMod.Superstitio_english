package SuperstitioMod.customStrings;

import SuperstitioMod.Logger;
import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.WordReplace;

import java.util.Map;

public interface HasSFWVersion {
    static boolean ifReturnSFWVersion(String sfwSting) {
        return SuperstitioModSetup.enableSFW && sfwSting != null && !sfwSting.isEmpty();
    }

    static boolean ifReturnSFWVersion(String[] sfwStings) {
        return SuperstitioModSetup.enableSFW && sfwStings != null && sfwStings.length != 0;
    }

    static <T extends HasSFWVersion> T getCustomStringsWithSFW(String keyName, Map<String, T> stringTMap, Class<T> tClass) throws InstantiationException, IllegalAccessException {
        if (stringTMap.containsKey(keyName)) {
            return stringTMap.get(keyName);
        }
        else {
            Logger.info("[ERROR] " + tClass.getSimpleName() + ": " + keyName + " not found");
            T customStringsWithSFW = tClass.newInstance();
            customStringsWithSFW.initialBlack();
            return customStringsWithSFW;
        }
    }

    void initialBlack();

    void setupSFWStringByWordReplace(WordReplace replaceRule);
}
