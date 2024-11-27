package superstitioapi.relicToBlight.customSave

import com.evacipated.cardcrawl.modthespire.lib.*
import com.evacipated.cardcrawl.modthespire.lib.Matcher.MethodCallMatcher
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue
import com.megacrit.cardcrawl.saveAndContinue.SaveFile
import javassist.CtBehavior

@SpirePatch2(clz = SaveAndContinue::class, method = "save", paramtypez = [SaveFile::class])
object SaveAndContinuePatch {
    @SpireInsertPatch(locator = Locator::class, localvars = ["params"])
    @JvmStatic
    fun Insert(save: SaveFile?, params: HashMap<String?, Any?>) {
        params["superstitioapimod:mod_blight"] = SuperstitioApiModSaves.modBlightSaves.get(save)
    }

    private class Locator : SpireInsertLocator() {
        @Throws(Exception::class)
        override fun Locate(ctMethodToPatch: CtBehavior): IntArray {
            val finalMatcher: Matcher = MethodCallMatcher("com.google.gson.GsonBuilder", "create")
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher)
        }
    }
}
