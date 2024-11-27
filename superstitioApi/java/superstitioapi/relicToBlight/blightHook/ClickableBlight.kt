package superstitioapi.relicToBlight.blightHook

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic
import com.evacipated.cardcrawl.modthespire.lib.*
import com.evacipated.cardcrawl.modthespire.lib.Matcher.MethodCallMatcher
import com.megacrit.cardcrawl.blights.AbstractBlight
import com.megacrit.cardcrawl.core.OverlayMenu
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet
import com.megacrit.cardcrawl.ui.panels.TopPanel
import javassist.CannotCompileException
import javassist.CtBehavior
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import sun.reflect.generics.reflectiveObjects.NotImplementedException

interface ClickableBlight : ClickableRelic {
    override fun clickUpdate() {
        if (this !is AbstractBlight) {
            throw NotImplementedException()
        } else {
            val blight = this as AbstractBlight
            if (HitboxRightClick.rightClicked[blight.hb] || (Settings.isControllerMode && blight.hb.hovered && CInputActionSet.topPanel.isJustPressed)) {
                CInputActionSet.topPanel.unpress()
                this.onRightClick()
            }
        }
    }

    override fun hovered(): Boolean {
        if (this is AbstractBlight) {
            val blight = this as AbstractBlight
            return blight.hb.hovered
        } else {
            throw NotImplementedException()
        }
    }

    companion object {
        @SpirePatch2(clz = OverlayMenu::class, method = "update")
        object ClickableBlightUpdatePatch {
            @SpireInsertPatch(locator = Locator::class, localvars = ["b"])
            @JvmStatic
            fun Insert(__instance: OverlayMenu?, b: AbstractBlight?) {
                if (b is ClickableBlight) {
                    (b as ClickableBlight).clickUpdate()
                }
            }

            private class Locator : SpireInsertLocator() {
                @Throws(Exception::class)
                override fun Locate(ctMethodToPatch: CtBehavior): IntArray {
                    val finalMatcher: Matcher = MethodCallMatcher(AbstractBlight::class.java, "update")
                    return LineFinder.findInOrder(ctMethodToPatch, finalMatcher)
                }
            }
        }


        @JvmStatic
        fun isClickableBlightHovered(action: Any?, image: Any?): Boolean {
            return if (!Settings.isControllerMode || action != null && action !== CInputActionSet.topPanel || image != null && image !== CInputActionSet.topPanel.keyImg) {
                false
            } else {
                AbstractDungeon.player != null && AbstractDungeon.player.blights != null && AbstractDungeon.player.blights.stream()
                    .anyMatch { b: AbstractBlight -> b.hb.hovered && b is ClickableBlight }
            }
        }

        @SpirePatch2(clz = TopPanel::class, method = "renderControllerUi")
        object HidePotionButtonPatch {
            @SpireInstrumentPatch
            @JvmStatic
            fun Instrument(): ExprEditor {
                return object : ExprEditor() {
                    @Throws(CannotCompileException::class)
                    override fun edit(m: MethodCall) {
                        if (m.methodName == "draw") {
                            m.replace(
                                String.format(
                                    "if (!%s.isClickableBlightHovered(null, $1)) { \$_ = \$proceed($$); }",
                                    Companion::class.qualifiedName
                                )
                            )
                        }
                    }
                }
            }
        }

        @SpirePatch2(clz = TopPanel::class, method = "update")
        object DisablePotionButtonPatch {
            @SpireInstrumentPatch
            @JvmStatic
            fun Instrument(): ExprEditor {
                return object : ExprEditor() {
                    @Throws(CannotCompileException::class)
                    override fun edit(m: MethodCall) {
                        if (m.methodName == "isJustPressed") {
                            m.replace(
                                String.format(
                                    "\$_ = \$proceed($$) && !%s.isClickableBlightHovered($0, null);",
                                    Companion::class.qualifiedName
                                )
                            )
                        }
                    }
                }
            }
        }

        @SpirePatch2(clz = AbstractBlight::class, method = "renderInTopPanel", paramtypez = [SpriteBatch::class])
        object AbstractBlightRenderPatch {
            @SpirePostfixPatch
            @JvmStatic
            fun Postfix(__instance: AbstractBlight, ___sb: SpriteBatch) {
                if (!Settings.hideRelics && Settings.isControllerMode && __instance is ClickableBlight && (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.BOSS_REWARD || __instance.isObtained) && __instance.hb.hovered) {
                    val scale = Settings.scale
                    ___sb.setColor(1.0f, 1.0f, 1.0f, 1.0f)
                    val texture = TextureRegion(CInputActionSet.topPanel.keyImg)
                    ___sb.draw(
                        texture,
                        __instance.currentX - 30.0f * scale - texture.regionWidth.toFloat() / 2.0f,
                        __instance.currentY - 35.0f * scale - texture.regionHeight.toFloat() / 2.0f,
                        texture.regionWidth.toFloat() / 2.0f,
                        texture.regionHeight.toFloat() / 2.0f,
                        texture.regionWidth.toFloat(),
                        texture.regionHeight.toFloat(),
                        scale * 0.85f,
                        scale * 0.85f,
                        0.0f
                    )
                }
            }
        }
    }
}
