package superstitio.orbs.actions

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.math.Interpolation
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.vfx.AbstractGameEffect

class FlashOrbEffect(x: Float, y: Float) : AbstractGameEffect()
{
    private val img: AtlasRegion
    private var x: Float
    private var y: Float
    var scale: Float = 0.0f

    init
    {
        this.scale = Settings.scale
        this.x = x
        this.y = y
        this.img = ImageMaster.GLOW_SPARK_2
        this.x -= img.packedWidth.toFloat() / 2
        this.y -= img.packedHeight.toFloat() / 2
        this.duration = 1.0f
        this.startingDuration = 1.0f
        this.color = Color.PINK.cpy()
        color.a = 0.65f
        this.renderBehind = true
    }

    override fun update()
    {
        super.update()
        this.scale = Interpolation.exp5In.apply(
            Settings.scale * 2.0f,
            Settings.scale * 0.3f,
            this.duration / this.startingDuration
        )
    }

    override fun render(sb: SpriteBatch)
    {
        sb.setBlendFunction(770, 1)
        sb.color = color
        sb.draw(
            this.img, this.x, this.y,
            img.packedWidth / 2.0f,
            img.packedHeight / 2.0f,
            img.packedWidth.toFloat(),
            img.packedHeight.toFloat(), this.scale * 3.0f, this.scale * 3.0f, 0.0f
        )
        sb.setBlendFunction(770, 771)
    }

    override fun dispose()
    {
    }
}
