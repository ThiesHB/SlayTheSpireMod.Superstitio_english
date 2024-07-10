package superstitioapi.cards;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitioapi.actions.AbstractContinuallyAction;
import superstitioapi.actions.DamageAllEnemiesAction;
import superstitioapi.actions.DamageEnemiesAction;
import superstitioapi.shader.heart.HeartMultiAtOneShader;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import static superstitioapi.shader.ShaderUtility.canUseShader;

/**
 * 这个类其实已经是过度包装了，它本来是原始{@link DamageAction}的builder，但我已经重写了一套新的代码（并且附带了builder）
 * <p>现在它唯一的用处就是实现{@link DamageEffect#HeartMultiInOne} 一类的特效，算是个可有可无的玩意</p>
 */
public class DamageActionMaker {
    private final DamageEnemiesAction.Builder builder;
    private AttackEffect effect = DamageActionMaker.DamageEffect.HeartMultiInOne;

    /**
     * 产生一个AOE伤害
     */
    protected DamageActionMaker(final int[] multiDamage) {
        this.builder = DamageAllEnemiesAction.builder(multiDamage);
    }

    protected DamageActionMaker(final int[] multiDamage, final AbstractMonster... targets) {
        this.builder = DamageEnemiesAction.builder(multiDamage, Arrays.stream(targets).collect(Collectors.toList()));
    }

    protected DamageActionMaker(final int damageAmount, final AbstractCreature target) {
        this.builder = DamageEnemiesAction.builder(damageAmount, target);
    }

    public static DamageActionMaker maker(final int damageAmount, final AbstractCreature target) {
        return new DamageActionMaker(damageAmount, target);
    }

    public static DamageActionMaker makeDamages(final AbstractCard exampleCard, final AbstractMonster... targets) {
        return new DamageActionMaker(exampleCard.multiDamage, targets);
    }

    public static DamageActionMaker makeAoeDamage(final AbstractCard exampleCard) {
        return new DamageActionMaker(exampleCard.multiDamage)
                .setDamageModifier(exampleCard, DamageModifierManager.modifiers(exampleCard).toArray(new AbstractDamageModifier[]{}));
    }

    public AbstractContinuallyAction createAction() {
        if (effect == DamageActionMaker.DamageEffect.HeartMultiInOne) {
            if (canUseShader)
                this.builder.setNewAttackEffectMaker(creature -> new HeartMultiAtOneShader.HeartMultiAtOneEffect(creature.hb));
            else
                this.builder.setAttackEffectType(AttackEffect.BLUNT_LIGHT);
        }
        return this.builder.create();
    }

    public final void addToBot() {
        this.createAction().addToBot();
    }

    public final void addToTop() {
        this.createAction().addToTop();
    }

    public final DamageActionMaker setSource(AbstractCreature source) {
        this.builder.setSource(source);
        return this;
    }

    public final DamageActionMaker setDamageType(DamageInfo.DamageType damageType) {
        this.builder.setDamageType(damageType);
        return this;
    }

    public final DamageActionMaker setEffect(AttackEffect effect) {
        this.effect = effect;
        this.builder.setAttackEffectType(effect);
        return this;
    }

    public final DamageActionMaker setExampleCard(AbstractCard card) {
        return this.setDamageModifier(card, DamageModifierManager.modifiers(card).toArray(new AbstractDamageModifier[]{}));
    }

    public final DamageActionMaker setSkipWait(boolean skipWait) {
        this.builder.setSkipWait(skipWait);
        return this;
    }

    public final DamageActionMaker setDamageModifier(Object instigator, AbstractDamageModifier... damageModifier) {
        if (damageModifier.length > 0)
            this.builder.setDamageModifier(instigator, damageModifier);
        return this;
    }

    public static class DamageEffect {
        @SpireEnum
        public static AttackEffect HeartMultiInOne;
    }
}
