package superstitio.cards;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import superstitio.Logger;
import superstitio.utils.ActionUtility;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;

public class DamageActionMaker {
    private final AbstractCreature[] _targets;
    private final AbstractCreature source;
    public int damageAmount;
    private DamageInfo.DamageType damageType = DamageInfo.DamageType.NORMAL;
    private AttackEffect effect = AttackEffect.BLUNT_LIGHT;
    private boolean superFast = false;
    private Object instigator;
    private List<AbstractDamageModifier> damageModifiers = null;

    public DamageActionMaker(int damageAmount, final AbstractCreature... targets) {
        this(AbstractDungeon.player, damageAmount, targets);
    }

    public DamageActionMaker(final AbstractCreature source, int damageAmount, final AbstractCreature... targets) {
        this._targets = targets;
        this.damageAmount = damageAmount;
        this.source = source;
    }

    public static DamageActionMaker maker(final AbstractCreature source, int damageAmount, final AbstractCreature... targets) {
        return new DamageActionMaker(source, damageAmount, targets);
    }

    public static DamageActionMaker maker(int damageAmount, final AbstractCreature targets) {
        return new DamageActionMaker(damageAmount, targets);
    }

    public static DamageActionMaker maker(int damageAmount, final AbstractCreature[] targets) {
        return new DamageActionMaker(damageAmount, targets);
    }

    public static DamageActionMaker makeAnAoeAction(int damageAmount) {
        return new DamageActionMaker(damageAmount, ActionUtility.getAllAliveMonsters());
    }

    public static DamageActionMaker makeAnAoeAction(final AbstractCreature source, int damageAmount) {
        return new DamageActionMaker(source, damageAmount, ActionUtility.getAllAliveMonsters());
    }

    public static AbstractCreature getTargetOrFirstMonster(AbstractCreature target) {
        if (ActionUtility.isAlive(target)) {
            return target;
        }
        AbstractMonster first = ActionUtility.getAllAliveMonsters()[0];
        if (first != null)
            return first;
        return new ApologySlime();
    }

    public static AbstractMonster getMonsterOrFirstMonster(AbstractCreature target) {
        if (target instanceof AbstractMonster && ActionUtility.isAlive(target)) {
            return (AbstractMonster) target;
        }
        AbstractMonster first = ActionUtility.getAllAliveMonsters()[0];
        if (first != null)
            return first;
        Logger.warning("NoAliveMonsters");
        return new ApologySlime();
    }

    private List<AbstractCreature> getTargets() {
        List<AbstractCreature> alive = Arrays.stream(_targets).filter(ActionUtility::isAlive).collect(Collectors.toList());
        if (alive.isEmpty())
            return Collections.singletonList(ActionUtility.getAllAliveMonsters()[0]);
        return alive;
    }

    public void addToBot() {
        getTargets().forEach(target -> AbstractDungeon.actionManager.addToBottom(this.get(target)));
    }

    public void addToTop() {
        getTargets().forEach(target -> AbstractDungeon.actionManager.addToTop(this.get(target)));
    }

    public DamageActionMaker setDamageType(DamageInfo.DamageType damageType) {
        this.damageType = damageType;
        return this;
    }

    public DamageActionMaker setEffect(AttackEffect effect) {
        this.effect = effect;
        return this;
    }

    public DamageActionMaker setExampleCard(AbstractCard card) {
        this.instigator = card;
        this.damageModifiers = DamageModifierManager.modifiers(card);
        return this;
    }

    public DamageActionMaker setSuperFast(boolean superFast) {
        this.superFast = superFast;
        return this;
    }

    public DamageActionMaker setDamageModifier(Object instigator, AbstractDamageModifier... damageModifier) {
        this.instigator = instigator;
        this.damageModifiers = Arrays.asList(damageModifier);
        return this;
    }

    private DamageAction get(AbstractCreature target) {
        if (damageModifiers != null)
            return new DamageAction(target,
                    BindingHelper.makeInfo(new DamageModContainer(instigator, damageModifiers), source, damageAmount, damageType),
                    effect, superFast);
        return new DamageAction(target,
                new DamageInfo(source, damageAmount, damageType),
                effect, superFast);
    }

    private DamageAction get() {
        if (damageModifiers != null)
            return new DamageAction(getTargets().get(0),
                    BindingHelper.makeInfo(new DamageModContainer(instigator, damageModifiers), source, damageAmount, damageType),
                    effect, superFast);
        return new DamageAction(getTargets().get(0), new DamageInfo(source, damageAmount, damageType), effect, superFast);
    }
}
