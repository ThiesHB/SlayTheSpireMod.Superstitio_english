package superstitioapi.powers.interfaces.invisible;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface InvisiblePower_InvisibleIconAndAmount {

    static boolean shouldInvisibleIcon(AbstractPower power) {
        if (power instanceof InvisiblePower_InvisibleIconAndAmount)
            return ((InvisiblePower_InvisibleIconAndAmount) power).checkShouldInvisibleIcon();
        return false;
    }

    static boolean shouldInvisibleAmount(AbstractPower power) {
        if (power instanceof InvisiblePower_InvisibleIconAndAmount)
            return ((InvisiblePower_InvisibleIconAndAmount) power).checkShouldInvisibleAmount();
        return false;
    }


    default boolean checkShouldInvisibleIcon() {
        return true;
    }

    default boolean checkShouldInvisibleAmount() {
        return true;
    }
}
