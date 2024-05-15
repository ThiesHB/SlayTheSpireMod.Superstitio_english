package superstitio.cardModifier.modifiers.block;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.cards.DamageActionMaker;

import java.util.ArrayList;

public class PregnantBlock_sealPower extends PregnantBlock {
    private final ArrayList<AbstractPower> sealPower;
    private final AbstractCreature sealCreature;

    public PregnantBlock_sealPower(ArrayList<AbstractPower> sealPower, AbstractCreature sealCreature) {
        super();
        this.sealPower = sealPower;
        this.sealCreature = sealCreature;
    }

    @Override
    public void onAttacked(DamageInfo info, int damageAmount) {
        DamageActionMaker.maker(Math.min(damageAmount, getCurrentAmount()), sealCreature).addToBot();
        super.onAttacked(info, damageAmount);
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new PregnantBlock_sealPower(this.sealPower, this.sealCreature);
    }

    @Override
    public void removeUnNaturally(DamageInfo info, int remainingDamage) {

        for (AbstractPower power : sealPower) {
            addToBot(new ApplyPowerAction(this.owner, sealCreature == null ? this.owner : sealCreature, power));
        }
    }

    @Override
    public void removeNaturally(DamageInfo info, int remainingDamage) {
    }
}