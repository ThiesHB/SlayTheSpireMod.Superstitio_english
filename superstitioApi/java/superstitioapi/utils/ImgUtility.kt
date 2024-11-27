package superstitioapi.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

object ImgUtility
{
    val nullColor: Color = Color(0f, 0f, 0f, 0f)

    fun draw(
        sb: SpriteBatch, texture: Texture, x: Float, y: Float,
        width: Float, height: Float, rotation: Float
    )
    {
        sb.draw(
            texture, x, y, 0f, 0f, width, height, 1.0f, 1.0f, rotation, 0, 0, texture.width, texture.height,
            false, false
        )
    }

    fun draw(
        sb: SpriteBatch, texture: Texture, x: Float, y: Float,
        width: Float, height: Float, rotation: Float, flipX: Boolean, flipY: Boolean
    )
    {
        sb.draw(
            texture, x, y, 0f, 0f, width, height, 1.0f, 1.0f, rotation, 0, 0, texture.width, texture.height,
            flipX, flipY
        )
    }

    fun mixColor(colorBase: Color, colorInsert: Color, InsertRate: Float): Color
    {
        val r = (colorBase.r + colorInsert.r * InsertRate) / (1.0f + InsertRate)
        val g = (colorBase.g + colorInsert.g * InsertRate) / (1.0f + InsertRate)
        val b = (colorBase.b + colorInsert.b * InsertRate) / (1.0f + InsertRate)
        val a = (colorBase.a + colorInsert.a * InsertRate) / (1.0f + InsertRate)
        return Color(r, g, b, a)
    }

    fun mixColor(colorBase: Color, colorInsert: Color, InsertRate: Float, alpha: Float): Color
    {
        val r = (colorBase.r + colorInsert.r * InsertRate) / (1.0f + InsertRate)
        val g = (colorBase.g + colorInsert.g * InsertRate) / (1.0f + InsertRate)
        val b = (colorBase.b + colorInsert.b * InsertRate) / (1.0f + InsertRate)
        return Color(r, g, b, alpha)
    }
}
