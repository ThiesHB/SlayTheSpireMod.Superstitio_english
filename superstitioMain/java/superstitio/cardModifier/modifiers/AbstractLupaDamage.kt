package superstitio.cardModifier.modifiers

import basemod.helpers.TooltipInfo
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import superstitio.DataManager
import superstitio.customStrings.interFace.StringSetUtility
import superstitio.customStrings.stringsSet.ModifierStringsSet

abstract class AbstractLupaDamage(id: String) : AbstractDamageModifier() {
    val damageStrings: ModifierStringsSet = getDamageStringsWithSFW(id)

    var tooltip: TooltipInfo? = null

    init {
        this.automaticBindingForCards = true
    }

    fun removeAutoBind(): AbstractLupaDamage {
        this.automaticBindingForCards = false
        return this
    }

    override fun getCustomTooltips(): ArrayList<TooltipInfo>? {
        if (tooltip == null) {
            tooltip = TooltipInfo(damageStrings.getBasicInfo(), damageStrings.getDESCRIPTION())
        }
        val tooltipInfos = ArrayList<TooltipInfo>()
        tooltipInfos.add(tooltip!!)
        return tooltipInfos
    }

    override fun getCardDescriptor(): String {
        return damageStrings.getNAME()
    }

    override fun isInherent(): Boolean {
        return true
    }

    companion object {
        fun getDamageStringsWithSFW(cardName: String): ModifierStringsSet {
            return StringSetUtility.getCustomStringsWithSFW(
                cardName,
                DataManager.modifiers,
                ModifierStringsSet::class.java
            )
        }
    }
}