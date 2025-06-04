package superstitio.orbs

import com.badlogic.gdx.graphics.Texture
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.orbs.AbstractOrb
import superstitio.DataManager
import superstitio.customStrings.interFace.StringSetUtility
import superstitio.customStrings.stringsSet.OrbStringsSet
import superstitioapi.DataUtility
import superstitioapi.utils.UpdateDescriptionAdvanced
import superstitioapi.utils.getFormattedDescription

abstract class AbstractLupaOrb private constructor(
    ID: String,
    basePassiveAmount: Int,
    baseEvokeAmount: Int,
    orbStringsSet: OrbStringsSet,
    autoUpdateDescription: Boolean
) : AbstractOrb(), UpdateDescriptionAdvanced
{
    protected val orbStringsSet: OrbStringsSet

    override var descriptionArgs: Array<out Any>? = null

    @JvmOverloads
    constructor(
        ID: String,
        basePassiveAmount: Int = 0,
        baseEvokeAmount: Int = 0,
        autoUpdateDescription: Boolean = true
    ) : this(ID, basePassiveAmount, baseEvokeAmount, getPowerStringsWithSFW(ID), autoUpdateDescription)

    init
    {
        this.ID = ID
        this.name = orbStringsSet.getNAME()
        this.orbStringsSet = orbStringsSet
        super.evokeAmount = baseEvokeAmount
        super.passiveAmount = basePassiveAmount
        val imgPath = getImgPath(ID)
        this.img = orbTextures[imgPath]
        if (this.img == null)
        {
            this.img = ImageMaster.loadImage(imgPath)
            orbTextures[imgPath] = this.img
        }

        if (autoUpdateDescription) this.updateDescription()
    }

    fun getEvokeAmount(): Int
    {
        return super.evokeAmount
    }

    fun setEvokeAmount(amount: Int)
    {
        super.evokeAmount = amount
        super.baseEvokeAmount = amount
    }

    fun getPassiveAmount(): Int
    {
        return super.passiveAmount
    }

    fun setPassiveAmount(amount: Int)
    {
        super.passiveAmount = amount
        super.basePassiveAmount = amount
    }

    abstract override fun applyFocus()

    override fun updateDescription()
    {
        this.description = getFormattedDescription()
    }

    override fun updateDescriptionArgs()
    {
    }

    override fun getDescriptionStrings(): String
    {
        return orbStringsSet.getRightVersion().DESCRIPTION[0]
    }

    companion object
    {
        private val orbTextures: MutableMap<String, Texture> = HashMap()
        fun getPowerStringsWithSFW(cardName: String): OrbStringsSet
        {
            return StringSetUtility.getCustomStringsWithSFW(
                cardName,
                DataManager.orbs,
                OrbStringsSet::class.java
            )
        }

        protected fun getImgPath(id: String): String
        {
            return DataManager.tryGetImgPath(DataUtility.ImgSubPath.orbsPath, id, "default")
        }
    }
}
