package superstitioapi.shader

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram

object RingShader {
    private val ringShader: ShaderProgram =
        ShaderUtility.initShader(ShaderUtility.DEFAULT_VERTEX_GLSL, "ring_fragment.glsl")
    private val ringShader_useHalfPic: ShaderProgram = ShaderUtility.initShader(
        ShaderUtility.DEFAULT_VERTEX_GLSL, "ring_fragment_halfPic" +
                ".glsl"
    )

    fun setUp_ringShader(sb: SpriteBatch, radius: Float, halfThick: Float, degreeStart: Float, degreeLength: Float) {
        sb.shader = ringShader
        sb.shader.setUniformf("u_radius", radius)
        sb.shader.setUniformf("u_halfThick", halfThick)
        sb.shader.setUniformf("u_degreeStart", degreeStart)
        sb.shader.setUniformf("u_degreeLength", degreeLength)
    }

    fun setUp_ringShader_useHalfPic(
        sb: SpriteBatch,
        radius: Float,
        halfThick: Float,
        degreeStart: Float,
        degreeLength: Float
    ) {
        sb.shader = ringShader_useHalfPic
        sb.shader.setUniformf("u_radius", radius)
        sb.shader.setUniformf("u_halfThick", halfThick)
        sb.shader.setUniformf("u_degreeStart", degreeStart)
        sb.shader.setUniformf("u_degreeLength", degreeLength)
    }
}
