package superstitioapi.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import superstitioapi.Logger;

import java.nio.FloatBuffer;

import static superstitioapi.DataUtility.makeShaderPath;

public class ShaderUtility {
    public static final FloatBuffer transColor = FloatBuffer.wrap(new float[]{0, 0, 0, 0});
    public static boolean canUseShader = true;
    public static final ShaderProgram ringShader = initShader("ring_vertex.glsl", "ring_fragment.glsl");
    public static final ShaderProgram ringShader_useHalfPic = initShader("ring_vertex.glsl", "ring_fragment_halfPic.glsl");
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
