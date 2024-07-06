//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package superstitioapi.actions;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 参数调用顺序：
 * <p>在 {@link #StartAction()} 中：</p>
 * {@link #atStart_additionalEffects} —— {@link #newAttackEffectMaker}
 * <p>在 {@link #EndAction()} ()} 中：</p>
 * {@link #atEnd_additionalEffects} —— {@link #newDamageInfoMaker} —— {@link #afterDamageConsumer}
 */
public class DamageAllEnemiesAction extends DamageEnemiesAction {

    private final int[] multiDamages;

    /**
     * {@link Builder#newDamageInfoMaker}: 默认值为新建一个标准DamageInfo
     * {@link Builder#newAttackEffectMaker}: 默认值为新建一个标准FlashAtkImgEffect
     */
    public static class Builder extends DamageEnemiesAction.Builder {
        private final int[] multiDamages;

        protected Builder(int[] multiDamages) {
            super(GetDamageMapFromMultiDamagesOfCard(multiDamages));
            this.multiDamages = multiDamages;
        }

        public static Map<AbstractCreature, Integer> GetDamageMapFromMultiDamagesOfCard(int[] multiDamages) {
            ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
            Map<AbstractCreature, Integer> targetsDamagesMap = new HashMap<>();
            for (int i = 0; i < Math.min(monsters.size(), multiDamages.length); i++) {
                targetsDamagesMap.put(monsters.get(i), multiDamages[i]);
            }
            return targetsDamagesMap;
        }

        @Override
        public AbstractContinuallyAction create() {
            return new DamageAllEnemiesAction(source, targetsDamagesMap, multiDamages, attackEffectType, skipWait, duration, newDamageInfoMaker,
                    newAttackEffectMaker, atStart_additionalEffects, atEnd_additionalEffects, afterDamageConsumer);
        }
    }

    protected DamageAllEnemiesAction(AbstractCreature source, Map<AbstractCreature, Integer> targetsDamagesMap, int[] multiDamages,
                                     AttackEffect attackEffect, boolean skipWait, float duration,
                                     Function<Integer, DamageInfo> newDamageInfoMaker,
                                     Function<AbstractCreature, AbstractGameEffect> newAttackEffectMaker,
                                     List<Supplier<AbstractGameEffect>> atStart_additionalEffects,
                                     List<Function<AbstractCreature, AbstractGameEffect>> atEnd_additionalEffects,
                                     Consumer<DamageEnemiesAction> afterDamageConsumer) {
        super(source, targetsDamagesMap, attackEffect, skipWait, duration, newDamageInfoMaker,
                newAttackEffectMaker, atStart_additionalEffects, atEnd_additionalEffects, afterDamageConsumer);
        this.multiDamages = multiDamages;
    }

    /**
     * 详情请查看{@link Builder}
     */
    public static DamageEnemiesAction.Builder builder(int[] damages) {
        return new Builder(damages);
    }

    /**
     * 详情请查看{@link Builder}
     */
    public static DamageEnemiesAction.Builder builder(AbstractCreature source, int[] damages) {
        return builder(damages).setSource(source);
    }

    @Override
    protected void EndAction() {
        for (AbstractPower p : AbstractDungeon.player.powers) {
            p.onDamageAllEnemies(this.multiDamages);
        }

        super.EndAction();
    }
}
