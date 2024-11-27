package superstitioapi.powers.interfaces

import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.powers.AbstractPower

object DecreaseHealthBarNumberPowerPatch
{
    private const val HIDDEN_DEEPER = 0.2f

    fun GetAllDecreaseHealthBarNumberShouldShown(abstractCreature: AbstractCreature): Int
    {
        return abstractCreature.powers.stream()
            .filter { power: AbstractPower? -> power is DecreaseHealthBarNumberPower }
            .filter { power: AbstractPower -> (power as DecreaseHealthBarNumberPower).showDecreaseAmount() }
            .mapToInt { power: AbstractPower -> (power as DecreaseHealthBarNumberPower).getDecreaseAmount() }.sum()
    }

    fun GetAllDecreaseHealthBarNumberTotall(abstractCreature: AbstractCreature): Int
    {
        return abstractCreature.powers.stream()
            .filter { power: AbstractPower? -> power is DecreaseHealthBarNumberPower }
            .mapToInt { power: AbstractPower -> (power as DecreaseHealthBarNumberPower).getDecreaseAmount() }.sum()
    }

    //    @SpirePatch2(clz = AbstractCreature.class, method = "renderHealthText")
    //    public static class DecreaseNumberPatch {
    //        public static ExprEditor Instrument() {
    //            return new ExprEditor() {
    //                public void edit(final FieldAccess m) throws CannotCompileException {
    //                    if (m.getFieldName().equals("currentHealth") && m.isReader()) {
    //                        m.replace("\$_ = -" + DecreaseHealthBarNumberPowerPatch.class.getName() + ".GetAllDecreaseHealthBarNumber( $0 ) + $proceed" +
    //                                "($$) ;");
    //                    }
    //                }
    //            };
    //        }
    //    }
    @SpirePatch2(clz = AbstractCreature::class, method = "renderHealthText")
    object DecreaseNumberTextPatch
    {
        private val HEALTH_BAR_OFFSET_Y = -28.0f * Settings.scale
        private val HEALTH_TEXT_OFFSET_Y = 6.0f * Settings.scale

        @SpirePrefixPatch
        @JvmStatic
        fun Prefix(__instance: AbstractCreature, sb: SpriteBatch?, y: Float): SpireReturn<Void>
        {
            val targetHealthBarWidth =
                ReflectionHacks.getPrivate<Float>(__instance, AbstractCreature::class.java, "targetHealthBarWidth")

            val hbTextColor = ReflectionHacks.getPrivate<Color>(__instance, AbstractCreature::class.java, "hbTextColor")

            if (targetHealthBarWidth == 0.0f)
            {
//                FontHelper.renderFontCentered(spriteBatch, FontHelper.healthInfoFont, AbstractCreature.TEXT[0], __instance.hb.cX, y +
//                HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y - Settings.scale, hbTextColor);
                return SpireReturn.Continue()
            }

            if (__instance.healthHb.hovered)
            {
                val decreaseNumber = GetAllDecreaseHealthBarNumberTotall(__instance)
                if (decreaseNumber <= 0) return SpireReturn.Continue()

                hbTextColor.a *= HIDDEN_DEEPER

                val renderColor = __instance.powers
                    .filter { power: AbstractPower? -> power is HealthBarRenderPower }
                    .map { power: AbstractPower -> (power as HealthBarRenderPower).color }
                    .firstOrNull() ?: hbTextColor


                FontHelper.renderFontCentered(
                    sb, FontHelper.healthInfoFont,
                    decreaseNumber.toString(), __instance.hb.cX,
                    y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0f * Settings.scale, renderColor
                )
            }
            else
            {
                val decreaseNumber = GetAllDecreaseHealthBarNumberShouldShown(__instance)
                if (decreaseNumber <= 0) return SpireReturn.Continue()
                val HEALTH_BAR_OFFSET_Y = -28.0f * Settings.scale
                val HEALTH_TEXT_OFFSET_Y = 6.0f * Settings.scale


                FontHelper.renderFontCentered(
                    sb,
                    FontHelper.healthInfoFont,
                    __instance.currentHealth.toString() + "(-" + decreaseNumber + ")" + "/" + __instance.maxHealth,
                    __instance.hb.cX,
                    y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0f * Settings.scale,
                    hbTextColor
                )
            }

            return SpireReturn.Return()
        }
    }
}
