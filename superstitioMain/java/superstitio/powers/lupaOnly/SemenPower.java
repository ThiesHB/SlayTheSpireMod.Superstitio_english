package superstitio.powers.lupaOnly;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static superstitioapi.utils.ActionUtility.addToBot_reducePower;

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
        addToBot_reducePower(this.getSelf().ID, MathUtils.ceil((float) valueUse / this.getSemenValue()), AbstractDungeon.player, AbstractDungeon.player);
    }

    default int getTotalValue() {
        return getSemenValue() * getSelf().amount;
    }
}
