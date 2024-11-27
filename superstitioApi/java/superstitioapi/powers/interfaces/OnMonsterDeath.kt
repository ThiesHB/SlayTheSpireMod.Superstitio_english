package superstitioapi.powers.interfaces

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster

/**
 * 可用于power
 */
object OnMonsterDeath
{
    interface OnMonsterDeathPower
    {
        /**
         * @return 返回true则不死亡
         */
        fun ifStopOwnerDeathWhenOwnerIsMonster(): Boolean
    }

    interface OnMonsterDeathRelic
    {
        /**
         * @return 返回true则不死亡
         */
        fun ifStopMonsterDeath(monster: AbstractMonster?): Boolean
    }

    @SpirePatch2(clz = AbstractMonster::class, method = "die", paramtypez = [Boolean::class])
    object OnMonsterDeathPatch
    {
        @SpirePrefixPatch
        @JvmStatic
        fun Prefix(__instance: AbstractMonster, triggerRelics: Boolean): SpireReturn<Void>
        {
            if (__instance.isDying) return SpireReturn.Continue()
            if (!triggerRelics) return SpireReturn.Continue()
            for (power in __instance.powers)
            {
                if (power is OnMonsterDeathPower)
                {
                    if ((power as OnMonsterDeathPower).ifStopOwnerDeathWhenOwnerIsMonster())
                    {
                        return SpireReturn.Return()
                    }
                }
            }
            for (relic in AbstractDungeon.player.relics)
            {
                if (relic is OnMonsterDeathRelic)
                {
                    if ((relic as OnMonsterDeathRelic).ifStopMonsterDeath(__instance))
                    {
                        return SpireReturn.Return()
                    }
                }
            }
            return SpireReturn.Continue()
        }
    }
}
