package superstitioapi.shader.heart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import superstitioapi.shader.ShaderUtility;
import superstitioapi.renderManager.inBattleManager.RenderInBattle;

import java.util.function.Supplier;

import static superstitioapi.shader.ShaderUtility.*;

public class HeartMultiAtOneShader {
    static final ShaderProgram heartMultiAtOne = ShaderUtility.initShader(ShaderUtility.DEFAULT_VERTEX_GLSL, "heart_multiAtOne.glsl");

    public static void setUp_heartMultiAtOne(SpriteBatch sb, float anim_timer, float whRate) {
        sb.setShader(heartMultiAtOne);
        sb.getShader().setUniformf("u_time", anim_timer);
        sb.getShader().setUniformf("u_whRate", whRate);
//        sb.getShader().setUniformf("u_endtime", endTime);
    }

    public static class RenderHeartMultiAtOne implements RenderInBattle {
        public static final float END_TIME = 2.5f;
        private final Supplier<Hitbox> hitboxBondTo;
        private float anim_timer = -2.0f;

        public RenderHeartMultiAtOne(Supplier<Hitbox> hitboxBondTo) {
            this.hitboxBondTo = hitboxBondTo;
        }

        public static void addToBot_addHeartMultiAtOneEffect(Supplier<Hitbox> hitboxBondTo) {
            if (!canUseShader) return;
            RenderInBattle.Register(RenderType.Normal,
                    new RenderHeartMultiAtOne(hitboxBondTo));
        }

        @Override
        public boolean shouldRemove() {
            return this.anim_timer >= END_TIME;
        }

        @Override
        public void render(SpriteBatch sb) {
            ShaderUtility.originShader = sb.getShader();

            setUp_heartMultiAtOne(sb, anim_timer, 1.0f);
            sb.draw(NOISE_TEXTURE,
                    hitboxBondTo.get().cX - hitboxBondTo.get().width,
                    hitboxBondTo.get().cY - hitboxBondTo.get().height / 2,
                    hitboxBondTo.get().width * 2.0f, hitboxBondTo.get().height * 2.0f);

            sb.setShader(originShader);
        }

        @Override
        public void update() {
            this.anim_timer += Gdx.graphics.getDeltaTime() * 4.0f;
        }

    }

    public static class HeartMultiAtOneEffect extends AbstractGameEffect {
        public static final float END_TIME = 1.0f;
        public static final float START_TIME = -2.0f;
        public static final float START_VANISH_TIME = -1.5f;
        private final float cX;
        private final float cY;
        private final float size;
        private float anim_time;

        public HeartMultiAtOneEffect(Hitbox hitboxForInit) {
            this.cX = hitboxForInit.cX;
            this.cY = hitboxForInit.cY;
            this.size = Math.max(hitboxForInit.width, hitboxForInit.height);
            this.anim_time = START_TIME;
            this.color = Color.WHITE.cpy();
        }

        @Override
        public void render(SpriteBatch spriteBatch) {
            ShaderUtility.originShader = spriteBatch.getShader();
            spriteBatch.setColor(color);
            setUp_heartMultiAtOne(spriteBatch, anim_time, 1.0f);
            spriteBatch.draw(NOISE_TEXTURE,
                    cX - size,
                    cY - size / 2,
                    size * 2.0f, size * 2.0f);
            spriteBatch.setShader(originShader);
        }

        @Override
        public void update() {
            this.anim_time += Gdx.graphics.getDeltaTime() * 3.0f;
            if (this.anim_time > END_TIME) {
                this.isDone = true;
                this.anim_time = 0.0f;
            }

            if (this.anim_time > START_VANISH_TIME) {
                this.color.a = Interpolation.pow2In.apply(1.0f, 0.0f, (this.anim_time - START_VANISH_TIME) / (END_TIME - START_VANISH_TIME));
            }
        }

        public void addToEffectsQueue() {
            AbstractDungeon.effectsQueue.add(this);
        }

        public void addToEffectList() {
            AbstractDungeon.effectList.add(this);
        }

        @Override
        public void dispose() {
        }
    }
}
