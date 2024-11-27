package superstitioapi.relicToBlight.customSave

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.megacrit.cardcrawl.saveAndContinue.SaveFile

@SpirePatch2(clz = SaveFile::class, method = "<class>")
object SuperstitioApiModSaves
{
    @SerializedName("superstitioapimod:mod_blight")
    @JvmField
    var modBlightSaves: SpireField<ArrayListOfJsonElement?> = SpireField<ArrayListOfJsonElement?> { null }

    class ArrayListOfJsonElement : ArrayList<JsonElement?>()

    class ArrayListOfString : ArrayList<String?>()

    class HashMapOfJsonElement : HashMap<String?, JsonElement?>()
}
