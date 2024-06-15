package superstitioapi.shader;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class RingShader {
    private static final ShaderProgram ringShader = ShaderUtility.initShader(ShaderUtility.DEFAULT_VERTEX_GLSL, "ring_fragment.glsl");
    private static final ShaderProgram ringShader_useHalfPic = ShaderUtility.initShader(ShaderUtility.DEFAULT_VERTEX_GLSL, "ring_fragment_halfPic.glsl");

    public static void setUp_ringShader(SpriteBatch sb,float radius,float halfThick,float degreeStart,float degreeLength){
        sb.setShader(ringShader);
        sb.getShader().setUniformf("u_radius", radius);
        sb.getShader().setUniformf("u_halfThick", halfThick);
        sb.getShader().setUniformf("u_degreeStart", degreeStart);
        sb.getShader().setUniformf("u_degreeLength", degreeLength);
    }

    public static void setUp_ringShader_useHalfPic(SpriteBatch sb,float radius,float halfThick,float degreeStart,float degreeLength){
        sb.setShader(ringShader_useHalfPic);
        sb.getShader().setUniformf("u_radius", radius);
        sb.getShader().setUniformf("u_halfThick", halfThick);
        sb.getShader().setUniformf("u_degreeStart", degreeStart);
        sb.getShader().setUniformf("u_degreeLength", degreeLength);
    }
}
