package superstitioapi.relicToBlight.blightHook

import com.evacipated.cardcrawl.modthespire.lib.*
import com.evacipated.cardcrawl.modthespire.lib.Matcher.FieldAccessMatcher
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import javassist.CtBehavior

interface BlightForCard {
    fun onExhaust(card: AbstractCard?) {
    }

    fun onCardDraw(card: AbstractCard?) {
    }

    companion object{
        @SpirePatch2(clz = CardGroup::class, method = "moveToExhaustPile", paramtypez = [AbstractCard::class])
        object OnExhaustPatch {
            @SpirePrefixPatch
        @JvmStatic
            fun Prefix(__instance: CardGroup?, c: AbstractCard?) {
                for (b in AbstractDungeon.player.blights) {
                    if (b is BlightForCard) (b as BlightForCard).onExhaust(c)
                }
            }
        }

        @SpirePatch2(clz = AbstractPlayer::class, method = "draw", paramtypez = [Int::class])
        object DrawCardPatch {
            @SpireInsertPatch(locator = DrawCardLocator::class, localvars = ["c"])
            @JvmStatic
            fun Insert(__instance: AbstractPlayer, c: AbstractCard?) {
                for (b in __instance.blights) {
                    if (b is BlightForCard) (b as BlightForCard).onCardDraw(c)
                }
            }

            private class DrawCardLocator : SpireInsertLocator() {
                @Throws(Exception::class)
                override fun Locate(ctMethodToPatch: CtBehavior): IntArray {
                    val finalMatcher: Matcher = FieldAccessMatcher(AbstractPlayer::class.java, "relics")
                    return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)
                }
            }
        }
    }
}
