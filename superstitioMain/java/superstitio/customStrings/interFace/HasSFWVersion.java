package superstitio.customStrings.interFace;

import java.util.List;

public interface HasSFWVersion<T> extends HasDifferentVersionStringSet<T> {
    T getSFWVersion();

    void initialSFW(T sfw);

    void setupSFWStringByWordReplace(List<WordReplace> replaceRules);

//    default void replaceWord_String(List<WordReplace> replaceRules, Function<T, String> stringGetter, BiConsumer<T, String> stringSetter) {
//        stringSetter.accept(getSFWVersion(), WordReplace.replaceWord(stringGetter.apply(
//                        StringSetUtility.isNullOrEmpty(stringGetter.apply(getSFWVersion()))
//                                ? getOriginVersion()
//                                : getSFWVersion())
//                , replaceRules));
//    }
//
//    default void replaceWord_StringArray(List<WordReplace> replaceRules, Function<T, String[]> stringGetter, BiConsumer<T, String[]> stringSetter) {
//        stringSetter.accept(getSFWVersion(), WordReplace.replaceWord(stringGetter.apply(
//                        StringSetUtility.isNullOrEmpty(stringGetter.apply(getSFWVersion()))
//                                ? getOriginVersion()
//                                : getSFWVersion())
//                , replaceRules));
//    }

//    default void replaceWord_StringMap(List<WordReplace> replaceRules, Function<T, Map<String, String>> stringGetter, BiConsumer<T, Map<String,
//            String>> stringSetter) {
//        stringSetter.accept(getSFWVersion(), WordReplace.replaceWord(stringGetter.apply(
//                        StringSetUtility.isNullOrEmpty(stringGetter.apply(getSFWVersion()))
//                                ? getOriginVersion()
//                                : getSFWVersion())
//                , replaceRules));
//    }
}
