package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.actions.AbstractAutoDoneAction;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.ChokePower;

public class ChokeChoker extends AbstractLupaPower implements NonStackablePower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(ChokeChoker.class.getSimpleName() + "Power");
    public static final int ChokeAmount = 1;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ChokeChoker(final AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings, owner, amount);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(ChokeChoker.powerStrings.DESCRIPTIONS[0], amount, ChokeAmount);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        AddPowers();
    }

    public void AddPowers() {
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new SexualHeatNeededModifier(this.owner, amount)));
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new ChokePower(this.owner, ChokeAmount)));
        this.addToBot(new AbstractAutoDoneAction() {
            @Override
            public void autoDoneUpdate() {
                SexualHeat.ActivateSexualHeat(owner).ifPresent(SexualHeat::CheckOrgasm);
            }
        });
    }
}
