package superstitioapi.relicToBlight.blightHook

import basemod.ReflectionHacks
import com.evacipated.cardcrawl.modthespire.lib.*
import com.evacipated.cardcrawl.modthespire.lib.Matcher.FieldAccessMatcher
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.rooms.CampfireUI
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption
import javassist.CtBehavior
import superstitioapi.utils.InOrderIndexMultiFinder

interface BlightCampfire {
    fun addCampfireOption(options: ArrayList<AbstractCampfireOption>) {
    }

    fun canUseCampfireOption(option: AbstractCampfireOption): Boolean {
        return true
    }


  companion object{
      @SpirePatch2(clz = CampfireUI::class, method = "initializeButtons")
      object AddCampFireOptPath {
          @SpireInsertPatch(locator = AddLocator::class)
          @JvmStatic
          fun AddButtonsInsert(__instance: CampfireUI?) {
              val buttons = ReflectionHacks.getPrivate<ArrayList<AbstractCampfireOption>>(
                  __instance,
                  CampfireUI::class.java,
                  "buttons"
              )
              for (blight in AbstractDungeon.player.blights) {
                  if (blight is BlightCampfire) (blight as BlightCampfire).addCampfireOption(buttons)
              }
          }

          @SpireInsertPatch(locator = CanUseLocator::class)
          @JvmStatic
          fun CanUseButtonsInsert(__instance: CampfireUI?) {
              val buttons = ReflectionHacks.getPrivate<ArrayList<AbstractCampfireOption>>(
                  __instance,
                  CampfireUI::class.java,
                  "buttons"
              )
              for (co in buttons) {
                  for (blight in AbstractDungeon.player.blights) {
                      if (blight is BlightCampfire) if (!(blight as BlightCampfire).canUseCampfireOption(co)) {
                          co.usable = false
                          break
                      }
                  }
              }
          }

          private class AddLocator : SpireInsertLocator() {
              @Throws(Exception::class)
              override fun Locate(ctMethodToPatch: CtBehavior): IntArray {
                  val finalMatcher: Matcher = FieldAccessMatcher(AbstractPlayer::class.java, "relics")
                  return LineFinder.findInOrder(ctMethodToPatch, finalMatcher)
              }
          }

          private class CanUseLocator : SpireInsertLocator() {
              @Throws(Exception::class)
              override fun Locate(ctMethodToPatch: CtBehavior): IntArray {
                  val finalMatcher: Matcher = FieldAccessMatcher(CampfireUI::class.java, "buttons")
                  return InOrderIndexMultiFinder.findInOrder(ctMethodToPatch, finalMatcher, intArrayOf(3))
              }
          }
      }
  }
}
