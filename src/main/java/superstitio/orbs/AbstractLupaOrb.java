package superstitio.orbs;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import superstitio.DataManager;
import superstitio.customStrings.HasSFWVersion;
import superstitio.customStrings.OrbStringsSet;
import superstitio.utils.updateDescriptionAdvanced;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLupaOrb extends AbstractOrb implements updateDescriptionAdvanced {
    private static final Map<String, Texture> orbTextures = new HashMap<>();
    protected final OrbStringsSet orbStringsSet;
    private Object[] descriptionArgs;

    public AbstractLupaOrb(String ID) {
        this(ID, 0, 0, true);
    }

    public AbstractLupaOrb(String ID, int basePassiveAmount, int baseEvokeAmount, boolean autoUpdateDescription) {
        this(ID, basePassiveAmount, baseEvokeAmount, getPowerStringsWithSFW(ID), autoUpdateDescription);
    }

    private AbstractLupaOrb(String ID, int basePassiveAmount, int baseEvokeAmount, OrbStringsSet orbStringsSet, boolean autoUpdateDescription) {
        this.ID = ID;
        this.name = orbStringsSet.getNAME();
        this.orbStringsSet = orbStringsSet;
        this.basePassiveAmount = basePassiveAmount;
        this.passiveAmount = this.basePassiveAmount;
        this.baseEvokeAmount = baseEvokeAmount;
        this.evokeAmount = this.baseEvokeAmount;
        String imgPath = getImgPath(ID);
        if (imgPath != null) {
            this.img = orbTextures.get(imgPath);
            if (this.img == null) {
                this.img = ImageMaster.loadImage(imgPath);
                orbTextures.put(imgPath, this.img);
            }
        }

        if (autoUpdateDescription)
            this.updateDescription();
    }

//    @Override
//    public abstract void updateDescription();

    public static OrbStringsSet getPowerStringsWithSFW(String cardName) {
        return HasSFWVersion.getCustomStringsWithSFW(cardName, DataManager.orbs, OrbStringsSet.class);
    }

    protected static String getImgPath(final String id) {
        return DataManager.makeImgPath("default", DataManager::makeImgFilesPath_Orb, id);
    }

    @Override
    public final void updateDescription() {
        this.updateDescriptionArgs();
        String string = getDescriptionStrings();
        string = String.format(string, descriptionArgs);
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
        return orbStringsSet.getRightVersion().DESCRIPTION[0];
    }
}
