package superstitio

import basemod.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.ImageMaster
import java.util.*
import java.util.function.Consumer

object SuperstitioConfig {
    private const val ENABLE_SFW_STRING = "enableSFW"
    private const val ENABLE_GURO_CHARACTER_STRING = "enableGuroCharacter"
    private const val ENABLE_PERFORMANCE_MODE = "enablePerformanceMode"
    private const val ENABLE_ONLY_SHOW_CARD_NOT_GENERAL = "enableOnlyShowCardNotGeneral"
    private const val isForcePlayGuroCharacter_STRING = "isForcePlayGuroCharacter"
    private val settingsPanel = ModPanel()
    var config: SpireConfig? = null
    var theDefaultSettings: Properties = Properties()
    private var enableSFW = true
    private var enableGuroCharacter = false
    var isEnablePerformanceMode: Boolean = false
        private set
    var isEnableOnlyShowCardNotGeneral: Boolean = false
        private set
    var isForcePlayGuroCharacter: Boolean = false
        private set

    @JvmStatic
    fun loadConfig() {
        theDefaultSettings.setProperty(ENABLE_SFW_STRING, "TRUE")
        theDefaultSettings.setProperty(ENABLE_GURO_CHARACTER_STRING, "FALSE")
        theDefaultSettings.setProperty(ENABLE_PERFORMANCE_MODE, "FALSE")
        theDefaultSettings.setProperty(ENABLE_ONLY_SHOW_CARD_NOT_GENERAL, "FALSE")
        theDefaultSettings.setProperty(isForcePlayGuroCharacter_STRING, "FALSE")
        try {
            config = SpireConfig(
                DataManager.getModID(),
                DataManager.getModID() + "Config",
                theDefaultSettings
            )
            config!!.load()
            enableSFW = config!!.getBool(ENABLE_SFW_STRING)
            enableGuroCharacter = config!!.getBool(ENABLE_GURO_CHARACTER_STRING)
            isEnablePerformanceMode = config!!.getBool(ENABLE_PERFORMANCE_MODE)
            isEnableOnlyShowCardNotGeneral = config!!.getBool(ENABLE_ONLY_SHOW_CARD_NOT_GENERAL)
            isForcePlayGuroCharacter = config!!.getBool(isForcePlayGuroCharacter_STRING)
        } catch (e: Exception) {
            Logger.error(e)
        }
    }
    @JvmStatic
    fun setUpModOptions() {
        Logger.run("Loading badge image and mod options")
        val badgeTexture = ImageMaster.loadImage(DataManager.makeImgFilesPath_UI("ModIcon"))
        setUpPanel()

        BaseMod.registerModBadge(
            badgeTexture,
            SuperstitioModSetup.MOD_NAME + " Mod",
            "Creeper_of_Fire",
            "A NSFW mod",
            settingsPanel
        )
    }
    @JvmStatic
    fun isEnableSFW(): Boolean {
        if (SuperstitioModSetup.SEAL_MANUAL_SFW) return true
        return enableSFW
    }

    fun isEnableGuroCharacter(): Boolean {
        return enableSFW || enableGuroCharacter
    }

    fun setEnableGuroCharacter(value: Boolean) {
        enableGuroCharacter = value
        setConfigBool(ENABLE_GURO_CHARACTER_STRING, value)
    }

    private fun setUpPanel() {
        val settingXPos = 350.0f
        var settingYPos = 750.0f
        val lineSpacing = 50.0f
        val UIStrings = CardCrawlGame.languagePack.getUIString(DataManager.MakeTextID("OptionsMenu"))
        val SettingText = UIStrings.TEXT

        settingsPanel.addUIElement(ModLabeledToggleButton(SettingText[0],
            settingXPos,
            settingYPos,
            Settings.CREAM_COLOR,
            FontHelper.charDescFont,
            enableSFW,
            settingsPanel,
            { label: ModLabel? -> },
            { button: ModToggleButton ->
                enableSFW = button.enabled
                setConfigBool(ENABLE_SFW_STRING, enableSFW)
            })
        )

        settingYPos -= 5 * lineSpacing

        settingsPanel.addUIElement(object : ModLabeledToggleButton(SettingText[1],
            settingXPos,
            settingYPos,
            Settings.CREAM_COLOR,
            FontHelper.charDescFont,
            enableGuroCharacter,
            settingsPanel,
            Consumer { label: ModLabel? -> },
            Consumer { button: ModToggleButton ->
                enableGuroCharacter = button.enabled
                setEnableGuroCharacter(enableGuroCharacter)
            }) {
            override fun render(sb: SpriteBatch?) {
                if (isEnableSFW()) return
                super.render(sb)
            }

            override fun update() {
                if (isEnableSFW()) return
                super.update()
            }
        })

        settingYPos -= 2 * lineSpacing

        settingsPanel.addUIElement(object : ModLabeledToggleButton(SettingText[2],
            settingXPos,
            settingYPos,
            Settings.CREAM_COLOR,
            FontHelper.charDescFont,
            isForcePlayGuroCharacter,
            settingsPanel,
            Consumer { label: ModLabel? -> },
            Consumer { button: ModToggleButton ->
                isForcePlayGuroCharacter = button.enabled
                setConfigBool(isForcePlayGuroCharacter_STRING, isForcePlayGuroCharacter)
            }) {
            override fun render(sb: SpriteBatch?) {
                if (isEnableSFW() || isEnableGuroCharacter()) return
                super.render(sb)
            }

            override fun update() {
                if (isEnableSFW() || isEnableGuroCharacter()) return
                super.update()
            }
        })

        settingYPos -= 1 * lineSpacing

        settingsPanel.addUIElement(
            ModLabeledToggleButton(SettingText[3],
                settingXPos,
                settingYPos,
                Settings.CREAM_COLOR,
                FontHelper.charDescFont,
                isEnableOnlyShowCardNotGeneral,
                settingsPanel,
                { label: ModLabel? -> },
                { button: ModToggleButton ->
                    isEnableOnlyShowCardNotGeneral = button.enabled
                    setConfigBool(ENABLE_ONLY_SHOW_CARD_NOT_GENERAL, isEnableOnlyShowCardNotGeneral)
                })
        )

        settingYPos -= 3 * lineSpacing

        settingsPanel.addUIElement(
            ModLabeledToggleButton(SettingText[4],
                settingXPos,
                settingYPos,
                Settings.CREAM_COLOR,
                FontHelper.charDescFont,
                isEnablePerformanceMode,
                settingsPanel,
                { label: ModLabel? -> },
                { button: ModToggleButton ->
                    isEnablePerformanceMode = button.enabled
                    setConfigBool(ENABLE_PERFORMANCE_MODE, isEnablePerformanceMode)
                })
        )
    }

    private fun setConfigBool(enablePerformanceMode: String, enablePerformanceMode1: Boolean) {
        try {
            config!!.setBool(enablePerformanceMode, enablePerformanceMode1)
            config!!.save()
        } catch (e: Exception) {
            Logger.error(e)
        }
    }
}
