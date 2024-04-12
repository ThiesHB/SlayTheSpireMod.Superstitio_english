package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ChokePower;

import java.util.Objects;
import java.util.Optional;

public class TimeStop extends TwoAmountPower implements  BetterOnApplyPowerPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(TimeStop.class.getSimpleName() + "Power");
    public static final int sexualReturnRate = 2;
    public int sexualHeatOrigin;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public TimeStop(final AbstractCreature owner) {
        this.name = TimeStop.powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.type = PowerType.BUFF;

        this.amount = -1;
        // 添加一大一小两张能力图
        String path128 = SuperstitioModSetup.makeImgFilesPath_Power("default84");
        String path48 = SuperstitioModSetup.makeImgFilesPath_Power("default32");
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();

        Optional<AbstractPower> sexualHeat =
                this.owner.powers.stream().filter(abstractPower -> Objects.equals(abstractPower.ID,
                        SexualHeat.POWER_ID)).findAny();
        sexualHeat.ifPresent(abstractPower -> this.sexualHeatOrigin = abstractPower.amount);

    }


    @Override
    public void updateDescription() {
        this.description = String.format(TimeStop.powerStrings.DESCRIPTIONS[0], amount2, sexualReturnRate);
    }


    @Override
    public void atStartOfTurn() {
        AbstractPower power = new SexualHeat(this.owner, amount2 * sexualReturnRate);
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, power));
    }

    @Override
    public boolean betterOnApplyPower(AbstractPower power, AbstractCreature creature, AbstractCreature creature1) {
        if (power instanceof SexualHeat && power.amount > 0) {
            this.amount2 += power.amount;
        }
        return false;
    }
}
