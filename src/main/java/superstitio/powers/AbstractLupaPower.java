package superstitio.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.customStrings.HasSFWVersion;
import superstitio.customStrings.PowerStringsSet;
import superstitio.utils.updateDescriptionAdvanced;

public abstract class AbstractLupaPower extends AbstractPower implements updateDescriptionAdvanced {
    public static final String DEFAULT = "default";
    public final PowerStringsSet powerStrings;
    private Object[] descriptionArgs;

    public AbstractLupaPower(String id, PowerStringsSet powerStrings, final AbstractCreature owner, int amount, PowerType powerType,
                             boolean needUpdateDescription) {
        this.name = powerStrings.getRightVersion().NAME;
        this.ID = id;
        this.owner = owner;
        this.type = powerType;

        this.amount = amount;
        this.powerStrings = powerStrings;

        // 添加一大一小两张能力图

        String path128 = makeImgPath(id, IconSize.Big);
        String path48 = makeImgPath(id, IconSize.Small);
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        if (needUpdateDescription)
            this.updateDescription();
    }

    public AbstractLupaPower(String id, final AbstractCreature owner, int amount, PowerType powerType, boolean needUpdateDescription) {
        this(id, AbstractLupaPower.getPowerStringsWithSFW(id), owner, amount, powerType, needUpdateDescription);
    }

    public AbstractLupaPower(String id, final AbstractCreature owner, int amount, PowerType powerType) {
        this(id, owner, amount, powerType, true);
    }

    public AbstractLupaPower(String id, final AbstractCreature owner, int amount) {
        this(id, owner, amount, PowerType.BUFF);
    }

    public static PowerStringsSet getPowerStringsWithSFW(String cardName) {
        return HasSFWVersion.getCustomStringsWithSFW(cardName, DataManager.powers, PowerStringsSet.class);
    }

    protected void renderAmount2(SpriteBatch sb, float x, float y, Color c, int amount2) {
        if (amount2 <= 0) return;
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "" + amount2, x, y + 15.0F * Settings.scale, this.fontScale, c);
    }

    protected void update_showTips(Hitbox hitbox) {
        hitbox.update();
        if (hitbox.hovered) {
            TipHelper.renderGenericTip(hitbox.cX + 96.0F * Settings.scale,
                    hitbox.cY + 64.0F * Settings.scale, this.name, this.description);
        }

        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
    }

    protected void addToBot_AutoRemoveOne(AbstractPower power) {
        this.flash();
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, power));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, power, 1));
        }
    }

    private String makeImgPath(final String id, IconSize size) {
        return DataManager.makeImgPath(DEFAULT + returnSizeNum(size),
                DataManager::makeImgFilesPath_Power, id + returnSizeNum(size));
    }

    private String returnSizeNum(IconSize size) {
        return size == IconSize.Big ? "84" : "32";
    }

    public void addToBot_applyPowerToOwner(final AbstractPower power) {
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, power));
    }

    public void addToBot_reducePowerToOwner(final String powerID, int amount) {
        this.addToBot(new ReducePowerAction(this.owner, this.owner, powerID, amount));
    }

    public void addToBot_removeSpecificPower(AbstractPower power) {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, power));
    }


    @SuppressWarnings("RedundantCast")
    @Override
    public final void updateDescription() {
        this.updateDescriptionArgs();
        String string = getDescriptionStrings();
        string = String.format(string, (Object[]) descriptionArgs);
        this.description = string;
    }

    @Override
    public final void setDescriptionArgs(Object... args) {
        if (args[0] instanceof Object[])
            descriptionArgs = (Object[]) args[0];
        else
            descriptionArgs = args;
    }

    @Override
    public abstract void updateDescriptionArgs();

    @Override
    public String getDescriptionStrings() {
        return powerStrings.getDESCRIPTIONS()[0];
    }

    private enum IconSize {
        Big,
        Small

    }
}
