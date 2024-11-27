package superstitioapi.relicToBlight.blightHook

import com.evacipated.cardcrawl.modthespire.lib.*
import com.evacipated.cardcrawl.modthespire.lib.Matcher.FieldAccessMatcher
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.saveAndContinue.SaveFile
import javassist.CtBehavior
import superstitioapi.utils.InOrderIndexMultiFinder

interface BlightOnEnterRoom {
    fun onEnterRoom(room: AbstractRoom?) {
    }

    fun justEnteredRoom(room: AbstractRoom?) {
    }

    companion object{
        @SpirePatch2(clz = AbstractDungeon::class, method = "nextRoomTransition", paramtypez = [SaveFile::class])
        object OnEnterRoomPatch {
            @SpireInsertPatch(locator = OnEnterRoomLocator::class)
            @JvmStatic
            fun OnEnterRoomInsert(__instance: AbstractDungeon?, saveFile: SaveFile?) {
                val isLoadingPostCombatSave = CardCrawlGame.loadingSave && saveFile != null && saveFile.post_combat
                if (AbstractDungeon.nextRoom != null && !isLoadingPostCombatSave) {
                    for (blight in AbstractDungeon.player.blights) {
                        if (blight is BlightOnEnterRoom) (blight as BlightOnEnterRoom).onEnterRoom(AbstractDungeon.nextRoom.room)
                    }
                }
            }

            @SpireInsertPatch(locator = OnJustEnterRoomLocator::class)
            @JvmStatic
            fun OnJustEnterRoomInsert(__instance: AbstractDungeon?, saveFile: SaveFile?) {
                val isLoadingPostCombatSave = CardCrawlGame.loadingSave && saveFile != null && saveFile.post_combat
                if (AbstractDungeon.getCurrRoom() != null && !isLoadingPostCombatSave) {
                    for (blight in AbstractDungeon.player.blights) {
                        if (blight is BlightOnEnterRoom) (blight as BlightOnEnterRoom).justEnteredRoom(AbstractDungeon.getCurrRoom())
                    }
                }
            }

            private class OnEnterRoomLocator : SpireInsertLocator() {
                @Throws(Exception::class)
                override fun Locate(ctMethodToPatch: CtBehavior): IntArray {
                    val finalMatcher: Matcher = FieldAccessMatcher(AbstractPlayer::class.java, "relics")
                    return InOrderIndexMultiFinder.findInOrder(ctMethodToPatch, finalMatcher, intArrayOf(0))
                }
            }

            private class OnJustEnterRoomLocator : SpireInsertLocator() {
                @Throws(Exception::class)
                override fun Locate(ctMethodToPatch: CtBehavior): IntArray {
                    val finalMatcher: Matcher = FieldAccessMatcher(AbstractPlayer::class.java, "relics")
                    return InOrderIndexMultiFinder.findInOrder(ctMethodToPatch, finalMatcher, intArrayOf(1))
                }
            }
        }
    }
}
