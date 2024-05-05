package superstitio.relics;

import superstitio.DataManager;
import superstitio.utils.updateDescriptionAdvanced;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public abstract class AbstractLupaRelic extends CustomRelic implements updateDescriptionAdvanced {
    public static final String DEFAULT_RELIC = "default_relic";
    private Object[] descriptionArgs;

    public AbstractLupaRelic(String id, RelicTier relicTier, LandingSound landingSound) {
        super(id, ImageMaster.loadImage(makeImgPath(id)), relicTier, landingSound);
    }

    private static String makeImgPath(final String id) {
        return DataManager.makeImgPath(DEFAULT_RELIC, DataManager::makeImgFilesPath_Relic, id);
    }

    @Override
    public final String getUpdatedDescription() {
        this.updateDescriptionArgs();
        String string = getDescriptionStrings();
        string = String.format(string, descriptionArgs);
        return string;
    }

    public int getCounter() {
        return counter;
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
        return this.DESCRIPTIONS[0];
    }
}