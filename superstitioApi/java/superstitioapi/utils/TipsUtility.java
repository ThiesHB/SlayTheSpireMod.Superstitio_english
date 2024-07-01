package superstitioapi.utils;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.ArrayList;

public class TipsUtility {
    public static void renderTipsWithMouse(ArrayList<PowerTip> tips) {
        if ((float) InputHelper.mX < 1400.0F * Settings.scale) {
            TipHelper.queuePowerTips((float) InputHelper.mX + 50.0F * Settings.scale,
                    (float) InputHelper.mY - 50.0F * Settings.scale, tips);
        } else {
            TipHelper.queuePowerTips((float) InputHelper.mX - 350.0F * Settings.scale,
                    (float) InputHelper.mY - 50.0F * Settings.scale, tips);
        }
    }

    public static void renderTipsWithMouse(PowerTip tip) {
        ArrayList<PowerTip> singleTip = new ArrayList<>();
        singleTip.add(tip);
        if ((float) InputHelper.mX < 1400.0F * Settings.scale) {
            TipHelper.queuePowerTips((float) InputHelper.mX + 50.0F * Settings.scale,
                    (float) InputHelper.mY - 50.0F * Settings.scale, singleTip);
        } else {
            TipHelper.queuePowerTips((float) InputHelper.mX - 350.0F * Settings.scale,
                    (float) InputHelper.mY - 50.0F * Settings.scale, singleTip);
        }
    }
}
