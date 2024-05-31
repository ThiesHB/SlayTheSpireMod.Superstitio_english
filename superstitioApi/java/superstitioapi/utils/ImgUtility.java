package superstitioapi.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ImgUtility {
    public static void draw(final SpriteBatch sb, Texture texture, float x, float y,
                            float width, float height, float rotation) {
        sb.draw(texture, x, y, 0, 0, width, height, 1.0f, 1.0f, rotation, 0, 0, texture.getWidth(), texture.getHeight(),
                false, false);
    }
    public static void draw(final SpriteBatch sb, Texture texture, float x, float y,
                            float width, float height, float rotation,boolean flipX,boolean flipY) {
        sb.draw(texture, x, y, 0, 0, width, height, 1.0f, 1.0f, rotation, 0, 0, texture.getWidth(), texture.getHeight(),
                flipX, flipY);
    }
}
