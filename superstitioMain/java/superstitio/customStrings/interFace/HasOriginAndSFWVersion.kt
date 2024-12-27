package superstitio.customStrings.interFace

import java.util.function.Function

interface HasOriginAndSFWVersion<T> : HasSFWVersion<T>
{
    //    @Override
    //    default T getRightVersion() {
    //        if (StringSetUtility.shouldReturnSFWVersion())
    //            return getSFWVersion();
    //        return getOriginVersion();
    //    }
    fun getFromRightVersion(supplier: Function<T, String?>): String?
    {
        return if (supplier.apply(getRightVersion()).isNullOrEmpty())
            supplier.apply(OriginVersion)
        else
            supplier.apply(getRightVersion())
    }

    fun getArrayFromRightVersion(supplier: Function<T, Array<String>?>): Array<String>?
    {
        return if (supplier.apply(getRightVersion()).isNullOrEmpty())
            supplier.apply(OriginVersion)
        else supplier.apply(getRightVersion())
    }

    fun getMapFromRightVersion(supplier: Function<T, Map<String, String>?>): Map<String, String>?
    {
        return if (supplier.apply(getRightVersion()).isNullOrEmpty())
            supplier.apply(OriginVersion)
        else supplier.apply(getRightVersion())
    }

    override fun initial()
    {
        initialOrigin(OriginVersion)
        initialSFW(SfwVersion)
    }

    fun updateFieldIfEmpty(sfwField: String?, originField: String?, replaceRules: List<WordReplace>): String
    {
        return sfwField.takeUnless(String?::isNullOrEmpty) ?: WordReplace.replaceWord(originField, replaceRules)
    }

    fun updateFieldIfEmpty(
        sfwField: Array<String>?,
        originField: Array<String>?,
        replaceRules: List<WordReplace>
    ): Array<String>
    {
        return if (sfwField == null || originField == null || sfwField.size != originField.size)
        {
            WordReplace.replaceWord(originField, replaceRules)
        }
        else
        {
            sfwField.mapIndexed { index, item ->
                item.takeUnless(String::isNullOrEmpty) ?: WordReplace.replaceWord(originField[index], replaceRules)
            }.toTypedArray()
        }
    }
}

fun String?.takeIfNullOrEmpty(default: String?): String?
{
    return this.takeUnless(String?::isNullOrEmpty) ?: default
}

fun Array<String>?.takeIfNullOrEmpty(default: Array<String>?): Array<String>?
{
    return if (this == null || default == null || this.size != default.size)
    {
        default
    }
    else
    {
        this.mapIndexed { index, item ->
            item.takeUnless(String::isNullOrEmpty) ?: default[index]
        }.toTypedArray()
    }
}
