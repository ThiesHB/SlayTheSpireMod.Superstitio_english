package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class Overdraft extends AbstractLupaPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(Overdraft.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public Overdraft(final AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings, owner, amount);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(Overdraft.powerStrings.DESCRIPTIONS[0], amount);
    }


    @Override
    public void atStartOfTurnPostDraw() {
        this.addToBot(new ExhaustAction(amount, false, false));
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }
}
