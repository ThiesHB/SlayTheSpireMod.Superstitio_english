package superstitioapi.player

import basemod.ReflectionHacks
import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.screens.VictoryScreen
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import superstitioapi.shader.heart.HeartStreamShader.RenderHeartStreamEffectGameEnd

interface HasCustomGameOverVfxOnVictory {


    companion object {
        val HITBOX: Hitbox = Hitbox(0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        @SpirePatch2(clz = VictoryScreen::class, method = "updateVfx")
        object VictoryScreenPatch {
            @SpirePostfixPatch
        @JvmStatic
            fun Postfix(__instance: VictoryScreen?) {
                var createdEffect = false
                val effect = ReflectionHacks.getPrivate<ArrayList<AbstractGameEffect>>(
                    __instance,
                    VictoryScreen::class.java,
                    "effect"
                )
                for (e in effect) {
                    if (e is RenderHeartStreamEffectGameEnd) {
                        createdEffect = true
                        break
                    }
                }

                if (!createdEffect) {
                    effect.add(RenderHeartStreamEffectGameEnd(20) { HITBOX })
                }
            }
        }
    }
}
