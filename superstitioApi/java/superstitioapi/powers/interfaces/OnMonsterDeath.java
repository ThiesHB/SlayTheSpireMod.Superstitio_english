package superstitioapi.powers.interfaces;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 * 可用于power
 */
public class OnMonsterDeath {
    public interface OnMonsterDeathPower {
        /**
         * @return 返回true则不死亡
         */
        boolean ifStopOwnerDeathWhenOwnerIsMonster();
    }

    public interface OnMonsterDeathRelic {
        /**
         * @return 返回true则不死亡
         */
        boolean ifStopMonsterDeath(AbstractMonster monster);
    }

    @SpirePatch(clz = AbstractMonster.class, method = "die", paramtypez = {boolean.class})
    public static class OnMonsterDeathPatch {

        public static SpireReturn<Void> Prefix(final AbstractMonster _inst, boolean triggerRelics) {
            if (_inst.isDying) return SpireReturn.Continue();
            if (!triggerRelics) return SpireReturn.Continue();
            for (AbstractPower power : _inst.powers) {
                if (power instanceof OnMonsterDeath.OnMonsterDeathPower) {
                    if (((OnMonsterDeath.OnMonsterDeathPower) power).ifStopOwnerDeathWhenOwnerIsMonster()) {
                        return SpireReturn.Return();
                    }
                }
            }
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof OnMonsterDeath.OnMonsterDeathRelic) {
                    if (((OnMonsterDeath.OnMonsterDeathRelic) relic).ifStopMonsterDeath(_inst)) {
                        return SpireReturn.Return();
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }
}
