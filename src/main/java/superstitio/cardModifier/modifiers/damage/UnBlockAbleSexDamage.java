package superstitio.cardModifier.modifiers.damage;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.AbstractLupaDamage;
import superstitio.powers.SexualDamage;

public class UnBlockAbleSexDamage extends AbstractLupaDamage {
    public static final String ID = DataManager.MakeTextID(UnBlockAbleSexDamage.class);
    private final static int OnlyDealDamage = 1;

    public UnBlockAbleSexDamage() {
        super(ID);
    }

    @Override
    public boolean ignoresBlock(AbstractCreature target) {
        return true;
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0) {
            this.addToTop(new ApplyPowerAction(target, info.owner, new SexualDamage(target, damageAmount, info.owner)));
        }
        return 0;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new UnBlockAbleSexDamage();
    }
}