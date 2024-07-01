package superstitio;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Properties;

public class SuperstitioConfig {
    private static final String ENABLE_SFW_STRING = "enableSFW";
    private static final String ENABLE_GURO_CHARACTER_STRING = "enableGuroCharacter";
    private static final String ENABLE_PERFORMANCE_MODE = "enablePerformanceMode";
    private static final String ENABLE_ONLY_SHOW_CARD_NOT_GENERAL = "enableOnlyShowCardNotGeneral";
    private static final ModPanel settingsPanel = new ModPanel();
    public static SpireConfig config = null;
    public static Properties theDefaultDefaultSettings = new Properties();
    private static boolean enableSFW = true;
    private static boolean enableGuroCharacter = false;
    private static boolean enablePerformanceMode = false;
    private static boolean enableOnlyShowCardNotGeneral = false;
    ;

    public static void loadConfig() {
        theDefaultDefaultSettings.setProperty(ENABLE_SFW_STRING, "TRUE");
        theDefaultDefaultSettings.setProperty(ENABLE_GURO_CHARACTER_STRING, "FALSE");
        theDefaultDefaultSettings.setProperty(ENABLE_PERFORMANCE_MODE, "FALSE");
        theDefaultDefaultSettings.setProperty(ENABLE_ONLY_SHOW_CARD_NOT_GENERAL, "FALSE");
        try {
            config = new SpireConfig(DataManager.getModID(), DataManager.getModID() + "Config", theDefaultDefaultSettings);
            config.load();
            enableSFW = config.getBool(ENABLE_SFW_STRING);
            enableGuroCharacter = config.getBool(ENABLE_GURO_CHARACTER_STRING);
            enablePerformanceMode = config.getBool(ENABLE_PERFORMANCE_MODE);
            enableOnlyShowCardNotGeneral = config.getBool(ENABLE_ONLY_SHOW_CARD_NOT_GENERAL);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public static void setUpModOptions() {
        Logger.run("Loading badge image and mod options");
        final Texture badgeTexture = ImageMaster.loadImage(DataManager.makeImgFilesPath_UI("ModIcon"));
        setUpPanel();

        BaseMod.registerModBadge(badgeTexture, SuperstitioModSetup.MOD_NAME + " Mod", "Creeper_of_Fire", "A NSFW mod", settingsPanel);
    }

    private static void setUpPanel() {
        float settingXPos = 350.0f;
        float settingYPos = 750.0f;
        final float lineSpacing = 50.0f;
        final UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(DataManager.MakeTextID("OptionsMenu"));
        final String[] SettingText = UIStrings.TEXT;

        settingsPanel.addUIElement(new ModLabeledToggleButton(SettingText[0], settingXPos, settingYPos, Settings.CREAM_COLOR,
                FontHelper.charDescFont, enableSFW, settingsPanel, label -> {
        }, button -> {
            enableSFW = button.enabled;
            setConfigBool(ENABLE_SFW_STRING, enableSFW);

//            ArrayList<IUIElement> uiElementsRender = ReflectionHacks.getPrivate(settingsPanel, ModPanel.class, "uiElementsRender");
//            ArrayList<IUIElement> uiElementsUpdate = ReflectionHacks.getPrivate(settingsPanel, ModPanel.class, "uiElementsUpdate");
//            uiElementsRender.clear();
//            uiElementsUpdate.clear();
//            setUpPanel();
        }));
        settingYPos -= 4 * lineSpacing;
            settingsPanel.addUIElement(new ModLabeledToggleButton(SettingText[1], settingXPos, settingYPos, Settings.CREAM_COLOR,
                    FontHelper.charDescFont, enableGuroCharacter, settingsPanel, label -> {
            }, button -> {
                enableGuroCharacter = button.enabled;
                setEnableGuroCharacter(enableGuroCharacter);
            }) {
                @Override
                public void render(SpriteBatch sb) {
                    if (isEnableSFW())return;
                    super.render(sb);
                }

                @Override
                public void update() {
                    if (isEnableSFW())return;
                    super.update();
                }
            });

        settingYPos -= 1 * lineSpacing;

        settingsPanel.addUIElement(new ModLabeledToggleButton(SettingText[2], settingXPos, settingYPos, Settings.CREAM_COLOR,
                FontHelper.charDescFont, enableOnlyShowCardNotGeneral, settingsPanel, label -> {
        }, button -> {
            enableOnlyShowCardNotGeneral = button.enabled;
            setConfigBool(ENABLE_ONLY_SHOW_CARD_NOT_GENERAL, enableOnlyShowCardNotGeneral);
        }));

        settingYPos -= 3 * lineSpacing;

        settingsPanel.addUIElement(new ModLabeledToggleButton(SettingText[3], settingXPos, settingYPos, Settings.CREAM_COLOR,
                FontHelper.charDescFont, enablePerformanceMode, settingsPanel, label -> {
        }, button -> {
            enablePerformanceMode = button.enabled;
            setConfigBool(ENABLE_PERFORMANCE_MODE, enablePerformanceMode);
        }));
    }

    private static void setConfigBool(String enablePerformanceMode, boolean enablePerformanceMode1) {
        try {
            config.setBool(enablePerformanceMode, enablePerformanceMode1);
            config.save();
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public static boolean isEnableSFW() {
        return enableSFW;
    }

    public static boolean isEnableGuroCharacter() {
        return enableSFW || enableGuroCharacter;
    }

    public static void setEnableGuroCharacter(boolean value) {
        enableGuroCharacter = value;
        setConfigBool(ENABLE_GURO_CHARACTER_STRING, value);
    }

    public static boolean isEnablePerformanceMode() {
        return enablePerformanceMode;
    }

    public static boolean isEnableOnlyShowCardNotGeneral() {
        return enableOnlyShowCardNotGeneral;
    }
}
