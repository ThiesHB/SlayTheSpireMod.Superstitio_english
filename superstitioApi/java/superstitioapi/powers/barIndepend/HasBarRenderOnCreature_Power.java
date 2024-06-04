package superstitioapi.powers.barIndepend;

import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface HasBarRenderOnCreature_Power extends HasBarRenderOnCreature {

    AbstractPower getSelf();

    default BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, ? extends RenderOnThing> makeNewBarRenderOnCreature() {
        return BarRenderOnThing::new;
    }

    @Override
    default String uuidOfSelf() {
        return getSelf().ID;
    }

    @Override
    default String uuidPointTo() {
        return HasBarRenderOnCreature.super.uuidPointTo() + ":" + this.getSelf().owner.id;
    }

    @Override
    default int getAmountForDraw() {
        return getSelf().amount;
    }

    int maxBarAmount();

    @Override
    default String getName() {
        return getSelf().name;
    }

    @Override
    default String getDescription() {
        return getSelf().description;
    }

    @Override
    default Hitbox getBarRenderHitBox() {
        return getSelf().owner.hb;
    }
}
