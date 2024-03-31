package SuperstitioMod.utils;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect;

public class PowerUtility {
    public static void BubbleMessage(AbstractPower power, boolean isDeBuffVer, String message) {
        if (isDeBuffVer) {
            AbstractDungeon.effectList.add(new PowerDebuffEffect(power.owner.hb.cX - power.owner.animX,
                    power.owner.hb.cY + power.owner.hb.height / 2.0f, message));
        } else {
            AbstractDungeon.effectList.add(new PowerBuffEffect(power.owner.hb.cX - power.owner.animX,
                    power.owner.hb.cY + power.owner.hb.height / 2.0f, message));
        }
    }
}
