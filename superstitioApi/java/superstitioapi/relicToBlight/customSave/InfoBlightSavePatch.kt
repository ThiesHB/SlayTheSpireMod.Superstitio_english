package superstitioapi.relicToBlight.customSave

import basemod.abstracts.CustomSavableRaw
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitioapi.relicToBlight.BlightWithRelic

@SpirePatch2(clz = CardCrawlGame::class, method = "loadPlayerSave")
object InfoBlightSavePatch {
    @SpirePostfixPatch
    @JvmStatic
    fun Postfix(__instance: CardCrawlGame?, p: AbstractPlayer?) {
        val blighSave =
            SuperstitioApiModSaves.modBlightSaves.get(CardCrawlGame.saveFile)
        val blights = AbstractDungeon.player.blights
        for (i in blights.indices) {
            val blight = blights[i] as? BlightWithRelic ?: continue
            val relic = blight.relic
            if (relic !is CustomSavableRaw) continue
            if (blighSave != null && i < blighSave.size) (relic as CustomSavableRaw).onLoadRaw(blighSave[i])
            else (relic as CustomSavableRaw).onLoadRaw(null)
        }
    }
}


