package superstitio.powers.lupaOnly;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.function.Function;

import static superstitioapi.utils.ActionUtility.*;

public interface SemenPower {
    int getSemenValue();

    AbstractPower getSelf();

    /*
     * 3精液价值为3-4腐朽移除，4-5格挡
     */


    default int compareTo(SemenPower other) {
        return Integer.compare(this.getSemenValue(), other.getSemenValue());
    }

    default void addToBot_UseValue(int valueUse) {
        addToBot_reducePower(this.getSelf().ID, MathUtils.ceil((float) valueUse / this.getSemenValue()), AbstractDungeon.player,
                AbstractDungeon.player);
    }

    default void transToOtherSemen(Function<Integer, AbstractPower> toOtherSemen) {
        AbstractPower SemenTo = toOtherSemen.apply(getSelf().amount);
        if (!(SemenTo instanceof SemenPower)) return;
        addToBot_removeSpecificPower(getSelf().ID, getSelf().owner, getSelf().owner);
        addToBot_applyPower(SemenTo);
    }

    default int getTotalValue() {
        return getSemenValue() * getSelf().amount;
    }
}
