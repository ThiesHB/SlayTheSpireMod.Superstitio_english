package superstitio.delayHpLose;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;

import java.util.Optional;

public class DelayHpLosePower_ApplyOnAttacked extends DelayHpLosePower{
    public static final String POWER_ID = DataManager.MakeTextID(DelayHpLosePower_ApplyOnAttacked.class);

    public DelayHpLosePower_ApplyOnAttacked(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public boolean checkShouldInvisibleTips() {
        return false;
    }

    @Override
    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.NORMAL) return;
        Optional<AbstractPower> preventHpLimit = owner.powers.stream().filter(power -> power instanceof IPreventHpLimit).findAny();
        Optional<IPreventHpLimit> iPreventHpLimit = preventHpLimit.map(power -> (IPreventHpLimit) power);
        if (!preventHpLimit.isPresent()) {
            immediate_applyDamage(this);
            return;
        }
        else if (preventHpLimit.get().amount == 0) {
            immediate_applyDamage(this);
            return;
        }
        else {
            iPreventHpLimit.ifPresent(IPreventHpLimit::onPreventHpLimit);
        }
        return;
    }


    @Override
    public void onVictory() {
        immediate_applyDamage(this);
    }

    @Override
    protected int addToBot_removeDelayHpLoss(int amount, boolean removeOther) {
        int lastAmount = amount - this.amount;
        addToBot_reducePowerToOwner(this.ID, amount);
        return lastAmount;
    }

    @Override
    public boolean showDecreaseAmount() {
        return true;
    }

    public interface IPreventHpLimit {
        void onPreventHpLimit();
    }
}
