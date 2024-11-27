package superstitioapi.utils

import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.helpers.TipHelper
import com.megacrit.cardcrawl.helpers.input.InputHelper

object TipsUtility {
    fun renderTipsWithMouse(tips: MutableList<PowerTip>) {
        if (InputHelper.mX.toFloat() < 1400.0f * Settings.scale) {
            TipHelper.queuePowerTips(
                InputHelper.mX.toFloat() + 50.0f * Settings.scale,
                InputHelper.mY.toFloat() - 50.0f * Settings.scale, tips.toCollection(ArrayList<PowerTip>())
            )
        } else {
            TipHelper.queuePowerTips(
                InputHelper.mX.toFloat() - 350.0f * Settings.scale,
                InputHelper.mY.toFloat() - 50.0f * Settings.scale, tips.toCollection(ArrayList<PowerTip>())
            )
        }
    }

    fun renderTipsWithMouse(tip: PowerTip) {
        val singleTip = ArrayList<PowerTip>()
        singleTip.add(tip)
        renderTipsWithMouse(singleTip)
    }
}
