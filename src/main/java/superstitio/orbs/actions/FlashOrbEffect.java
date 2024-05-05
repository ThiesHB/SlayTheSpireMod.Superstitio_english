package superstitio.orbs.actions;

import com.megacrit.cardcrawl.vfx.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.helpers.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.*;

public class FlashOrbEffect extends AbstractGameEffect
{
    private float x;
    private float y;
    private final TextureAtlas.AtlasRegion img;
    private float scale;

    public FlashOrbEffect(final float x, final float y) {
        this.scale = Settings.scale;
        this.x = x;
        this.y = y;
        this.img = ImageMaster.GLOW_SPARK_2;
        this.x -= (float) this.img.packedWidth / 2;
        this.y -= (float) this.img.packedHeight / 2;
        this.duration = 1.0f;
        this.startingDuration = 1.0f;
        this.color = Color.PINK.cpy();
        this.color.a = 0.65f;
        this.renderBehind = true;
    }

    public void update() {
        super.update();
        this.scale = Interpolation.exp5In.apply(Settings.scale * 2.0f, Settings.scale * 0.3f, this.duration / this.startingDuration);
    }

    public void render(final SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * 3.0f, this.scale * 3.0f, 0.0f);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
