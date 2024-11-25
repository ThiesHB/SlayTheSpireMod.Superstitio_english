package superstitioapi.actions;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import superstitioapi.utils.ActionUtility.TriFunction;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.megacrit.cardcrawl.cards.DamageInfo.DamageType;

public class DamageEnemiesAction extends AbstractContinuallyAction {
    protected static final float DURATION = 0.1F;
    protected static final float POST_ATTACK_WAIT_DUR = 0.1F;
    protected final Function<Integer, DamageInfo> newDamageInfoMaker;
    protected final boolean skipWait;
    protected final Consumer<DamageEnemiesAction> afterDamageConsumer;
    protected final List<Supplier<AbstractGameEffect>> atStart_additionalEffects = new ArrayList<>();
    protected final List<Function<AbstractCreature, AbstractGameEffect>> atEnd_additionalEffects = new ArrayList<>();
    protected final Function<AbstractCreature, AbstractGameEffect> newAttackEffectMaker;
    protected final Map<AbstractCreature, Integer> targetsDamagesMap;

    public static class Builder {
        protected final List<Supplier<AbstractGameEffect>> atStart_additionalEffects = new ArrayList<>();
        protected final List<Function<AbstractCreature, AbstractGameEffect>> atEnd_additionalEffects = new ArrayList<>();
        protected final Map<AbstractCreature, Integer> targetsDamagesMap;
        protected AbstractCreature source;
        protected DamageType damageType;
        protected boolean skipWait = false;
        protected float duration = DURATION;
        protected Function<Integer, DamageInfo> newDamageInfoMaker;
        protected Function<AbstractCreature, AbstractGameEffect> newAttackEffectMaker;
        protected Consumer<DamageEnemiesAction> afterDamageConsumer;
        protected AttackEffect attackEffectType = AttackEffect.NONE;

        /**
         * @param calculatedDamage 计算后的伤害值
         * @param target           单一目标
         */
        protected Builder(int calculatedDamage, AbstractCreature target) {
            this.source = AbstractDungeon.player;
            this.damageType = DamageType.NORMAL;
            this.newDamageInfoMaker = damage -> new DamageInfo(this.source, damage, this.damageType);
            this.newAttackEffectMaker = creature -> new FlashAtkImgEffect(creature.hb.cX, creature.hb.cY, attackEffectType);
            this.targetsDamagesMap = new HashMap<>();
            this.targetsDamagesMap.put(target, calculatedDamage);
        }

        /**
         * 本初始化只能对 {@link AbstractMonster}起作用
         *
         * @param multiDamages   计算后的复数伤害值，考虑到兼容性，可以直接拿{@link AbstractCard#multiDamage}
         * @param targetMonsters 复数目标
         */
        protected Builder(int[] multiDamages, List<AbstractMonster> targetMonsters) {
            this.source = AbstractDungeon.player;
            this.damageType = DamageType.NORMAL;
            this.newDamageInfoMaker = damage -> new DamageInfo(this.source, damage, this.damageType);
            this.newAttackEffectMaker = creature -> new FlashAtkImgEffect(creature.hb.cX, creature.hb.cY, attackEffectType);
            this.targetsDamagesMap = GetDamageMapFromMultiDamagesOfCard(targetMonsters, multiDamages);
        }

        /**
         * @param targetsDamagesMap 计算后的伤害值
         */
        protected Builder(Map<AbstractCreature, Integer> targetsDamagesMap) {
            this.source = AbstractDungeon.player;
            this.damageType = DamageType.NORMAL;
            this.newDamageInfoMaker = damage -> new DamageInfo(this.source, damage, this.damageType);
            this.newAttackEffectMaker = creature -> new FlashAtkImgEffect(creature.hb.cX, creature.hb.cY, attackEffectType);
            this.targetsDamagesMap = targetsDamagesMap;
        }

        public static Map<AbstractCreature, Integer> GetDamageMapFromMultiDamagesOfCard(List<AbstractMonster> targetMonsters, int[] multiDamages) {
            ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
            Map<AbstractCreature, Integer> targetsDamagesMap = new HashMap<>();
            for (int i = 0; i < Math.min(monsters.size(), multiDamages.length); i++) {
                if (targetMonsters.contains(monsters.get(i)))
                    targetsDamagesMap.put(monsters.get(i), multiDamages[i]);
            }
            return targetsDamagesMap;
        }

        public static int DefaultDamageCalculator(AbstractCreature target, AbstractCreature source, int baseDamage) {
            DamageInfo info = new DamageInfo(source, baseDamage);
            info.applyPowers(source, target);
            return info.output;
        }

        public static int DoNotCalculateDamageCalculator(AbstractCreature target, AbstractCreature source, int damage) {
            return damage;
        }

        public static Map<AbstractCreature, Integer> createDamageMatrix(int baseDamage, AbstractCreature source, List<AbstractCreature> targets,
                                                                        TriFunction<AbstractCreature, AbstractCreature, Integer, Integer> damageCalculator) {
            Map<AbstractCreature, Integer> targetsDamagesMap = new HashMap<>();
            for (AbstractCreature creature : targets) {
                targetsDamagesMap.put(creature, damageCalculator.apply(creature, source, baseDamage));
            }
            return targetsDamagesMap;
        }

        public AbstractContinuallyAction create() {
            return new DamageEnemiesAction(source, targetsDamagesMap, attackEffectType, skipWait, duration, newDamageInfoMaker,
                    newAttackEffectMaker, atStart_additionalEffects, atEnd_additionalEffects, afterDamageConsumer);
        }

        public final Builder setSource(AbstractCreature source) {
            this.source = source;
            return this;
        }

        public final Builder setNewAttackEffectMakerAsMuteSfxFlashAtkImgEffect() {
            this.newAttackEffectMaker = creature -> new FlashAtkImgEffect(creature.hb.cX, creature.hb.cY, attackEffectType, true);
            return this;
        }

        public final Builder setDamageType(DamageType damageType) {
            this.damageType = damageType;
            return this;
        }

        public final Builder setSkipWait(boolean skipWait) {
            this.skipWait = skipWait;
            return this;
        }


        public final Builder setAfterDamageConsumer(Consumer<DamageEnemiesAction> afterDamageConsumer) {
            this.afterDamageConsumer = afterDamageConsumer;
            return this;
        }

        public final Builder setAttackEffectType(AttackEffect attackEffectType) {
            this.attackEffectType = attackEffectType;
            return this;
        }

        public final Builder setNewAttackEffectMaker(Function<AbstractCreature, AbstractGameEffect> newAttackEffectMaker) {
            this.newAttackEffectMaker = newAttackEffectMaker;
            return this;
        }

        @SafeVarargs
        public final Builder setAtStart_additionalEffects(Supplier<AbstractGameEffect>... atStart_additionalEffects) {
            this.atStart_additionalEffects.addAll(Arrays.asList(atStart_additionalEffects));
            return this;
        }

        @SafeVarargs
        public final Builder setAtEnd_additionalEffects(Function<AbstractCreature, AbstractGameEffect>... atEnd_additionalEffects) {
            this.atEnd_additionalEffects.addAll(Arrays.asList(atEnd_additionalEffects));
            return this;
        }

        public final Builder setFast() {
            this.duration = Settings.ACTION_DUR_XFAST;
            return this;
        }

        public final Builder setNewDamageInfoMaker(Function<Integer, DamageInfo> newDamageInfoMaker) {
            this.newDamageInfoMaker = newDamageInfoMaker;
            return this;
        }

        public final Builder setDuration(float duration) {
            this.duration = duration;
            return this;
        }

        public final Builder setDamageModifier(Object instigator, AbstractDamageModifier... damageModifiers) {
            this.newDamageInfoMaker = damageAmount -> BindingHelper.makeInfo(new DamageModContainer(instigator, damageModifiers), source,
                    damageAmount, damageType);
            return this;
        }

        public final void addToBot() {
            this.create().addToBot();
        }

        public final void addToTop() {
            this.create().addToTop();
        }
    }

    protected DamageEnemiesAction(AbstractCreature source, Map<AbstractCreature, Integer> targetsDamagesMap, AttackEffect effect,
                                  boolean skipWait, float duration, Function<Integer, DamageInfo> newDamageInfoMaker,
                                  Function<AbstractCreature, AbstractGameEffect> newAttackEffectMaker,
                                  List<Supplier<AbstractGameEffect>> atStart_additionalEffects,
                                  List<Function<AbstractCreature, AbstractGameEffect>> atEnd_additionalEffects,
                                  Consumer<DamageEnemiesAction> afterDamageConsumer) {
        super(ActionType.DAMAGE, duration);
        this.targetsDamagesMap = Collections.unmodifiableMap(targetsDamagesMap);
        if (targetsDamagesMap.size() == 1)
            this.target = targetsDamagesMap.keySet().stream().findAny().get();
        this.source = source;
        this.skipWait = skipWait;
        this.newDamageInfoMaker = newDamageInfoMaker;
        this.afterDamageConsumer = afterDamageConsumer;
        this.newAttackEffectMaker = newAttackEffectMaker;
        this.atStart_additionalEffects.addAll(atStart_additionalEffects);
        this.atEnd_additionalEffects.addAll(atEnd_additionalEffects);
        this.attackEffect = effect;
    }

    /**
     * 详情请查看{@link Builder#Builder(int, AbstractCreature)}
     */
    public static Builder builder(int damage, AbstractCreature target) {
        return new Builder(damage, target);
    }

    /**
     * 详情请查看{@link Builder#Builder(int, AbstractCreature)}
     */
    public static Builder builder(AbstractCreature source, int damage, AbstractCreature target) {
        return builder(damage, target).setSource(source);
    }

    /**
     * 详情请查看{@link Builder#Builder(Map)}
     */
    public static Builder builder(Map<AbstractCreature, Integer> targetsDamagesMap) {
        return new Builder(targetsDamagesMap);
    }

    /**
     * 详情请查看{@link Builder#Builder(Map)}
     */
    public static Builder builder(AbstractCreature source, Map<AbstractCreature, Integer> targetsDamagesMap) {
        return builder(targetsDamagesMap).setSource(source);
    }

    /**
     * 详情请查看{@link Builder#Builder(int[], List)}
     */
    public static Builder builder(int[] multiDamages, List<AbstractMonster> targetMonsters) {
        return new Builder(multiDamages, targetMonsters);
    }

    /**
     * 详情请查看{@link Builder#Builder(int[], List)}
     */
    public static Builder builder(AbstractCreature source, int[] multiDamages, List<AbstractMonster> targetMonsters) {
        return builder(multiDamages, targetMonsters).setSource(source);
    }

    @Override
    protected void StartAction() {
        for (Supplier<AbstractGameEffect> additionalEffect : atStart_additionalEffects)
            AbstractDungeon.effectList.add(additionalEffect.get());
        targetsDamagesMap.forEach((creature, damage) -> {
            AbstractDungeon.effectList.add(newAttackEffectMaker.apply(creature));
        });
    }


    @Override
    protected void RunAction() {
    }

    @Override
    protected void EndAction() {
        targetsDamagesMap.forEach((creature, damage) -> {
            for (Function<AbstractCreature, AbstractGameEffect> action : atEnd_additionalEffects)
                AbstractDungeon.effectList.add(action.apply(creature));
            creature.damage(newDamageInfoMaker.apply(damage));
        });

        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) AbstractDungeon.actionManager.clearPostCombatActions();

        if (afterDamageConsumer != null) afterDamageConsumer.accept(this);

        if (!this.skipWait && !Settings.FAST_MODE) this.addToTop(new WaitAction(POST_ATTACK_WAIT_DUR));
    }


    @Override
    protected boolean isDoneCheck() {
        if (this.damageType == DamageType.THORNS) return false;
        return this.source == null || this.source.isDying || this.source.halfDead;
    }
}
