//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package superstitio.powers.interfaces.invisible;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface InvisiblePower_InvisibleTips {
    static boolean shouldInvisible(AbstractPower power) {
        if (power instanceof InvisiblePower_InvisibleTips)
            return ((InvisiblePower_InvisibleTips) power).checkShouldInvisibleTips();
        return false;
    }

    default boolean checkShouldInvisibleTips() {
        return true;
    }
}

