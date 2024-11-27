package superstitioapi.relicToBlight.customSave

import basemod.abstracts.CustomSavableRaw
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.saveAndContinue.SaveFile
import com.megacrit.cardcrawl.saveAndContinue.SaveFile.SaveType
import superstitioapi.relicToBlight.BlightWithRelic

@SpirePatch2(clz = SaveFile::class, method = "<ctor>", paramtypez = [SaveType::class])
object InfoBlightConstructSaveFilePatch
{
    @SpirePostfixPatch
    @JvmStatic
    fun Prefix(__instance: SaveFile?, type: SaveType?)
    {
        val blightSaves = SuperstitioApiModSaves.ArrayListOfJsonElement()
        for (blight in AbstractDungeon.player.blights)
        {
            if (blight is BlightWithRelic && blight.relic is CustomSavableRaw)
                blightSaves.add((blight.relic as CustomSavableRaw).onSaveRaw())
            else
            {
                blightSaves.add(null)
            }
        }
        SuperstitioApiModSaves.modBlightSaves.set(__instance, blightSaves)
    }
}
