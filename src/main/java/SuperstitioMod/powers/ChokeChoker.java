package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ChokeChoker extends AbstractPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(ChokeChoker.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ChokeChoker(final AbstractCreature owner) {
        this.name = ChokeChoker.powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.type = PowerType.BUFF;

        this.amount = -1;
        // 添加一大一小两张能力图
        String path128 = SuperstitioModSetup.getImgFilesPath() + "powers/default84.png";
        String path48 = SuperstitioModSetup.getImgFilesPath() + "powers/default32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();

    }


    @Override
    public void updateDescription() {
        this.description = String.format(ChokeChoker.powerStrings.DESCRIPTIONS[0]);
    }
}
