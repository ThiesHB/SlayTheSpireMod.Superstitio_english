package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ChokePower;

public class ChokeChoker extends AbstractPower implements NonStackablePower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(ChokeChoker.class.getSimpleName() + "Power");
    public static final int ChokeAmount = 3;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ChokeChoker(final AbstractCreature owner, int amount) {
        this.name = ChokeChoker.powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.type = PowerType.BUFF;

        this.amount = amount;
        // 添加一大一小两张能力图
        String path128 = SuperstitioModSetup.makeImgFilesPath_Power("default84");
        String path48 = SuperstitioModSetup.makeImgFilesPath_Power("default32");
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();

    }


    @Override
    public void updateDescription() {
        this.description = String.format(ChokeChoker.powerStrings.DESCRIPTIONS[0],amount,ChokeAmount);
    }

    @Override
    public void atStartOfTurn() {
        AddPowers();
    }

    public void AddPowers(){
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new SexualHeatNeededModifier(this.owner, amount)));
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new ChokePower(this.owner, ChokeAmount)));
    }
}
