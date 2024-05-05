package superstitio.cards.modifiers.block;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class PregnantBlock_sealPower extends PregnantBlock {
    private final ArrayList<AbstractPower> sealPower;
    private final AbstractCreature sealCreature;

    public PregnantBlock_sealPower(ArrayList<AbstractPower> sealPower, AbstractCreature sealCreature) {
        super();
        this.sealPower = sealPower;
        this.sealCreature = sealCreature;
    }

    ///受到外力则为流产
    @Override
    public int onRemove(boolean lostByStartOfTurn, DamageInfo info, int remainingDamage) {
        if (info != null)
            for (AbstractPower power : sealPower) {
                addToBot(new ApplyPowerAction(this.owner, sealCreature == null ? this.owner : sealCreature, power));
            }
        return super.onRemove(lostByStartOfTurn, info, remainingDamage);
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new PregnantBlock_sealPower(this.sealPower, this.sealCreature);
    }
}