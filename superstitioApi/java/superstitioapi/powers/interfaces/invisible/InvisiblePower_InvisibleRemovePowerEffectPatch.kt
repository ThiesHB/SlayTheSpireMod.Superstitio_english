package superstitioapi.powers.interfaces.invisible

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import javassist.CannotCompileException
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

object InvisiblePower_InvisibleRemovePowerEffectPatch
{
    @SpirePatch2(clz = RemoveSpecificPowerAction::class, method = "update")
    object HideExpireText
    {
        @SpireInstrumentPatch
        @JvmStatic
        fun Instrument(): ExprEditor
        {
            return object : ExprEditor()
            {
                @Throws(CannotCompileException::class)
                override fun edit(m: MethodCall)
                {
                    if (m.className == ArrayList::class.qualifiedName && m.methodName == "add")
                    {
                        m.replace(
                            "if (!(" + InvisiblePower_InvisibleRemovePowerEffect.Companion::class.qualifiedName +
                                    ".shouldInvisibleRemovePowerEffect(removeMe))) {\$_ = \$proceed($$);}"
                        )
                    }
                }
            }
        }
    }
}
