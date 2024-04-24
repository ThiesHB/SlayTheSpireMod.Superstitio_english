package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SexMark_Outside extends SexMark {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(SexMark_Outside.class.getSimpleName() +
            "Power");
    public static final int MARKNeeded = 5;

    public static final int TemporaryHPRate = 2;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SexMark_Outside(final AbstractCreature owner, final String sexName) {
        super(powerStrings.NAME, POWER_ID, owner, sexName);
    }

    @Override
    protected float Height() {
        return this.owner.hb.cY + SexMark.BAR_RADIUS;
    }

    @Override
    protected void Trigger() {
        super.Trigger();
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new DrySemen(this.owner, TemporaryHPRate * FindJobAndFuckCard())));
    }

    @Override
    public void updateDescription() {
        StringBuilder names = new StringBuilder();
        for (String sexName : this.sexNames) {
            names.append(sexName);
        }
        this.description = String.format(SexMark_Outside.powerStrings.DESCRIPTIONS[0], names, MARKNeeded, TemporaryHPRate * FindJobAndFuckCard());
    }
}
