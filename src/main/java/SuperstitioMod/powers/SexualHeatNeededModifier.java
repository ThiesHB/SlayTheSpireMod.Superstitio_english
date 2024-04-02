package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.powers.interFace.OnOrgasm;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SexualHeatNeededModifier extends AbstractPower implements OnOrgasm {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(SexualHeatNeededModifier.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SexualHeatNeededModifier(final AbstractCreature owner, int amount) {
        this.name = SexualHeatNeededModifier.powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        //大于0会降低高潮需求，小于0会提高高潮需求
        this.type = this.amount < 0 ? PowerType.DEBUFF : PowerType.BUFF;
        this.canGoNegative = true;

        this.amount = amount;
        // 添加一大一小两张能力图
        String path128 = SuperstitioModSetup.getImgFilesPath() + "powers/default84.png";
        String path48 = SuperstitioModSetup.getImgFilesPath() + "powers/default32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();

    }


    @Override
    public void updateDescription() {
        this.description = String.format(SexualHeatNeededModifier.powerStrings.DESCRIPTIONS[this.amount < 0 ? 1 : 0], Math.abs(this.amount));
    }

    @Override
    public void onCheckOrgasm(SexualHeat SexualHeatPower) {
        SexualHeatPower.setHeatRequired(SexualHeat.HEAT_REQUIREDOrigin - this.amount);
    }

    @Override
    public void afterOrgasm(SexualHeat SexualHeatPower) {
    }

    @Override
    public boolean shouldOrgasm(SexualHeat SexualHeatPower) {
        return true;
    }

    @Override
    public void beforeSquirt(SexualHeat SexualHeatPower) {

    }
}
