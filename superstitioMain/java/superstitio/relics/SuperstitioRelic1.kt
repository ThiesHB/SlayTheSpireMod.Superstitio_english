package superstitio.relics

import basemod.abstracts.CustomRelic
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.ImageMaster
import superstitio.DataManager
import superstitio.characters.BaseCharacter
import superstitioapi.utils.UpdateDescriptionAdvanced
import superstitioapi.utils.getFormattedDescription
import java.util.function.BiFunction

abstract class SuperstitioRelic(id: String, relicTier: RelicTier?, landingSound: LandingSound?) : CustomRelic(
    id, ImageMaster.loadImage(
        makeImgPath(id)
    ), ImageMaster.loadImage(makeImgPathOutLine(id)), relicTier, landingSound
), UpdateDescriptionAdvanced {

    init {
        this.largeImg = ImageMaster.loadImage(makeImgPathLarge(id))
    }

    override fun setCounter(counter: Int) {
        super.counter = counter
    }

    fun getCounter():Int{
        return super.counter
    }

    fun updateDescription() {
        this.description = getFormattedDescription()
    }

    override fun canSpawn(): Boolean {
        return AbstractDungeon.player is BaseCharacter
    }

    override fun getUpdatedDescription(): String {
        return this.getFormattedDescription()
    }

    abstract override fun updateDescriptionArgs()

    override fun getDescriptionStrings(): String {
        return DESCRIPTIONS[0]
    }

    override var descriptionArgs: Array<out Any>? = null

    companion object {
        const val DEFAULT_RELIC: String = "default_relic"
        private fun makeImgPath(id: String): String {
            return DataManager.makeImgPath(
                DEFAULT_RELIC,
                BiFunction<String, Array<String>, String>(DataManager::makeImgFilesPath_Relic),
                id
            )
        }

        private fun makeImgPathOutLine(id: String): String {
            return DataManager.makeImgPath(
                DEFAULT_RELIC,
                BiFunction<String, Array<String>, String>(DataManager::makeImgFilesPath_RelicOutline),
                id
            )
        }

        private fun makeImgPathLarge(id: String): String {
            return DataManager.makeImgPath(
                DEFAULT_RELIC,
                BiFunction<String, Array<String>, String>(DataManager::makeImgFilesPath_RelicLarge),
                id
            )
        }
    }
}