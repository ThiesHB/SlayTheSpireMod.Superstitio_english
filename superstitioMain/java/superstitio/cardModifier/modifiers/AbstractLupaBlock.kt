package superstitio.cardModifier.modifiers

import basemod.helpers.TooltipInfo
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import superstitio.DataManager
import superstitio.customStrings.interFace.StringSetUtility
import superstitio.customStrings.stringsSet.ModifierStringsSet
import superstitioapi.utils.UpdateDescriptionAdvanced
import superstitioapi.utils.getFormattedDescription

abstract class AbstractLupaBlock(id: String) : AbstractBlockModifier(), UpdateDescriptionAdvanced
{
    val blockStrings: ModifierStringsSet?
    var tooltip: TooltipInfo? = null

    init
    {
        this.blockStrings = getBlockStringsWithSFW(id)
        this.automaticBindingForCards = true
    }

    fun removeAutoBind(): AbstractLupaBlock
    {
        this.automaticBindingForCards = false
        return this
    }

    override fun getName(): String
    {
        return blockStrings!!.getNAME()
    }

    override var descriptionArgs: Array<out Any>? = null

    /**
     * 卡牌的描述
     */
    override fun getCustomTooltips(): ArrayList<TooltipInfo>?
    {
        if (tooltip == null)
        {
            tooltip = TooltipInfo(blockStrings!!.getBasicInfo(), description)
        }
        val tooltipInfos = ArrayList<TooltipInfo>()
        tooltipInfos.add(tooltip!!)
        return tooltipInfos
    }

    /**
     * 鼠标放到格挡上的描述
     */
    override fun getDescription(): String?
    {
        return getFormattedDescription()
    }

    override fun updateDescriptionArgs()
    {
    }

    override fun getDescriptionStrings(): String
    {
        return blockStrings!!.getDESCRIPTION()
    }


    /**
     * 显示在卡牌的类型旁边的那玩意
     */
    override fun getCardDescriptor(): String
    {
        return blockStrings!!.getNAME()
    }

    override fun isInherent(): Boolean
    {
        return true
    }

    companion object
    {
        fun getBlockStringsWithSFW(cardName: String): ModifierStringsSet
        {
            return StringSetUtility.getCustomStringsWithSFW(
                cardName,
                DataManager.modifiers,
                ModifierStringsSet::class.java
            )
        }
    }
}
