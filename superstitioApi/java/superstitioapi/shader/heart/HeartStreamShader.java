package superstitioapi.shader.heart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import superstitioapi.renderManager.inBattleManager.RenderInBattle;
import superstitioapi.shader.ShaderUtility;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

import static superstitioapi.actions.AutoDoneInstantAction.addToBotAbstract;
import static superstitioapi.shader.ShaderUtility.*;

public class HeartStreamShader {
    public static final ShaderProgram heartStream = ShaderUtility.initShader(ShaderUtility.DEFAULT_VERTEX_GLSL, "heartStream.glsl");

    public static void setUp_heartStream(SpriteBatch sb,
                                         float density, float startTime, float speed,
                                         Vector2 tileTimes, float anim_timer,
                                         float whRate, Vector2 offset, float spawnRemoveTimer) {
        sb.setShader(heartStream);
        sb.getShader().setUniformf("u_density", density);
        sb.getShader().setUniformf("u_startTime", startTime);
        sb.getShader().setUniformf("u_speed", speed);
        sb.getShader().setUniformf("u_tileTimes", tileTimes);
        sb.getShader().setUniformf("u_time", anim_timer);
        sb.getShader().setUniformf("u_whRate", whRate);
        sb.getShader().setUniformf("u_offset", offset);
        sb.getShader().setUniformf("u_spawnRemoveTimer", spawnRemoveTimer);
    }

    public static class RenderHeartStreamEffectGameEnd extends AbstractGameEffect {
        private static final int MAX_LEVEL = 8;
        //        private final int maxLevel;
        private final Supplier<Hitbox> hitboxBondTo;
        private final ArrayList<HeartStreamSubRender> heartStreamSubRenders = new ArrayList<>();
        private final float level;

        public RenderHeartStreamEffectGameEnd(int level, Supplier<Hitbox> hitboxBondTo) {
            this.hitboxBondTo = hitboxBondTo;
            this.level = level;
            initSubRender();
        }

        private void initSubRender() {
            if (heartStreamSubRenders.size() >= level) return;
            int newIndex = (int) Math.min(MAX_LEVEL, Math.ceil(level));
            for (int i = heartStreamSubRenders.size(); i < newIndex; i++) {
                heartStreamSubRenders.add(new HeartStreamSubRender(
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
        public void dispose() {
        }

        @Override
        public void update() {
//            if (level != levelTarget)
//                level = MathHelper.uiLerpSnap(this.level, this.levelTarget);
            heartStreamSubRenders.forEach(HeartStreamSubRender::update);
        }

        private static class HeartStreamSubRender {
            public static final float ALPHA_TIME = 2.0f;
            private final float startTime;
            private final float speed;
            private final Vector2 tileTimes;
            private float anim_timer = 0.0f;

            private HeartStreamSubRender(float startTime, float speed, Vector2 tileTimes) {
                this.startTime = startTime;
                this.speed = speed;
                this.tileTimes = tileTimes;
            }

            public void update() {
                anim_timer += Gdx.graphics.getDeltaTime();
                if (anim_timer <= 0)
                    anim_timer = 0;
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
                    RenderInBattle.getRenderGroup(RenderType.Stance).stream()
                            .filter(renderInBattle -> renderInBattle instanceof RenderHeartStream)
                            .map(renderInBattle -> (RenderHeartStream) renderInBattle)
                            .findAny();
            if (renderOrgasm.isPresent())
                addToBotAbstract(() ->
                        renderOrgasm.get().levelUp());
            else
                RenderInBattle.Register(RenderType.Stance,
                        new RenderHeartStream(maxLevel, customShouldRemove, hitboxBondTo));
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
