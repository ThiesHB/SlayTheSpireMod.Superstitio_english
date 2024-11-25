package superstitio.customStrings.interFace

import superstitio.Logger
import superstitio.SuperstitioConfig

object StringSetUtility {
    fun shouldReturnSFWVersion(sfwSting: String?): Boolean {
        return SuperstitioConfig.isEnableSFW() && !sfwSting.isNullOrEmpty()
    }

    fun shouldReturnSFWVersion(): Boolean {
        return SuperstitioConfig.isEnableSFW()
    }

    fun shouldReturnSFWVersion(sfwStings: Array<String>?): Boolean {
        return SuperstitioConfig.isEnableSFW() && !sfwStings.isNullOrEmpty()
    }

    fun shouldReturnSFWVersion(sfwStings: Map<String, String>?): Boolean {
        return SuperstitioConfig.isEnableSFW() && !sfwStings.isNullOrEmpty()
    }

//    fun isNullOrEmpty(sfwStings: String?): Boolean {
//        return sfwStings.isNullOrEmpty()
//    }
//
//    fun isNullOrEmpty(sfwStings: Array<String>?): Boolean {
//        return sfwStings.isNullOrEmpty()
//    }
//
//    fun isNullOrEmpty(sfwStings: Map<String, String>?): Boolean {
//        return sfwStings == null || sfwStings.isEmpty()
//    }

    fun <T : HasDifferentVersionStringSet<*>> getCustomStringsWithSFW(
        keyName: String,
        stringTMap: Map<String, T>,
        tClass: Class<T>
    ): T {
        if (stringTMap.containsKey(keyName)) {
            return stringTMap[keyName]!!
        } else {
            Logger.debug(tClass.simpleName + ": " + keyName + " not found")
            try {
                val customStringsWithSFW = tClass.newInstance()
                customStringsWithSFW.initialSelfBlack()
                customStringsWithSFW.initial()
                return customStringsWithSFW
            } catch (e: InstantiationException) {
                throw RuntimeException(e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            }
        }
    }
}
