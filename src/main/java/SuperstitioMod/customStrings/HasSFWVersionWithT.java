package SuperstitioMod.customStrings;

import SuperstitioMod.Logger;
import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.WordReplace;

import java.util.Map;

public interface HasSFWVersionWithT<T> extends HasSFWVersion {
    T getRightVersion();
}
