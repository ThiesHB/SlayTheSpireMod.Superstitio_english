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
    fun getFromRightVersion(supplier: Function<T, String?>): String
    {
        if (!supplier.apply(getRightVersion()).isNullOrEmpty())
            return supplier.apply(getRightVersion())!!
        else
            return supplier.apply(OriginVersion)!!
    }

    fun getArrayFromRightVersion(supplier: Function<T, Array<String>?>): Array<String>
    {
        return if (supplier.apply(getRightVersion()).isNullOrEmpty())
            supplier.apply(OriginVersion)!!
        else supplier.apply(getRightVersion())!!
    }

    fun getMapFromRightVersion(supplier: Function<T, Map<String, String>?>): Map<String, String>
    {
        return if (supplier.apply(getRightVersion()).isNullOrEmpty())
            supplier.apply(OriginVersion)!!
        else supplier.apply(getRightVersion())!!
    }

    override fun initial()
    {
        initialOrigin(OriginVersion)
        initialSFW(SfwVersion)
    }
}
