package superstitio.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;

public class Milk extends AbstractSuperstitioPower {
    public static final String POWER_ID = DataManager.MakeTextID(Milk.class);
    public static final int REMOVE_EACH_TIME = 1;

    public Milk(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void updateDescriptionArgs() {
        this.setDescriptionArgs(amount, REMOVE_EACH_TIME);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.NORMAL) return;
//        if (damageAmount <= 0) return;
        this.flash();
        addToBot(new AddTemporaryHPAction(target, owner, this.amount));
        addToBot(new AddTemporaryHPAction(owner, owner, this.amount));
        this.amount = this.amount / 2;
//        addToBot_reducePowerToOwner(Milk.POWER_ID, this.amount / 2);
    }
}
