package superstitioapi.pet;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_S;
import superstitioapi.Logger;

import java.lang.reflect.Constructor;

public class CopyAndSpawnMonsterUtility {
    public static AbstractMonster motherFuckerWhyIShouldUseThisToCopyMonster(final Class<? extends AbstractMonster> amClass) {
        if (amClass.equals(AcidSlime_S.class)) {
            return new AcidSlime_S(0.0f, 0.0f, 0);
        }
        if (amClass.equals(SpikeSlime_S.class)) {
            return new SpikeSlime_S(0.0f, 0.0f, 0);
        }
        if (amClass.getName().equals("monsters.pet.ScapeGoatPet")) {
            return new ApologySlime();
        }
        final Constructor<?>[] con = amClass.getDeclaredConstructors();
        if (con.length > 0) {
            final Constructor<?> c = con[0];
            try {
                final int paramCt = c.getParameterCount();
                final Class[] params = c.getParameterTypes();
                final Object[] paramz = new Object[paramCt];
                for (int i = 0; i < paramCt; ++i) {
                    final Class param = params[i];
                    if (Integer.TYPE.isAssignableFrom(param)) {
                        paramz[i] = 1;
                    }
                    else if (Boolean.TYPE.isAssignableFrom(param)) {
                        paramz[i] = true;
                    }
                    else if (Float.TYPE.isAssignableFrom(param)) {
                        paramz[i] = 0.0f;
                    }
                }
                return (AbstractMonster)c.newInstance(paramz);
            }
            catch (Exception e) {
                Logger.warning("Error occurred while trying to instantiate class: " + c.getName());
                Logger.warning("Reverting to Apology Slime");
                return new ApologySlime();
            }
        }
        Logger.info("Failed to create monster, returning Apology Slime");
        return new ApologySlime();
    }
}
