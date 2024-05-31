package superstitioapi;

import basemod.BaseMod;
import basemod.interfaces.EditStringsSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.PowerStrings;

@SpireInitializer
public class SuperstitioApiSetup implements EditStringsSubscriber {

    public static final String MOD_NAME = "SuperstitioApi";

    public SuperstitioApiSetup() {
        // 这里注册颜色
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new SuperstitioApiSetup();
    }


    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(PowerStrings.class, DataUtility.makeLocalizationPath(Settings.language, "power"));
    }
}
