package superstitio.customStrings.interFace;

import java.util.Map;
import java.util.function.Function;

public interface HasOriginAndSFWVersion<T> extends HasSFWVersion<T> {
//    @Override
//    default T getRightVersion() {
//        if (StringSetUtility.shouldReturnSFWVersion())
//            return getSFWVersion();
//        return getOriginVersion();
//    }

    @Override
    default void initial() {
        initialOrigin(getOriginVersion());
        initialSFW(getSFWVersion());
    }

    default String getFromRightVersion(Function<T, String> supplier) {
        if (StringSetUtility.isNullOrEmpty(supplier.apply(getRightVersion())))
            return supplier.apply(getOriginVersion());
        else
            return supplier.apply(getRightVersion());
    }

    default String[] getArrayFromRightVersion(Function<T, String[]> supplier) {
        if (StringSetUtility.isNullOrEmpty(supplier.apply(getRightVersion())))
            return supplier.apply(getOriginVersion());
        else
            return supplier.apply(getRightVersion());
    }

    default Map<String, String> getMapFromRightVersion(Function<T, Map<String, String>> supplier) {
        if (StringSetUtility.isNullOrEmpty(supplier.apply(getRightVersion())))
            return supplier.apply(getOriginVersion());
        else
            return supplier.apply(getRightVersion());
    }
}
