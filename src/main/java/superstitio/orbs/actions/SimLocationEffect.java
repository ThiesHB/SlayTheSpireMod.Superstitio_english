package superstitio.orbs.actions;

import superstitio.orbs.OrbGroup;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SimLocationEffect extends AbstractGameEffect {
    private final Vector2 pos;
    private final Vector2 target;
    private final int index;
    private final OrbGroup targetManager;
    public AbstractOrb orb;

    public SimLocationEffect(final AbstractOrb orb, final int index, final OrbGroup targetManager) {
        this.orb = orb;
        this.pos = new Vector2(orb.cX, orb.cY);
        this.target = new Vector2(targetManager.hitbox.cX, targetManager.hitbox.cY);
        this.index = index;
        this.targetManager = targetManager;
        this.duration = 1.0f;
        this.startingDuration = 1.0f;
        this.color = Color.WHITE.cpy();
    }

    public void update() {
        super.update();
        final float dt = Gdx.graphics.getDeltaTime();
        this.pos.x = Interpolation.exp10Out.apply(this.pos.x, this.target.x, 5.0f * dt);
        this.pos.y = Interpolation.exp10Out.apply(this.pos.y, this.target.y, 5.0f * dt);
        this.orb.cX = this.pos.x;
        this.orb.cY = this.pos.y;
        this.orb.tX = this.orb.cX;
        this.orb.tY = this.orb.cY;
        if (this.pos.dst(this.target) <= 9.0f) {
            this.isDone = true;
        }
        if (!this.isDone) return;

        for (int max = targetManager.GetMaxOrbs(), j = 0; j < max; ++j) {
            targetManager.setSlotPlace(this.orb, this.index);
        }
    }

    public void render(final SpriteBatch sb) {
    }

    public void dispose() {
    }
}
