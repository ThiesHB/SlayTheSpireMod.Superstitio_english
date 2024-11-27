package superstitioapi.shader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.megacrit.cardcrawl.helpers.ImageMaster
import superstitioapi.DataUtility
import superstitioapi.Logger
import java.nio.FloatBuffer

object ShaderUtility
{
    val transColor: FloatBuffer = FloatBuffer.wrap(floatArrayOf(0f, 0f, 0f, 0f))
    val NOISE_TEXTURE: Texture = ImageMaster.loadImage(DataUtility.makeImgFilesPath_UI("noise"))
    const val DEFAULT_VERTEX_GLSL: String = "default_vertex.glsl"

    @JvmField
    var canUseShader: Boolean = true
    var originShader: ShaderProgram? = null

    fun initShader(vertexShaderName: String, fragmentShaderName: String): ShaderProgram
    { // 初始化着色器程序
        val vertexShader = Gdx.files.internal(DataUtility.makeShaderPath(vertexShaderName)).readString()
        val fragmentShader = Gdx.files.internal(DataUtility.makeShaderPath(fragmentShaderName)).readString()
        val shaderProgram = ShaderProgram(vertexShader, fragmentShader)
        if (!shaderProgram.isCompiled)
        {
            canUseShader = false
            Logger.error(shaderProgram.log)
        }
        return shaderProgram
    }
}
