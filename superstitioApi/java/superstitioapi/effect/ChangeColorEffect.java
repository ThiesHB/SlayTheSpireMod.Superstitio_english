package superstitioapi.effect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ChangeColorEffect extends AbstractGameEffect {

    private final AbstractCreature creature;
    private final Color colorChangeTo;

    public ChangeColorEffect(AbstractCreature creature, Color color) {
        this.creature = creature;
        colorChangeTo = color;
    }

    @Override
    public void update() {
        creature.tint.color.set(colorChangeTo);
        creature.tint.changeColor(Color.WHITE.cpy());
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
    }

    @Override
    public void dispose() {
    }
}
