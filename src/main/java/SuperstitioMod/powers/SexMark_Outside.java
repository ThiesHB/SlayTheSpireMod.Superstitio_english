package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class SexMark_Outside extends SexMark {
    public static final String POWER_ID = DataManager.MakeTextID(SexMark_Outside.class.getSimpleName());
    public static final int MARKNeeded = 5;
    public static final int TemporaryHPRate = 2;

    public SexMark_Outside(final AbstractCreature owner, final String sexName) {
        super(POWER_ID, owner, sexName);
    }

    @Override
    protected float Height() {
        return this.owner.hb.cY + SexMark.BAR_RADIUS;
    }

    @Override
    protected void Trigger() {
        super.Trigger();
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new OutsideSemen(this.owner, TemporaryHPRate * FindJobAndFuckCard())));
    }

    @Override
    public void updateDescriptionArgs() {
        StringBuilder names = new StringBuilder();
        for (String sexName : this.sexNames) {
            names.append(sexName);
        }
        setDescriptionArgs(names, MARKNeeded, TemporaryHPRate * FindJobAndFuckCard());
    }
}
