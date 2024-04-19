package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractLupaPower extends AbstractPower {


    public AbstractLupaPower(String id, String name, final AbstractCreature owner, int amount, PowerType powerType, boolean needUpdateDescription) {
        this.name = name;
        this.ID = id;
        this.owner = owner;

        this.type = powerType;

        this.amount = amount;

        // 添加一大一小两张能力图

        String path128 = makeImgPath(id, IconSize.Big);
        String path48 = makeImgPath(id, IconSize.Small);
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        if (needUpdateDescription)
            this.updateDescription();
    }

    public AbstractLupaPower(String id, String name, final AbstractCreature owner, int amount, PowerType powerType) {
        this(id, name, owner, amount, powerType, true);
    }

    public AbstractLupaPower(String id, PowerStrings powerStrings, final AbstractCreature owner, int amount, PowerType powerType) {
        this(id, powerStrings.NAME, owner, amount, powerType);
    }


    public AbstractLupaPower(String id, PowerStrings powerStrings, final AbstractCreature owner, int amount) {
        this(id, powerStrings, owner, amount, PowerType.BUFF);
    }

    private String makeImgPath(final String id, IconSize size) {
        String path;
        path = SuperstitioModSetup.makeImgFilesPath_Power(id + returnSizeNum(size));
        if (Gdx.files.internal(path).exists())
            return path;
        SuperstitioModSetup.logger.info("Can't find " + id + returnSizeNum(size) + ". Use default img instead.");
        return SuperstitioModSetup.makeImgFilesPath_Power("default" + returnSizeNum(size));
    }

    private String returnSizeNum(IconSize size) {
        return size == IconSize.Big ? "84" : "32";
    }

    private enum IconSize {
        Big,
        Small
    }

    public void addToBot_applyPowerToOwner(final AbstractPower power) {
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, power));
    }

    public void addToBot_reducePowerToOwner(final String powerID, int amount) {
        this.addToBot(new ReducePowerAction(this.owner, this.owner, powerID, amount));
    }
}
