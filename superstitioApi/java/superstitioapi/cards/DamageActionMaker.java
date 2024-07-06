package superstitioapi.cards;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
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

    //    public static NewDamageActionMaker maker(final AbstractCreature source, int damageAmount, final AbstractCreature target) {
//        return new NewDamageActionMaker(damageAmount, target).setSource(source);
//    }
    public static DamageActionMaker maker(final int damageAmount, final AbstractCreature target) {
        return new DamageActionMaker(damageAmount, target);
    }

    //    public static NewDamageActionMaker maker(final int[] multiDamage, final AbstractMonster... targets) {
//        return new NewDamageActionMaker(multiDamage, targets);
//    }
    public static DamageActionMaker makeDamages(final AbstractCard exampleCard, final AbstractMonster... targets) {
        return new DamageActionMaker(exampleCard.multiDamage, targets);
    }

//    public static NewDamageActionMaker maker(int damageAmount, final AbstractCreature[] targets) {
//        return new NewDamageActionMaker(damageAmount, targets);
//    }

    public static DamageActionMaker makeAoeDamage(final AbstractCard exampleCard) {
        return new DamageActionMaker(exampleCard.multiDamage);
    }

//    public static NewDamageActionMaker makeAnAoeAction(final AbstractCreature source, int[] multiDamage) {
//        return new NewDamageActionMaker(source, multiDamage);
//    }

    public AbstractContinuallyAction createAction() {
        if (effect == DamageActionMaker.DamageEffect.HeartMultiInOne)
            this.builder.setNewAttackEffectMaker(creature -> new HeartMultiAtOneShader.HeartMultiAtOneEffect(creature.hb));
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

//    private AbstractGameAction get(AbstractCreature target) {
//        return builder.create();
//    }

//    private DamageInfo makeDamageInfo() {
//        if (damageModifiers != null)
//            return BindingHelper.makeInfo(new DamageModContainer(instigator, damageModifiers), source, damageAmount, damageType);
//        else return new DamageInfo(source, damageAmount, damageType);
//    }

    public static class DamageEffect {
        @SpireEnum
        public static AttackEffect HeartMultiInOne;
    }
}
