//package superstitioapi.powers.barIndepend;
//
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.megacrit.cardcrawl.helpers.Hitbox;
//
//import java.util.function.Supplier;
//
//public class TextRenderOnThing extends RenderOnThing {
//    public TextRenderOnThing(Supplier<Hitbox> hitbox, HasBarRenderOnCreature power) {
//        super(hitbox, power);
//        this.hitbox.width = BarRenderOnThing_Ring.BAR_SIZE / 2;
//        this.hitbox.height = BarRenderOnThing_Ring.BAR_SIZE / 2;
//        updateHitBoxPlace(this.hitbox);
//    }
//
//    @Override
//    protected float getYDrawStart() {
//        return this.hitbox.cY - this.hitbox.height / 2;
//    }
//
//    @Override
//    protected float getXDrawStart() {
//        return this.hitbox.cX - this.hitbox.width / 2;
//    }
//
//    @Override
//    protected AmountChunk makeNewAmountChunk(BarRenderUpdateMessage message) {
//        return new AmountChunk(getNextOrder()) {
//            @Override
//            public void render(SpriteBatch sb) {
//            }
//
//            @Override
//            public void update() {
//            }
//        };
//    }
//}
