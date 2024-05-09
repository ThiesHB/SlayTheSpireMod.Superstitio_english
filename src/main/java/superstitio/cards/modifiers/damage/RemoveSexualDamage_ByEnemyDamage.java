package superstitio.cards.modifiers.damage;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.cards.modifiers.AbstractLupaDamage;
import superstitio.utils.ActionUtility;

public class RemoveSexualDamage_ByEnemyDamage extends AbstractLupaDamage {
    public static final String ID = DataManager.MakeTextID(RemoveSexualDamage_ByEnemyDamage.class.getSimpleName());
    private ActionUtility.VoidSupplier actionWhenAttack;

    public RemoveSexualDamage_ByEnemyDamage() {
        super(ID);
    }

    public RemoveSexualDamage_ByEnemyDamage(ActionUtility.VoidSupplier actionWhenAttack) {
        super(ID);
        this.actionWhenAttack = actionWhenAttack;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if (actionWhenAttack != null)
            actionWhenAttack.get();
    }

    @Override
    public boolean ignoresBlock(AbstractCreature target) {
        return true;
    }

    @Override
    public boolean ignoresTempHP(AbstractCreature target) {
        return true;
    }

    @Override
    public boolean ignoresThorns() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new RemoveSexualDamage_ByEnemyDamage();
    }
}