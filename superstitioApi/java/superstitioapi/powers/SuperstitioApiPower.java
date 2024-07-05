package superstitioapi.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitioapi.DataUtility;
import superstitioapi.utils.ActionUtility;
import superstitioapi.utils.UpdateDescriptionAdvanced;

public abstract class SuperstitioApiPower extends AbstractPower implements UpdateDescriptionAdvanced {
    public static final String DEFAULT = "default";
    private Object[] descriptionArgs;

    public SuperstitioApiPower(String id, PowerStrings powerStrings, final AbstractCreature owner, int amount, PowerType powerType,
                               boolean needUpdateDescription) {
        SetupPower(id, powerStrings, owner, amount, powerType, needUpdateDescription);
    }

    public SuperstitioApiPower(String id, final AbstractCreature owner, int amount, PowerType powerType, boolean needUpdateDescription) {
        this(id, SuperstitioApiPower.getPowerStrings(id), owner, amount, powerType, needUpdateDescription);
    }

    public SuperstitioApiPower(String id, final AbstractCreature owner, int amount, PowerType powerType) {
        this(id, owner, amount, powerType, true);
    }

    public SuperstitioApiPower(String id, final AbstractCreature owner, int amount) {
        this(id, owner, amount, PowerType.BUFF);
    }

    public static PowerStrings getPowerStrings(String powerID) {
        return CardCrawlGame.languagePack.getPowerStrings(powerID);
    }

    @Override
    public Object[] getDescriptionArgs() {
        return descriptionArgs;
    }

    protected void SetupPower(String id, PowerStrings powerStrings, final AbstractCreature owner, int amount, PowerType powerType,
                              boolean needUpdateDescription) {
        this.name = powerStrings.NAME;
        this.ID = id;
        this.owner = owner;
        this.type = powerType;
        this.amount = amount;
        String path128 = makeImgPath(id, IconSize.Big);
        String path48 = makeImgPath(id, IconSize.Small);
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        if (needUpdateDescription)
            this.updateDescription();
    }

    protected void renderAmount2(SpriteBatch sb, float x, float y, Color c, int amount2) {
        if (amount2 <= 0) return;
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "" + amount2, x, y + 15.0F * Settings.scale, this.fontScale, c);
    }

    private String makeImgPath(final String id, SuperstitioApiPower.IconSize size) {
        return DataUtility.makeImgPath(DEFAULT + returnSizeNum(size),
                DataUtility::makeImgFilesPath_Power, id + returnSizeNum(size));
    }


    private String returnSizeNum(IconSize size) {
        return size == IconSize.Big ? "84" : "32";
    }

    public void addToBot_applyPower(final AbstractPower power) {
        ActionUtility.addToBot_applyPower(power, this.owner);
    }

    public void addToBot_reducePowerToOwner(final String powerID, int amount) {
        ActionUtility.addToBot_reducePower(powerID, amount, this.owner, this.owner);
    }

//    public void addToBot_reducePowerToOwner(final AbstractPower power) {
//        ActionUtility.addToBot_reducePower(power, power.owner);
//    }

    public void addToBot_removeSpecificPower(AbstractPower power) {
        ActionUtility.addToBot_removeSpecificPower(power, power.owner);
    }

    /**
     * 没事干不要重写这个
     */
    @Override
    public void updateDescription() {
        this.updateDescriptionArgs();
        String string = getDescriptionStrings();
        string = String.format(string, descriptionArgs);
        this.description = string;
    }

    @Override
    public abstract String getDescriptionStrings();

    @Override
    public final void setDescriptionArgs(Object... args) {
        if (args[0] instanceof Object[])
            descriptionArgs = (Object[]) args[0];
        else
            descriptionArgs = args;
    }

    @Override
    public abstract void updateDescriptionArgs();


    private enum IconSize {
        Big,
        Small

    }
}
