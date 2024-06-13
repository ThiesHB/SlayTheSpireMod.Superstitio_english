package superstitioapi.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ImgUtility {


    public final static Color nullColor = new Color(0, 0, 0, 0);

    public static void draw(final SpriteBatch sb, Texture texture, float x, float y,
                            float width, float height, float rotation) {
        sb.draw(texture, x, y, 0, 0, width, height, 1.0f, 1.0f, rotation, 0, 0, texture.getWidth(), texture.getHeight(),
                false, false);
    }

    public static void draw(final SpriteBatch sb, Texture texture, float x, float y,
                            float width, float height, float rotation, boolean flipX, boolean flipY) {
        sb.draw(texture, x, y, 0, 0, width, height, 1.0f, 1.0f, rotation, 0, 0, texture.getWidth(), texture.getHeight(),
                flipX, flipY);
    }

    public static Color mixColor(Color colorBase, Color colorInsert, float InsertRate) {
        float r = (colorBase.r + colorInsert.r * InsertRate) / (1.0f + InsertRate);
        float g = (colorBase.g + colorInsert.g * InsertRate) / (1.0f + InsertRate);
        float b = (colorBase.b + colorInsert.b * InsertRate) / (1.0f + InsertRate);
        float a = (colorBase.a + colorInsert.a * InsertRate) / (1.0f + InsertRate);
        return new Color(r, g, b, a);
    }

    public static Color mixColor(Color colorBase, Color colorInsert, float InsertRate, float alpha) {
        float r = (colorBase.r + colorInsert.r * InsertRate) / (1.0f + InsertRate);
        float g = (colorBase.g + colorInsert.g * InsertRate) / (1.0f + InsertRate);
        float b = (colorBase.b + colorInsert.b * InsertRate) / (1.0f + InsertRate);
        return new Color(r, g, b, alpha);
    }


}
