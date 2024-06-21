package superstitioapi.player;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import superstitioapi.shader.heart.HeartStreamShader;

import java.util.ArrayList;

public interface HasCustomGameOverVfxOnVictory {

    Hitbox HITBOX = new Hitbox(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    @SpirePatch2(clz = VictoryScreen.class, method = "updateVfx")
    public static class VictoryScreenPatch {
        @SpirePostfixPatch
        public static void Postfix(VictoryScreen __instance) {
            boolean createdEffect = false;
            ArrayList<AbstractGameEffect> effect = ReflectionHacks.getPrivate(__instance, VictoryScreen.class, "effect");
            for (AbstractGameEffect e : effect) {
                if (e instanceof HeartStreamShader.RenderHeartStreamEffectGameEnd) {
                    createdEffect = true;
                    break;
                }
            }

            if (!createdEffect) {
                effect.add(new HeartStreamShader.RenderHeartStreamEffectGameEnd(20, () -> HITBOX));
            }
        }
    }
}
