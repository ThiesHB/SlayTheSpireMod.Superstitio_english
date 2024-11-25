package superstitio.cardModifier.modifiers.block;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitioapi.cards.DamageActionMaker;

import java.util.ArrayList;

public class PregnantBlock_sealPower extends PregnantBlock {
    public static final String ID = DataManager.MakeTextID(PregnantBlock_sealPower.class);
    public static final int THORNS_RATE = 2;
    public final AbstractCreature sealCreature;
    private final ArrayList<AbstractPower> sealPower;

    public PregnantBlock_sealPower(ArrayList<AbstractPower> sealPower, AbstractCreature sealCreature) {
        super(ID);
        this.sealPower = sealPower;
        this.sealCreature = sealCreature;
    }

    @Override
    public void onThisBlockDamaged(DamageInfo info, int lostAmount) {
        super.onThisBlockDamaged(info, lostAmount);
        if (sealCreature == null || sealCreature.isDeadOrEscaped()) return;
        if (info.type != DamageInfo.DamageType.NORMAL) return;
        DamageActionMaker.maker(lostAmount, sealCreature)
                .setDamageType(DamageInfo.DamageType.THORNS)
                .addToBot();
        super.onAttacked(info, lostAmount);
    }

    @Override
    public String getDescription() {
        if (sealCreature == null)
            return super.getDescription();
        if (sealCreature instanceof AbstractPlayer)
            return super.getDescription() + this.blockStrings.getEXTENDED_DESCRIPTION(0) + this.blockStrings.getEXTENDED_DESCRIPTION(1);
        return super.getDescription() + this.blockStrings.getEXTENDED_DESCRIPTION(0) + sealCreature.name;
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new PregnantBlock_sealPower(this.sealPower, this.sealCreature);
    }

    @Override
    public int removeUnNaturally(DamageInfo info, int remainingDamage) {
        if (sealCreature == null || sealCreature.isDeadOrEscaped()) return super.removeUnNaturally(info, remainingDamage);
        for (AbstractPower power : sealPower) {
            addToBot(new ApplyPowerAction(this.owner, this.owner, power));
        }
        return super.removeUnNaturally(info, remainingDamage);
    }
}