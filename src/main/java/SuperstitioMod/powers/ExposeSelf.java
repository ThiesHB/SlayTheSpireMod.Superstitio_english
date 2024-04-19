package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class ExposeSelf extends AbstractLupaPower implements OnLoseBlockPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(ExposeSelf.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ExposeSelf(final AbstractCreature owner, int amount) {
        super(POWER_ID,powerStrings,owner,amount);

    }


    @Override
    public void updateDescription() {
        this.description = String.format(ExposeSelf.powerStrings.DESCRIPTIONS[0]);
    }


    @Override
    public int onLoseBlock(DamageInfo damageInfo, int i) {
        this.addToBot(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player, new DrySemen(AbstractDungeon.player, i)));
        return i;
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount == 1) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
        }
    }
}
