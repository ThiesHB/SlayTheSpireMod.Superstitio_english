//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package superstitioapi.utils

import com.evacipated.cardcrawl.modthespire.finders.MatchFinderExprEditor
import com.evacipated.cardcrawl.modthespire.lib.Matcher
import com.evacipated.cardcrawl.modthespire.patcher.Expectation
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException
import javassist.CannotCompileException
import javassist.CtBehavior
import javassist.expr.Expr
import java.util.*
import java.util.stream.Collectors

class InOrderIndexMultiFinder(
    private val expectedMatches: List<Matcher>,
    private val finalMatch: Matcher,
    indexes: IntArray?
) : MatchFinderExprEditor()
{
    protected val indexes: List<Int> = Arrays.stream(indexes).boxed().collect(Collectors.toList())
    private val locations: MutableList<Int> = ArrayList()
    private var foundLocation = false
    private var foundMatchesIndex = 0

    private fun foundFinalMatch(lineNumber: Int)
    {
        this.foundLocation = true
        locations.add(lineNumber)
    }

    private fun finalMatch(): Boolean
    {
        return this.foundMatchesIndex >= expectedMatches.size
    }

    private fun foundMatch()
    {
        ++this.foundMatchesIndex
    }

    private fun currentMatch(): Matcher
    {
        return expectedMatches[foundMatchesIndex]
    }

    override fun getFoundLocations(): IntArray
    {
        val asArray = IntArray(locations.size)

        val returnArray = ArrayList<Int>()

        for (i in asArray.indices)
        {
            if (indexes.contains(i)) returnArray.add(locations[i])
        }

        return returnArray.stream().mapToInt { integer: Int -> integer }.toArray()
    }

    override fun didFindLocation(): Boolean
    {
        return this.foundLocation
    }

    override fun doMatch(expectedType: Expectation, toMatch: Expr)
    {
        if (this.finalMatch())
        {
            if (finalMatch.expectation == expectedType && finalMatch.match(toMatch))
            {
                this.foundFinalMatch(toMatch.lineNumber)
            }
        }
        else
        {
            val current = this.currentMatch()
            if (current.expectation == expectedType && current.match(toMatch))
            {
                this.foundMatch()
            }
        }
    }

    companion object
    {
        @Throws(CannotCompileException::class, PatchingException::class)
        fun findInOrder(ctMethodToPatch: CtBehavior, finalMatch: Matcher, indexes: IntArray?): IntArray
        {
            return findInOrder(ctMethodToPatch, ArrayList(), finalMatch, indexes)
        }

        @Throws(CannotCompileException::class, PatchingException::class)
        fun findInOrder(
            ctMethodToPatch: CtBehavior,
            expectedMatches: List<Matcher>,
            finalMatch: Matcher,
            indexes: IntArray?
        ): IntArray
        {
            val editor: MatchFinderExprEditor = InOrderIndexMultiFinder(expectedMatches, finalMatch, indexes)
            ctMethodToPatch.instrument(editor)
            if (!editor.didFindLocation())
            {
                throw PatchingException(
                    ctMethodToPatch,
                    "Location matching given description could not be found for patch"
                )
            }
            else
            {
                return editor.foundLocations
            }
        }
    }
}
