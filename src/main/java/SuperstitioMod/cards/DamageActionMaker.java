package SuperstitioMod.cards;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Arrays;

public class DamageActionMaker {
    private final AbstractCreature[] _targets;
    public int damageAmount;
    private AbstractCreature source = AbstractDungeon.player;
    private DamageInfo.DamageType damageType = DamageInfo.DamageType.NORMAL;
    private AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
    private boolean superFast = false;
    private Object instigator;
    private AbstractDamageModifier damageModifier = null;

    public DamageActionMaker(int damageAmount, final AbstractCreature... targets) {
        this._targets = targets;
        this.damageAmount = damageAmount;
    }

    public static DamageActionMaker make(int damageAmount, final AbstractCreature... targets) {
        return new DamageActionMaker(damageAmount, targets);
    }

    private AbstractCreature[] getTargets() {
        if (_targets.length == 0)
            return new AbstractCreature[]{AbstractDungeon.player};
        return _targets;
    }

    public void addToBot() {
        Arrays.stream(getTargets()).forEach(target -> AbstractDungeon.actionManager.addToBottom(this.get(target)));
    }

    public DamageActionMaker setupSource(AbstractCreature source) {
        this.source = source;
        return this;
    }

    public DamageActionMaker setupDamageType(DamageInfo.DamageType damageType) {
        this.damageType = damageType;
        return this;
    }

    public DamageActionMaker setupEffect(AbstractGameAction.AttackEffect effect) {
        this.effect = effect;
        return this;
    }

    public DamageActionMaker setupSuperFast(boolean superFast) {
        this.superFast = superFast;
        return this;
    }

    public DamageActionMaker setupDamageModifier(Object instigator, AbstractDamageModifier damageModifier) {
        this.instigator = instigator;
        this.damageModifier = damageModifier;
        return this;
    }

    private DamageAction get(AbstractCreature target) {
        if (damageModifier != null)
            return new DamageAction(target,
                    BindingHelper.makeInfo(new DamageModContainer(instigator, damageModifier), source, damageAmount, damageType),
                    effect, superFast);
        return new DamageAction(target, new DamageInfo(source, damageAmount, damageType), effect, superFast);
    }

    private DamageAction get() {
        if (damageModifier != null)
            return new DamageAction(getTargets()[0],
                    BindingHelper.makeInfo(new DamageModContainer(instigator, damageModifier), source, damageAmount, damageType),
                    effect, superFast);
        return new DamageAction(getTargets()[0], new DamageInfo(source, damageAmount, damageType), effect, superFast);
    }
}
