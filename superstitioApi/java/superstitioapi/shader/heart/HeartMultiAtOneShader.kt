package superstitioapi.shader.heart

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Interpolation
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import superstitioapi.renderManager.inBattleManager.RenderInBattle
import superstitioapi.shader.ShaderUtility
import java.util.function.Supplier
import kotlin.math.max

object HeartMultiAtOneShader
{
    val heartMultiAtOne: ShaderProgram =
        ShaderUtility.initShader(ShaderUtility.DEFAULT_VERTEX_GLSL, "heart_multiAtOne.glsl")

    fun setUp_heartMultiAtOne(sb: SpriteBatch, anim_timer: Float, whRate: Float)
    {
        sb.shader = heartMultiAtOne
        sb.shader.setUniformf("u_time", anim_timer)
        sb.shader.setUniformf("u_whRate", whRate)
        //        sb.getShader().setUniformf("u_endtime", endTime);
    }

    class RenderHeartMultiAtOne(private val hitboxBondTo: Supplier<Hitbox>) : RenderInBattle
    {
        private var anim_timer = -2.0f

        override fun shouldRemove(): Boolean
        {
            return this.anim_timer >= END_TIME
        }

        override fun render(sb: SpriteBatch)
        {
            ShaderUtility.originShader = sb.shader

            setUp_heartMultiAtOne(sb, anim_timer, 1.0f)
            sb.draw(
                ShaderUtility.NOISE_TEXTURE,
                hitboxBondTo.get().cX - hitboxBondTo.get().width,
                hitboxBondTo.get().cY - hitboxBondTo.get().height / 2,
                hitboxBondTo.get().width * 2.0f, hitboxBondTo.get().height * 2.0f
            )

            sb.shader = ShaderUtility.originShader
        }

        override fun update()
        {
            this.anim_timer += Gdx.graphics.deltaTime * 4.0f
        }

        companion object
        {
            const val END_TIME: Float = 2.5f
            fun addToBot_addHeartMultiAtOneEffect(hitboxBondTo: Supplier<Hitbox>)
            {
                if (!ShaderUtility.canUseShader) return
                RenderInBattle.Register(
                    RenderInBattle.RenderType.Normal,
                    RenderHeartMultiAtOne(hitboxBondTo)
                )
            }
        }
    }

    class HeartMultiAtOneEffect(hitboxForInit: Hitbox) : AbstractGameEffect()
    {
        private val cX = hitboxForInit.cX
        private val cY = hitboxForInit.cY
        private val size = max(hitboxForInit.width, hitboxForInit.height)
        private var anim_time: Float

        init
        {
            this.anim_time = START_TIME
            this.color = Color.WHITE.cpy()
        }

        fun addToEffectsQueue()
        {
            AbstractDungeon.effectsQueue.add(this)
        }

        fun addToEffectList()
        {
            AbstractDungeon.effectList.add(this)
        }

        override fun render(spriteBatch: SpriteBatch)
        {
            ShaderUtility.originShader = spriteBatch.shader
            spriteBatch.color = color
            setUp_heartMultiAtOne(spriteBatch, anim_time, 1.0f)
            spriteBatch.draw(
                ShaderUtility.NOISE_TEXTURE,
                cX - size,
                cY - size / 2,
                size * 2.0f, size * 2.0f
            )
            spriteBatch.shader = ShaderUtility.originShader
        }

        override fun update()
        {
            this.anim_time += Gdx.graphics.deltaTime * 3.0f
            if (this.anim_time > END_TIME)
            {
                this.isDone = true
                this.anim_time = 0.0f
            }

            if (this.anim_time > START_VANISH_TIME)
            {
                color.a = Interpolation.pow2In.apply(
                    1.0f,
                    0.0f,
                    (this.anim_time - START_VANISH_TIME) / (END_TIME - START_VANISH_TIME)
                )
            }
        }

        override fun dispose()
        {
        }

        companion object
        {
            const val END_TIME: Float = 1.0f
            const val START_TIME: Float = -2.0f
            const val START_VANISH_TIME: Float = -1.5f
        }
    }
}
