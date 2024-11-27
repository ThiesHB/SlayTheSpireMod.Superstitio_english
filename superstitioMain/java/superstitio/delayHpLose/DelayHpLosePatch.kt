package superstitio.delayHpLose

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import javassist.CannotCompileException
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess
import superstitioapi.utils.ActionUtility

@SpirePatch2(clz = AbstractPlayer::class, method = "damage")
object DelayHpLosePatch
{
    @SpireInstrumentPatch
    @JvmStatic
    fun Instrument(): ExprEditor
    {
        return object : ExprEditor()
        {
            private var count = 0

            @Throws(CannotCompileException::class)
            override fun edit(m: FieldAccess)
            {
                if (m.fieldName != "currentHealth" || !m.isWriter) return
                if (this.count <= 0)
                {
                    m.replace(
                        "if( " + DelayHpLosePatch::class.qualifiedName + ".isImmunity( $0 , info,damageAmount )  )" +
                                "{$0.currentHealth = " + DelayHpLosePatch::class.qualifiedName + ".ForceSetCurrentHealth( $0 , info ) ;}" +
                                "else { \$proceed($$) ;}"
                    )
                }

                ++this.count
            }
        }
    }

    @JvmStatic
    fun ForceSetCurrentHealth(abstractPlayer: AbstractPlayer, info: DamageInfo?): Int
    {
        return abstractPlayer.currentHealth
    }

    @JvmStatic
    fun isImmunity(abstractPlayer: AbstractPlayer, info: DamageInfo, damageAmount: Int): Boolean
    {
        if (ActionUtility.isNotInBattle) return false
        val predicate =
            IsImmunityFields.checkShouldImmunity[abstractPlayer] ?: return false
        return predicate.test(abstractPlayer, info, damageAmount)
    }


}

