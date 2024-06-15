package superstitioapi.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import superstitioapi.utils.RenderInBattle;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

import static superstitioapi.actions.AutoDoneInstantAction.addToBotAbstract;
import static superstitioapi.shader.ShaderUtility.*;

public class HeartShader {
    public static final ShaderProgram heartStream = ShaderUtility.initShader(ShaderUtility.DEFAULT_VERTEX_GLSL, "heartStream.glsl");
    private static final ShaderProgram heartMultiAtOne = ShaderUtility.initShader(ShaderUtility.DEFAULT_VERTEX_GLSL, "heart_multiAtOne.glsl");

    public static void setUp_heartStream(SpriteBatch sb,
                                         float density, float startTime, float speed,
                                         Vector2 tileTimes, float anim_timer,
                                         float whRate, Vector2 offset, float spawnRemoveTimer) {
        sb.setShader(HeartShader.heartStream);
        sb.getShader().setUniformf("u_density", density);
        sb.getShader().setUniformf("u_startTime", startTime);
        sb.getShader().setUniformf("u_speed", speed);
        sb.getShader().setUniformf("u_tileTimes", tileTimes);
        sb.getShader().setUniformf("u_time", anim_timer);
        sb.getShader().setUniformf("u_whRate", whRate);
        sb.getShader().setUniformf("u_offset", offset);
        sb.getShader().setUniformf("u_spawnRemoveTimer", spawnRemoveTimer);
    }

    public static void setUp_heartMultiAtOne(SpriteBatch sb, float anim_timer, float whRate) {
        sb.setShader(HeartShader.heartMultiAtOne);
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
                    new HeartShader.RenderHeartMultiAtOne(hitboxBondTo));
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

    public static class RenderHeartStream implements RenderInBattle {
        private final int maxLevel;
        private final Supplier<Boolean> customShouldRemove;
        private final Supplier<Hitbox> hitboxBondTo;
        private final ArrayList<HeartStreamSubRender> heartStreamSubRenders = new ArrayList<>();
        private float level;
        private float levelTarget;

        public RenderHeartStream(int maxLevel, Supplier<Boolean> customShouldRemove, Supplier<Hitbox> hitboxBondTo) {
            this.customShouldRemove = customShouldRemove;
            this.hitboxBondTo = hitboxBondTo;
            level = 3.0f;
            levelTarget = 3.5f;
            this.maxLevel = maxLevel;
            levelUp();
        }

        public static void addToBot_addHeartStreamEffect(int maxLevel, Supplier<Boolean> customShouldRemove, Supplier<Hitbox> hitboxBondTo) {
            if (!canUseShader) return;
            Optional<RenderHeartStream> renderOrgasm =
                    RenderInBattle.getRenderGroup(RenderInBattle.RenderType.Stance).stream()
                            .filter(renderInBattle -> renderInBattle instanceof HeartShader.RenderHeartStream)
                            .map(renderInBattle -> (HeartShader.RenderHeartStream) renderInBattle)
                            .findAny();
            if (renderOrgasm.isPresent())
                addToBotAbstract(() ->
                        renderOrgasm.get().levelUp());
            else
                RenderInBattle.Register(RenderInBattle.RenderType.Stance,
                        new HeartShader.RenderHeartStream(maxLevel, customShouldRemove, hitboxBondTo));
        }

        private void levelUp() {
            levelTarget += 1.0f;
            if (heartStreamSubRenders.size() >= maxLevel) return;
            int newIndex = (int) Math.min(maxLevel, Math.ceil(level));
            for (int i = heartStreamSubRenders.size(); i < newIndex; i++) {
                heartStreamSubRenders.add(new HeartStreamSubRender(this,
                        0.1f + 0.1f * i,
                        1.4f - 0.08f * i,
                        new Vector2(5 + i, 5 + i)));
            }
        }


        @Override
        public void render(SpriteBatch sb) {
            ShaderUtility.originShader = sb.getShader();
            float density = (float) (1.0f / Math.pow(level, 0.5f));
            this.heartStreamSubRenders.forEach(heartStreamSubRender ->
                    heartStreamSubRender.drawHeartStream(sb, (float) (0.8 * density),
                            new Vector2((Gdx.graphics.getWidth() / 2.0f - hitboxBondTo.get().cX) / Gdx.graphics.getWidth(), 0.0f)));
            sb.setShader(originShader);
        }

        @Override
        public boolean shouldRemove() {
            return customShouldRemove.get() && heartStreamSubRenders.stream().allMatch(heartStreamSubRender -> heartStreamSubRender.anim_timer < 0.0f);
        }

        @Override
        public void update() {
            if (level != levelTarget)
                level = MathHelper.uiLerpSnap(this.level, this.levelTarget);
            heartStreamSubRenders.forEach(HeartStreamSubRender::update);
        }

        private static class HeartStreamSubRender {
            public static final float ALPHA_TIME = 2.0f;
            private final RenderHeartStream owner;
            private final float startTime;
            private final float speed;
            private final Vector2 tileTimes;
            private float anim_timer = 0.0f;

            private HeartStreamSubRender(RenderHeartStream owner, float startTime, float speed, Vector2 tileTimes) {
                this.owner = owner;
                this.startTime = startTime;
                this.speed = speed;
                this.tileTimes = tileTimes;
            }

            public void update() {
                if (!owner.customShouldRemove.get()) {
                    anim_timer += Gdx.graphics.getDeltaTime();
                    return;
                }
                if (anim_timer >= ALPHA_TIME)
                    anim_timer = ALPHA_TIME;
                anim_timer -= Gdx.graphics.getDeltaTime();
            }

            private void drawHeartStream(SpriteBatch sb, float density, Vector2 offset) {
                float spawnRemoveTimer = Math.min(ALPHA_TIME, Math.max(anim_timer, 0.0f)) / ALPHA_TIME;
                float height = Gdx.graphics.getHeight();
                float width = Gdx.graphics.getWidth();
                setUp_heartStream(sb, density, startTime, speed, tileTimes, anim_timer, width / height, offset, spawnRemoveTimer);
                sb.draw(NOISE_TEXTURE, 0, 0, width, height);
            }
        }
    }
}
