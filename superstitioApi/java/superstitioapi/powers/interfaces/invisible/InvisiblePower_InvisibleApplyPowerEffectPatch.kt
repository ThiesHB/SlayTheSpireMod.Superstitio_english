package superstitioapi.powers.interfaces.invisible

import com.evacipated.cardcrawl.modthespire.lib.*
import com.evacipated.cardcrawl.modthespire.lib.Matcher.MethodCallMatcher
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect
import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect
import javassist.CannotCompileException
import javassist.CtBehavior
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

object InvisiblePower_InvisibleApplyPowerEffectPatch {
    @SpirePatch2(clz = ApplyPowerAction::class, method = "update")
    object RemoveApplicationEffectsForInvisiblePower {
        @SpireInsertPatch(locator = Locator::class)
        @JvmStatic
        fun antiApplicationEffect(__instance: ApplyPowerAction?, ___powerToApply: AbstractPower) {
            if (___powerToApply !is InvisiblePower_InvisibleApplyPowerEffect) return
            for (i in AbstractDungeon.effectList.size - 1 downTo -1 + 1) {
                if (___powerToApply.type == PowerType.DEBUFF) {
                    if (AbstractDungeon.effectList[i] is PowerDebuffEffect) {
                        AbstractDungeon.effectList.removeAt(i)
                    }
                } else if (___powerToApply.type == PowerType.BUFF
                    && AbstractDungeon.effectList[i] is PowerBuffEffect
                ) {
                    AbstractDungeon.effectList.removeAt(i)
                }
            }
        }


        private class Locator : SpireInsertLocator() {
            @Throws(Exception::class)
            override fun Locate(ctMethodToPatch: CtBehavior): IntArray {
                val finalMatcher: Matcher = MethodCallMatcher(AbstractDungeon::class.java, "onModifyPower")
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)
            }
        }
    }

    @SpirePatch2(clz = ApplyPowerAction::class, method = "update")
    object RemoveFlashEffectsForInvisiblePower {
        @SpireInstrumentPatch
        @JvmStatic
        fun DontFlashInvisiblePower(): ExprEditor {
            return object : ExprEditor() {
                @Throws(CannotCompileException::class)
                override fun edit(m: MethodCall) {
                    if (m.className == AbstractPower::class.qualifiedName && m.methodName == "flash") {
                        m.replace(
                            "if (!(powerToApply instanceof "
                                    + InvisiblePower_InvisibleApplyPowerEffect::class.qualifiedName + ")) {\$_ = \$proceed($$);}"
                        )
                    }
                }
            }
        }
    }
}
