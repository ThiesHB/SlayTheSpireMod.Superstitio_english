package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Objects;
import java.util.Optional;

public class DelaySexualHeat extends AbstractLupaPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(DelaySexualHeat.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DelaySexualHeat(final AbstractCreature owner,int amount) {
        super(POWER_ID,powerStrings,owner,amount);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(DelaySexualHeat.powerStrings.DESCRIPTIONS[0], amount);
    }


    @Override
    public void atStartOfTurn() {
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new SexualHeat(this.owner, amount)));
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

}
