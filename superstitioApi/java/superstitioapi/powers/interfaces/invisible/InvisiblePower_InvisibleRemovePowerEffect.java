//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package superstitioapi.powers.interfaces.invisible;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface InvisiblePower_InvisibleRemovePowerEffect {
    static boolean shouldInvisibleRemovePowerEffect(AbstractPower power) {
        if (power instanceof InvisiblePower_InvisibleRemovePowerEffect)
            return ((InvisiblePower_InvisibleRemovePowerEffect) power).checkShouldInvisibleRemovePowerEffect();
        return false;
    }

    default boolean checkShouldInvisibleRemovePowerEffect() {
        return true;
    }
}

