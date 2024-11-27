package superstitio.customStrings.interFace

interface HasDifferentVersionStringSet<T>
{
    fun getRightVersion(): T

    fun initial()

    fun initialSelfBlack()

    val OriginVersion: T

    fun initialOrigin(origin: T)

    fun getSubClass(): Class<T>

    fun makeCopy(): HasDifferentVersionStringSet<T>
}

