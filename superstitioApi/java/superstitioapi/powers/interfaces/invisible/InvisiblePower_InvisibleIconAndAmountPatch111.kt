package superstitioapi.powers.interfaces.invisible

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.core.AbstractCreature
import javassist.CannotCompileException
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

object InvisiblePower_InvisibleIconAndAmountPatch {
    @SpirePatch2(clz = AbstractCreature::class, method = "renderPowerIcons")
    object RenderPowerIcons {
        @SpireInstrumentPatch
        @JvmStatic
        fun Instrument(): ExprEditor {
            return object : ExprEditor() {
                @Throws(CannotCompileException::class)
                override fun edit(m: MethodCall) {
                    if (m.methodName == "renderIcons") {
                        m.replace(
                            "if (" + InvisiblePower_InvisibleIconAndAmount.Companion::class.qualifiedName + ".shouldInvisibleIcon(p)) " +
                                    "{offset -= " +
                                    "POWER_ICON_PADDING_X;} else {\$proceed($$);}"
                        )
                    } else if (m.methodName == "renderAmount") {
                        m.replace(
                            "if (" + InvisiblePower_InvisibleIconAndAmount.Companion::class.qualifiedName + ".shouldInvisibleAmount(p)) " +
                                    "{offset -= " +
                                    "POWER_ICON_PADDING_X;\$proceed($$);} else {\$proceed($$);}"
                        )
                    }
                }
            }
        }
    }
}
