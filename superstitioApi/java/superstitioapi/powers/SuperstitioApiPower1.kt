package superstitioapi.powers

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitioapi.DataUtility
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.UpdateDescriptionAdvanced

abstract class SuperstitioApiPower(
    id: String, powerStrings: PowerStrings, owner: AbstractCreature, amount: Int, powerType: PowerType,
    needUpdateDescription: Boolean
) : AbstractPower(), UpdateDescriptionAdvanced {

    override var descriptionArgs: Array<out Any>? = null

    init {
        SetupPower(id, powerStrings, owner, amount, powerType, needUpdateDescription)
    }

    @JvmOverloads
    constructor(
        id: String,
        owner: AbstractCreature,
        amount: Int,
        powerType: PowerType = PowerType.BUFF,
        needUpdateDescription: Boolean = true
    ) : this(id, getPowerStrings(id), owner, amount, powerType, needUpdateDescription)

    fun addToBot_applyPower(power: AbstractPower) {
        ActionUtility.addToBot_applyPower(power, this.owner)
    }

    fun addToBot_reducePowerToOwner(powerID: String, amount: Int) {
        ActionUtility.addToBot_reducePower(powerID, amount, this.owner, this.owner)
    }

    fun addToBot_removeSpecificPower(power: AbstractPower) {
        ActionUtility.addToBot_removeSpecificPower(power, power.owner)
    }

    protected fun SetupPower(
        id: String, powerStrings: PowerStrings, owner: AbstractCreature, amount: Int, powerType: PowerType,
        needUpdateDescription: Boolean
    ) {
        this.name = powerStrings.NAME
        this.ID = id
        this.owner = owner
        this.type = powerType
        this.amount = amount
        val path128 = makeImgPath(id, IconSize.Big)
        val path48 = makeImgPath(id, IconSize.Small)
        this.region128 = AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84)
        this.region48 = AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32)

        if (needUpdateDescription) this.updateDescription()
    }

    protected fun renderAmount2(sb: SpriteBatch, x: Float, y: Float, c: Color, amount2: Int) {
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

    private fun makeImgPath(id: String, size: IconSize): String {
        return DataUtility.makeImgPath(
            DEFAULT + returnSizeNum(size),
            DataUtility::makeImgFilesPath_Power,
            id + returnSizeNum(size)
        )
    }

    private fun returnSizeNum(size: IconSize): String {
        return if (size == IconSize.Big) "84" else "32"
    }

    /**
     * 没事干不要重写这个
     */
    override fun updateDescription() {
        this.updateDescriptionArgs()
        var string = getDescriptionStrings()
        string = String.format(string, *descriptionArgs!!)
        this.description = string
    }

    abstract override fun updateDescriptionArgs()


    private sealed class IconSize {
        data object Big : IconSize()
        data object Small : IconSize()
        companion object {
            fun values(): Array<IconSize> {
                return arrayOf(Big, Small)
            }

            fun valueOf(value: String): IconSize {
                return when (value) {
                    "Big" -> Big
                    "Small" -> Small
                    else -> throw IllegalArgumentException("No object superstitioapi.powers.SuperstitioApiPower.IconSize.$value")
                }
            }
        }
    }

    companion object {
        const val DEFAULT: String = "default"
        fun getPowerStrings(powerID: String?): PowerStrings {
            return CardCrawlGame.languagePack.getPowerStrings(powerID)
        }
    }
}
