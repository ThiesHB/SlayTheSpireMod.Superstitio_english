//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package superstitioapi.powers.interfaces.invisible;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface InvisiblePower_InvisibleTips {
    static boolean shouldInvisibleTips(AbstractPower power) {
        if (power instanceof InvisiblePower_InvisibleTips)
            return ((InvisiblePower_InvisibleTips) power).checkShouldInvisibleTips();
        return false;
    }

    default boolean checkShouldInvisibleTips() {
        return true;
    }
}

