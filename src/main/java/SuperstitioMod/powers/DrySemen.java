package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class DrySemen extends AbstractLupaPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(DrySemen.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DrySemen(final AbstractCreature owner, int amount) {
        super(POWER_ID,powerStrings,owner,amount);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(DrySemen.powerStrings.DESCRIPTIONS[0]);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, this.amount));
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }
}
