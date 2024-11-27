package superstitioapi.shader.heart

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.MathHelper
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import superstitioapi.renderManager.inBattleManager.RenderInBattle
import superstitioapi.shader.ShaderUtility
import superstitioapi.utils.ActionUtility.VoidSupplier
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object HeartStreamShader {
    val heartStream: ShaderProgram = ShaderUtility.initShader(ShaderUtility.DEFAULT_VERTEX_GLSL, "heartStream.glsl")

    fun setUp_heartStream(
        sb: SpriteBatch,
        density: Float, startTime: Float, speed: Float,
        tileTimes: Vector2?, anim_timer: Float,
        whRate: Float, offset: Vector2?, spawnRemoveTimer: Float
    ) {
        sb.shader = heartStream
        sb.shader.setUniformf("u_density", density)
        sb.shader.setUniformf("u_startTime", startTime)
        sb.shader.setUniformf("u_speed", speed)
        sb.shader.setUniformf("u_tileTimes", tileTimes)
        sb.shader.setUniformf("u_time", anim_timer)
        sb.shader.setUniformf("u_whRate", whRate)
        sb.shader.setUniformf("u_offset", offset)
        sb.shader.setUniformf("u_spawnRemoveTimer", spawnRemoveTimer)
    }

    class RenderHeartStreamEffectGameEnd(
        level: Int, //        private final int maxLevel;
        private val hitboxBondTo: Supplier<Hitbox>
    ) : AbstractGameEffect() {
        private val heartStreamSubRenders = ArrayList<HeartStreamSubRender>()
        private val level = level.toFloat()

        init {
            initSubRender()
        }

        private fun initSubRender() {
            if (heartStreamSubRenders.size >= level) return
            val newIndex = min(MAX_LEVEL.toDouble(), ceil(level.toDouble()))
                .toInt()
            for (i in heartStreamSubRenders.size until newIndex) {
                heartStreamSubRenders.add(
                    HeartStreamSubRender(
                        0.1f + 0.1f * i,
                        1.4f - 0.08f * i,
                        Vector2((5 + i).toFloat(), (5 + i).toFloat())
                    )
                )
            }
        }


        override fun render(sb: SpriteBatch) {
            ShaderUtility.originShader = sb.shader
            val density: Float = (1.0f / level.pow(0.5f)).toFloat()
            heartStreamSubRenders.forEach(Consumer { heartStreamSubRender: HeartStreamSubRender ->
                heartStreamSubRender.drawHeartStream(
                    sb, (0.8 * density).toFloat(),
                    Vector2((Gdx.graphics.width / 2.0f - hitboxBondTo.get().cX) / Gdx.graphics.width, 0.0f)
                )
            })
            sb.shader = ShaderUtility.originShader
        }

        override fun dispose() {
        }

        override fun update() {
//            if (level != levelTarget)
//                level = MathHelper.uiLerpSnap(this.level, this.levelTarget);
            heartStreamSubRenders.forEach(Consumer(HeartStreamSubRender::update))
        }

        private class HeartStreamSubRender(
            private val startTime: Float,
            private val speed: Float,
            private val tileTimes: Vector2
        ) {
            private var anim_timer = 0.0f

            fun update() {
                anim_timer += Gdx.graphics.deltaTime
                if (anim_timer <= 0) anim_timer = 0f
            }

            fun drawHeartStream(sb: SpriteBatch, density: Float, offset: Vector2) {
                val spawnRemoveTimer = (min(
                    ALPHA_TIME.toDouble(),
                    max(anim_timer.toDouble(), 0.0)
                ) / ALPHA_TIME).toFloat()
                val height = Gdx.graphics.height.toFloat()
                val width = Gdx.graphics.width.toFloat()
                setUp_heartStream(
                    sb,
                    density,
                    startTime,
                    speed,
                    tileTimes,
                    anim_timer,
                    width / height,
                    offset,
                    spawnRemoveTimer
                )
                sb.draw(ShaderUtility.NOISE_TEXTURE, 0f, 0f, width, height)
            }

            companion object {
                const val ALPHA_TIME: Float = 2.0f
            }
        }

        companion object {
            private const val MAX_LEVEL = 8
        }
    }

    class RenderHeartStream @JvmOverloads constructor(
        private val maxLevel: Int,
        private val customShouldRemove: Supplier<Boolean>,
        private val hitboxBondTo: Supplier<Hitbox>,
        levelTarget: Float = 0.0f
    ) : RenderInBattle {
        private val heartStreamSubRenders = ArrayList<HeartStreamSubRender>()
        private var forceRemove = false
        private var level = 3.0f
        private var levelTarget: Float

        init {
            this.levelTarget = levelTarget + 3.5f
            checkLevelUp()
        }

        private fun checkLevelUp() {
//            levelTarget += 1.0f;
            if (heartStreamSubRenders.size >= maxLevel) return
            val newIndex = min(maxLevel.toDouble(), ceil(level.toDouble())).toInt()
            for (i in heartStreamSubRenders.size until newIndex) {
                heartStreamSubRenders.add(
                    HeartStreamSubRender(
                        this,
                        0.1f + 0.1f * i,
                        1.4f - 0.08f * i,
                        Vector2((5 + i).toFloat(), (5 + i).toFloat())
                    )
                )
            }
        }

        private fun startEndRenderMode(): Boolean {
            return customShouldRemove.get() || forceRemove
        }

        override fun render(sb: SpriteBatch) {
            ShaderUtility.originShader = sb.shader
            val density: Float = (1.0f / level.pow(0.5f)).toFloat()
            heartStreamSubRenders.forEach(Consumer { heartStreamSubRender: HeartStreamSubRender ->
                heartStreamSubRender.drawHeartStream(
                    sb, (0.8 * density).toFloat(),
                    Vector2((Gdx.graphics.width / 2.0f - hitboxBondTo.get().cX) / Gdx.graphics.width, 0.0f)
                )
            })
            sb.shader = ShaderUtility.originShader
        }

        override fun shouldRemove(): Boolean {
            return startEndRenderMode() && heartStreamSubRenders.stream()
                .allMatch { heartStreamSubRender: HeartStreamSubRender -> heartStreamSubRender.anim_timer < 0.0f }
        }

        override fun update() {
            if (level != levelTarget) level = MathHelper.uiLerpSnap(this.level, this.levelTarget)
            heartStreamSubRenders.forEach(Consumer(HeartStreamSubRender::update))
        }

        private class HeartStreamSubRender(
            private val owner: RenderHeartStream,
            private val startTime: Float,
            private val speed: Float,
            private val tileTimes: Vector2
        ) {
            var anim_timer = 0.0f

            fun update() {
                if (!owner.startEndRenderMode()) {
                    anim_timer += Gdx.graphics.deltaTime
                    if (anim_timer >= 20.0f) {
                        anim_timer = ALPHA_TIME
                    }
                    return
                }
                if (anim_timer >= ALPHA_TIME) anim_timer = ALPHA_TIME
                anim_timer -= Gdx.graphics.deltaTime
            }

            fun drawHeartStream(sb: SpriteBatch, density: Float, offset: Vector2) {
                val spawnRemoveTimer = (min(
                    ALPHA_TIME.toDouble(),
                    max(anim_timer.toDouble(), 0.0)
                ) / ALPHA_TIME).toFloat()
                val height = Gdx.graphics.height.toFloat()
                val width = Gdx.graphics.width.toFloat()
                setUp_heartStream(
                    sb,
                    density,
                    startTime,
                    speed,
                    tileTimes,
                    anim_timer,
                    width / height,
                    offset,
                    spawnRemoveTimer
                )
                sb.draw(ShaderUtility.NOISE_TEXTURE, 0f, 0f, width, height)
            }

            companion object {
                const val ALPHA_TIME: Float = 2.0f
            }
        }

        companion object {
            fun action_addHeartStreamEffect(
                maxLevel: Int, customShouldRemove: Supplier<Boolean>,
                hitboxBondTo: Supplier<Hitbox>
            ): VoidSupplier {
                if (!ShaderUtility.canUseShader) return VoidSupplier.Empty
                val renderHeartStream = renderHeartStream
                return renderHeartStream?.let { stream: RenderHeartStream ->
                    VoidSupplier {
                        stream.levelTarget++
                        stream.checkLevelUp()
                    }
                } ?: VoidSupplier {
                    RenderInBattle.Register(
                        RenderInBattle.RenderType.Stance,
                        RenderHeartStream(maxLevel, customShouldRemove, hitboxBondTo)
                    )
                }
            }

            fun action_addHeartStreamEffectAndSetLevel(
                maxLevel: Int, customShouldRemove: Supplier<Boolean>,
                hitboxBondTo: Supplier<Hitbox>, setLevel: Int
            ): VoidSupplier {
                if (!ShaderUtility.canUseShader) return VoidSupplier.Empty
                val renderHeartStream = renderHeartStream
                    ?: return VoidSupplier {
                        RenderInBattle.Register(
                            RenderInBattle.RenderType.Stance,
                            RenderHeartStream(maxLevel, customShouldRemove, hitboxBondTo, setLevel.toFloat())
                        )
                    }
                return renderHeartStream.let { stream: RenderHeartStream ->
                    VoidSupplier {
                        stream.levelTarget = setLevel.toFloat()
                        stream.checkLevelUp()
                    }
                }
            }

            fun action_setStopRender(): VoidSupplier {
                return VoidSupplier {
                    renderHeartStream.let { renderHeartStream: RenderHeartStream? ->
                        renderHeartStream?.forceRemove = true
                    }
                }
            }

            private val renderHeartStream: RenderHeartStream?
                get() = RenderInBattle.getRenderGroup(RenderInBattle.RenderType.Stance)
                    .filterIsInstance<RenderHeartStream>()
                    .firstOrNull()
        }
    }
}
