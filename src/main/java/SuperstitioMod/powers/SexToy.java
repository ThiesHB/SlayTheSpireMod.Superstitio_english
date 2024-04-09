package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.utils.PowerUtility;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashMap;
import java.util.Map;

public class SexToy extends AbstractPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(SexToy.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int SEXUAL_HEAT_RATE = 5;
    public Map<String, Integer> sexToyNames;

    public SexToy(final AbstractCreature owner, int amount, String sexToyName) {
        this.name = SexToy.powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        this.sexToyNames = new HashMap<>();
        this.sexToyNames.put(sexToyName, amount);

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
        this.description = String.format(SexToy.powerStrings.DESCRIPTIONS[0], SEXUAL_HEAT_RATE);
        this.sexToyNames.forEach((name, num) ->
                this.description = this.description + String.format(SexToy.powerStrings.DESCRIPTIONS[1], name, num));
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new SexualHeat(this.owner, this.amount / SEXUAL_HEAT_RATE)));
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (!(power instanceof SexToy)) {
            return;
        }
        this.sexToyNames = PowerUtility.mergeAndSumMaps(this.sexToyNames, ((SexToy)power).sexToyNames);
        updateDescription();
    }
}
