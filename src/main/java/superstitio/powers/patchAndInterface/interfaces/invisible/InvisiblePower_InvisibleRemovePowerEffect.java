//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package superstitio.powers.patchAndInterface.interfaces.invisible;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface InvisiblePower_InvisibleRemovePowerEffect {
    static boolean shouldInvisible(AbstractPower power) {
        if (power instanceof InvisiblePower_InvisibleRemovePowerEffect)
            return ((InvisiblePower_InvisibleRemovePowerEffect) power).checkShouldInvisibleRemovePowerEffect();
        return false;
    }

    default boolean checkShouldInvisibleRemovePowerEffect() {
        return true;
    }
}

