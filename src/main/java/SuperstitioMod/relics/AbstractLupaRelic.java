package SuperstitioMod.relics;

import SuperstitioMod.DataManager;
import SuperstitioMod.utils.updateDescriptionAdvanced;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;

public abstract class AbstractLupaRelic extends CustomRelic implements updateDescriptionAdvanced {
    private Object[] descriptionArgs;

    public AbstractLupaRelic(String id, RelicTier relicTier, LandingSound landingSound) {
        super(id, ImageMaster.loadImage(makeImgPath(id)), relicTier, landingSound);
    }

    private static String makeImgPath(final String id) {
        return DataManager.makeImgPath("default_relic", DataManager::makeImgFilesPath_Relic, id);
    }

    @Override
    public final String getUpdatedDescription() {
        this.updateDescriptionArgs();
        String string = getDescriptionStrings();
        for (Object o : descriptionArgs) {
            string = String.format(string, o);
        }
        return string;
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