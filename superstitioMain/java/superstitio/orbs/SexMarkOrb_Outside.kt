package superstitio.orbs

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.orbs.AbstractOrb
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect
import superstitio.DataManager

class SexMarkOrb_Outside @JvmOverloads constructor(sexMarkName: String = "") :
    SexMarkOrb(ORB_ID, TemporaryHPRate, sexMarkName)
{
    private val hFlip1 = MathUtils.randomBoolean()
    private val hFlip2 = MathUtils.randomBoolean()
    private var vfxTimer = 1.0f

    override fun attack(): Int
    {
        return 0
    }

    override fun block(): Int
    {
        return this.evokeAmount
    }

    override fun makeCopy(): AbstractOrb
    {
        return SexMarkOrb_Outside()
    }

    override fun updateAnimation()
    {
        super.updateAnimation()
        this.angle += Gdx.graphics.deltaTime * 180.0f
        this.vfxTimer -= Gdx.graphics.deltaTime
        if (this.vfxTimer < 0.0f)
        {
            AbstractDungeon.effectList.add(FrostOrbPassiveEffect(this.cX, this.cY))
            if (MathUtils.randomBoolean())
            {
                AbstractDungeon.effectList.add(FrostOrbPassiveEffect(this.cX, this.cY))
            }

            val vfxIntervalMin = 0.15f
            val vfxIntervalMax = 0.8f
            this.vfxTimer = MathUtils.random(vfxIntervalMin, vfxIntervalMax)
        }
    }

    override fun triggerEvokeAnimation()
    {
        CardCrawlGame.sound.play("ORB_FROST_EVOKE", 0.1f)
        AbstractDungeon.effectsQueue.add(FrostOrbActivateEffect(this.cX, this.cY))
    }

    override fun render(sb: SpriteBatch)
    {
        sb.color = c
        sb.draw(
            ImageMaster.FROST_ORB_RIGHT,
            this.cX - 48.0f + bobEffect.y / 4.0f,
            this.cY - 48.0f + bobEffect.y / 4.0f,
            48.0f,
            48.0f,
            96.0f,
            96.0f,
            this.scale,
            this.scale,
            0.0f,
            0,
            0,
            96,
            96,
            this.hFlip1,
            false
        )
        sb.draw(
            ImageMaster.FROST_ORB_LEFT,
            this.cX - 48.0f + bobEffect.y / 4.0f,
            this.cY - 48.0f - (bobEffect.y / 4.0f),
            48.0f,
            48.0f,
            96.0f,
            96.0f,
            this.scale,
            this.scale,
            0.0f,
            0,
            0,
            96,
            96,
            this.hFlip1,
            false
        )
        sb.draw(
            ImageMaster.FROST_ORB_MIDDLE,
            this.cX - 48.0f - (bobEffect.y / 4.0f),
            this.cY - 48.0f + bobEffect.y / 2.0f,
            48.0f,
            48.0f,
            96.0f,
            96.0f,
            this.scale,
            this.scale,
            0.0f,
            0,
            0,
            96,
            96,
            this.hFlip2,
            false
        )
        this.renderText(sb)
        hb.render(sb)
    }

    override fun playChannelSFX()
    {
        CardCrawlGame.sound.play("ORB_FROST_CHANNEL", 0.1f)
    }

    companion object
    {
        val ORB_ID: String = DataManager.MakeTextID(SexMarkOrb_Outside::class.java)
        private const val TemporaryHPRate = 2
    }
}
