package superstitio.orbs

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.orbs.AbstractOrb
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect
import com.megacrit.cardcrawl.vfx.combat.LightningOrbPassiveEffect
import superstitio.DataManager

class SexMarkOrb_Inside @JvmOverloads constructor(sexMarkName: String = "") :
    SexMarkOrb(ORB_ID, AOEDamageRate, sexMarkName)
{
    private var vfxTimer = 1.0f

    init
    {
        this.img = ImageMaster.ORB_LIGHTNING

        this.angle = MathUtils.random(360.0f)
        this.channelAnimTimer = 0.5f
    }

    override fun attack(): Int
    {
        return this.evokeAmount
    }

    override fun block(): Int
    {
        return 0
    }

    override fun makeCopy(): AbstractOrb
    {
        return SexMarkOrb_Inside()
    }

    override fun triggerEvokeAnimation()
    {
        CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1f)
        AbstractDungeon.effectsQueue.add(LightningOrbActivateEffect(this.cX, this.cY))
    }

    override fun updateAnimation()
    {
        super.updateAnimation()
        this.angle += Gdx.graphics.deltaTime * 180.0f
        this.vfxTimer -= Gdx.graphics.deltaTime
        if (this.vfxTimer < 0.0f)
        {
            AbstractDungeon.effectList.add(LightningOrbPassiveEffect(this.cX, this.cY))
            if (MathUtils.randomBoolean())
            {
                AbstractDungeon.effectList.add(LightningOrbPassiveEffect(this.cX, this.cY))
            }

            this.vfxTimer = MathUtils.random(0.15f, 0.8f)
        }
    }

    override fun render(sb: SpriteBatch)
    {
        shineColor.a = c.a / 2.0f
        sb.color = shineColor
        sb.setBlendFunction(770, 1)
        sb.draw(
            this.img,
            this.cX - 48.0f,
            this.cY - 48.0f + bobEffect.y,
            48.0f,
            48.0f,
            96.0f,
            96.0f,
            this.scale + (MathUtils.sin(this.angle / PI_4) * ORB_WAVY_DIST) + PI_DIV_16,
            this.scale * ORB_BORDER_SCALE,
            this.angle,
            0,
            0,
            96,
            96,
            false,
            false
        )
        sb.draw(
            this.img,
            this.cX - 48.0f,
            this.cY - 48.0f + bobEffect.y,
            48.0f,
            48.0f,
            96.0f,
            96.0f,
            this.scale * ORB_BORDER_SCALE,
            this.scale + (MathUtils.sin(this.angle / PI_4) * ORB_WAVY_DIST) + PI_DIV_16,
            -this.angle,
            0,
            0,
            96,
            96,
            false,
            false
        )
        sb.setBlendFunction(770, 771)
        sb.color = c
        sb.draw(
            this.img,
            this.cX - 48.0f,
            this.cY - 48.0f + bobEffect.y,
            48.0f,
            48.0f,
            96.0f,
            96.0f,
            this.scale,
            this.scale,
            this.angle / 12.0f,
            0,
            0,
            96,
            96,
            false,
            false
        )
        this.renderText(sb)
        hb.render(sb)
    }

    override fun playChannelSFX()
    {
        CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1f)
    }

    companion object
    {
        val ORB_ID: String = DataManager.MakeTextID(SexMarkOrb_Inside::class.java)
        private const val AOEDamageRate = 4
        private const val PI_DIV_16 = 0.19634955f
        private const val ORB_WAVY_DIST = 0.05f
        private const val PI_4 = 12.566371f
        private const val ORB_BORDER_SCALE = 1.2f
    }
}
