package superstitioapi.powers.interfaces.invisible

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import javassist.CannotCompileException
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

object InvisiblePower_InvisibleTipsPatch {
    private val exprEditor: ExprEditor
        get() = object : ExprEditor() {
            private var count = 0

            @Throws(CannotCompileException::class)
            override fun edit(m: MethodCall) {
                if (m.className == ArrayList::class.qualifiedName && m.methodName == "add") {
                    if (this.count > 0) {
                        m.replace(
                            "if (!( " + InvisiblePower_InvisibleTips.Companion::class.qualifiedName + ".shouldInvisibleTips(p)" +
                                    ")) {\$_ = \$proceed($$);}"
                        )
                    }

                    ++this.count
                }
            }
        }


    @SpirePatch2(clz = AbstractMonster::class, method = "renderTip")
    object RenderMonsterPowerTips {
        @SpireInstrumentPatch
        @JvmStatic
        fun Instrument(): ExprEditor {
            return exprEditor
        }
    }

    @SpirePatch2(clz = AbstractPlayer::class, method = "renderPowerTips")
    object RenderPlayerPowerTips {
        @SpireInstrumentPatch
        @JvmStatic
        fun Instrument(): ExprEditor {
            return exprEditor
        }
    }

    @SpirePatch2(
        clz = AbstractCreature::class,
        method = "renderPowerTips",
        paramtypes = ["com.badlogic.gdx.graphics.g2d.SpriteBatch"]
    )
    object RenderCreaturePowerTips {
        @SpireInstrumentPatch
        @JvmStatic
        fun Instrument(): ExprEditor {
            return object : ExprEditor() {
                @Throws(CannotCompileException::class)
                override fun edit(m: MethodCall) {
                    if (m.className != ArrayList::class.qualifiedName || m.methodName != "add") return
                    m.replace(
                        "if (!( ${InvisiblePower_InvisibleTips.Companion::class.qualifiedName}.shouldInvisibleTips(p) )) " +
                                "{\$_ = \$proceed($$);}"
                    )
                }
            }
        }
    }
}
