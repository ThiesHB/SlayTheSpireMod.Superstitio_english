package superstitio.orbs.actions

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.megacrit.cardcrawl.orbs.AbstractOrb
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import superstitio.orbs.orbgroup.OrbGroup

class SimLocationEffect(var orb: AbstractOrb, private val index: Int, private val targetManager: OrbGroup) :
    AbstractGameEffect()
{
    private val pos = Vector2(orb.cX, orb.cY)
    private val target = Vector2(targetManager.hitbox.cX, targetManager.hitbox.cY)

    init
    {
        this.duration = 1.0f
        this.startingDuration = 1.0f
        this.color = Color.WHITE.cpy()
    }

    override fun update()
    {
        super.update()
        val dt = Gdx.graphics.deltaTime
        pos.x = Interpolation.exp10Out.apply(pos.x, target.x, 5.0f * dt)
        pos.y = Interpolation.exp10Out.apply(pos.y, target.y, 5.0f * dt)
        orb.cX = pos.x
        orb.cY = pos.y
        orb.tX = orb.cX
        orb.tY = orb.cY
        if (pos.dst(this.target) <= 9.0f)
        {
            this.isDone = true
        }
        if (!this.isDone) return

        val max = targetManager.GetMaxOrbs()
        var j = 0
        while (j < max)
        {
            targetManager.letOrbToSlotPlace(this.orb, this.index)
            ++j
        }
    }

    override fun render(sb: SpriteBatch)
    {
    }

    override fun dispose()
    {
    }
}
