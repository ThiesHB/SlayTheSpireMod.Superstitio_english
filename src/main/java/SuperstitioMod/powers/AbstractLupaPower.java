package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import SuperstitioMod.customStrings.HasSFWVersion;
import SuperstitioMod.customStrings.PowerStringsWithSFW;
import SuperstitioMod.utils.updateDescriptionAdvanced;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractLupaPower extends AbstractPower implements updateDescriptionAdvanced {
    protected PowerStringsWithSFW powerStrings;
    private Object[] descriptionArgs;

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

    public AbstractLupaPower(String id, PowerStringsWithSFW powerStrings, final AbstractCreature owner, int amount, PowerType powerType, boolean needUpdateDescription) {
        this(id, powerStrings.getNAME(), owner, amount, powerType, needUpdateDescription);
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

    public static PowerStringsWithSFW getPowerStringsWithSFW(String cardName) {
        try {
            return HasSFWVersion.getCustomStringsWithSFW(cardName, DataManager.powers, PowerStringsWithSFW.class);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String makeImgPath(final String id, IconSize size) {
        return DataManager.makeImgPath("default" + returnSizeNum(size),
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

    @Override
    public final void updateDescription() {
        this.updateDescriptionArgs();
        String string = getDescriptionStrings();
        for (Object o : descriptionArgs) {
            string = String.format(string, o);
        }
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
        return powerStrings.DESCRIPTIONS[0];
    }

    private enum IconSize {
        Big,
        Small

    }
}
