package superstitio.powers

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.*
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitio.customStrings.interFace.StringSetUtility
import superstitio.customStrings.stringsSet.PowerStringsSet
import superstitioapi.utils.UpdateDescriptionAdvanced

abstract class AbstractSuperstitioPower : AbstractPower, UpdateDescriptionAdvanced
{
    lateinit var powerStrings: PowerStringsSet

    override var descriptionArgs: Array<out Any>? = null

    protected constructor()

    @JvmOverloads
    constructor(
        id: String,
        owner: AbstractCreature,
        amount: Int,
        powerType: PowerType = PowerType.BUFF,
        needUpdateDescription: Boolean = true
    )
    {
        SetupPower(id, getPowerStringsWithSFW(id), owner, amount, powerType, needUpdateDescription)
    }

    protected fun SetupPower(
        id: String, powerStrings: PowerStringsSet, owner: AbstractCreature, amount: Int, powerType: PowerType,
        needUpdateDescription: Boolean
    )
    {
        this.name = powerStrings.getNAME()
        this.ID = id
        this.owner = owner
        this.type = powerType

        this.amount = amount
        this.powerStrings = powerStrings

        val path128 = makeImgPath(id, IconSize.Big)
        val path48 = makeImgPath(id, IconSize.Small)
        this.region128 = AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84)
        this.region48 = AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32)

        if (needUpdateDescription) this.updateDescription()
    }

    protected fun renderAmount2(sb: SpriteBatch?, x: Float, y: Float, c: Color?, amount2: Int)
    {
        if (amount2 <= 0) return
        FontHelper.renderFontRightTopAligned(
            sb,
            FontHelper.powerAmountFont,
            "" + amount2,
            x,
            y + 15.0f * Settings.scale,
            this.fontScale,
            c
        )
    }

    protected fun update_showTips(hitbox: Hitbox)
    {
        hitbox.update()
        if (hitbox.hovered)
        {
            TipHelper.renderGenericTip(
                hitbox.cX + 96.0f * Settings.scale,
                hitbox.cY + 64.0f * Settings.scale, this.name, this.description
            )
        }

        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7f)
    }

    private fun makeImgPath(id: String, size: IconSize): String
    {
        return DataManager.makeImgPath(
            DEFAULT + returnSizeNum(size),
            DataManager::makeImgFilesPath_Power, id + returnSizeNum(size)
        )
    }

    private fun returnSizeNum(size: IconSize): String
    {
        return if (size == IconSize.Big) "84" else "32"
    }

    //    public void addToBot_reducePowerToOwner(final AbstractPower power) {
    //        ActionUtility.addToBot_reducePower(power, power.owner);
    //    }
//    override fun getDescriptionArgs(): Array<Any> {
//        return descriptionArgs
//    }

//    override fun setDescriptionArgs(vararg args: Any) {
//        setDescriptionToArgs({ allArgs: Array<Any> -> descriptionArgs = allArgs }, args)
//    }

    /**
     * 没事干不要重写这个
     */
    override fun updateDescription()
    {
        this.updateDescriptionArgs()
        val string = getDescriptionStrings()
        val stringMayNull = descriptionArgs?.let { String.format(string, *it) }
        this.description = stringMayNull
    }

    abstract override fun updateDescriptionArgs()

    override fun getDescriptionStrings(): String
    {
        return powerStrings.getDESCRIPTION(0)
    }

    private sealed class IconSize
    {
        data object Big : IconSize()
        data object Small : IconSize()
        companion object
        {
            fun values(): Array<IconSize>
            {
                return arrayOf(Big, Small)
            }

            fun valueOf(value: String): IconSize
            {
                return when (value)
                {
                    "Big"   -> Big
                    "Small" -> Small
                    else    -> throw IllegalArgumentException("No object superstitio.powers.AbstractSuperstitioPower.IconSize.$value")
                }
            }
        }
    }

    companion object
    {
        const val DEFAULT: String = "default"
        fun getPowerStringsWithSFW(powerID: String): PowerStringsSet
        {
            return StringSetUtility.getCustomStringsWithSFW(
                powerID,
                DataManager.powers,
                PowerStringsSet::class.java
            )
        }
    }
}

