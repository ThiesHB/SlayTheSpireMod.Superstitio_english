package superstitioapi.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import superstitioapi.DataUtility;
import superstitioapi.Logger;

import java.nio.FloatBuffer;

import static superstitioapi.DataUtility.makeShaderPath;

public class ShaderUtility {
    public static final FloatBuffer transColor = FloatBuffer.wrap(new float[]{0, 0, 0, 0});
    public static final Texture NOISE_TEXTURE = ImageMaster.loadImage(DataUtility.makeImgFilesPath_UI("noise"));
    public static final String DEFAULT_VERTEX_GLSL = "default_vertex.glsl";
    public static boolean canUseShader = true;
    public static ShaderProgram originShader;

    public static ShaderProgram initShader(String vertexShaderName, String fragmentShaderName) { // 初始化着色器程序
        String vertexShader = Gdx.files.internal(makeShaderPath(vertexShaderName)).readString();
        String fragmentShader = Gdx.files.internal(makeShaderPath(fragmentShaderName)).readString();
        ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
        if (!shaderProgram.isCompiled()) {
            canUseShader = false;
            Logger.error(shaderProgram.getLog());
        }
        return shaderProgram;
    }
}
