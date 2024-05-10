package superstitio.powers.interfaces.invisible;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface InvisiblePower_InvisibleIconAndAmount {

    static boolean shouldInvisible(AbstractPower power) {
        if (power instanceof InvisiblePower_InvisibleIconAndAmount)
            return ((InvisiblePower_InvisibleIconAndAmount) power).checkShouldInvisibleIconAndAmount();
        return false;
    }

    default boolean checkShouldInvisibleIconAndAmount() {
        return true;
    }
}
