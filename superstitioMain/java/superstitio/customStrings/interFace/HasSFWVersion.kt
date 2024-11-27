package superstitio.customStrings.interFace

interface HasSFWVersion<T> : HasDifferentVersionStringSet<T> {
    val SfwVersion: T

    fun initialSFW(sfw: T)

    fun setupSFWStringByWordReplace(replaceRules: List<WordReplace>)
    fun makeSFWCopy(): HasDifferentVersionStringSet<T>
}
