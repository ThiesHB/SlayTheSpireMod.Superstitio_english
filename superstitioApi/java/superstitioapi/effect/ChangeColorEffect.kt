package superstitioapi.effect

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.vfx.AbstractGameEffect

class ChangeColorEffect(private val creature: AbstractCreature, private val colorChangeTo: Color) :
    AbstractGameEffect()
{
    override fun update()
    {
        creature.tint.color.set(colorChangeTo)
        creature.tint.changeColor(Color.WHITE.cpy())
        this.isDone = true
    }

    override fun render(spriteBatch: SpriteBatch)
    {
    }

    override fun dispose()
    {
    }
}
