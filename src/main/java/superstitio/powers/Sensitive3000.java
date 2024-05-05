package superstitio.powers;

import superstitio.DataManager;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class Sensitive3000 extends AbstractLupaPower implements BetterOnApplyPowerPower {
    public static final String POWER_ID = DataManager.MakeTextID(Sensitive3000.class.getSimpleName());
    private static final int SexualHeatRate = 3000;

    public Sensitive3000(final AbstractCreature owner) {
        super(POWER_ID, owner, -1);
    }

    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public boolean betterOnApplyPower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (abstractPower instanceof SexualHeat)
            abstractPower.amount *= SexualHeatRate;
        addToBot_applyPowerToOwner(new SexualDamage(this.owner, abstractPower.amount, this.owner));
        return true;
    }
}
