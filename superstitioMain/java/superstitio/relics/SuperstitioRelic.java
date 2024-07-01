package superstitio.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import superstitio.DataManager;
import superstitio.characters.BaseCharacter;
import superstitioapi.utils.updateDescriptionAdvanced;

public abstract class SuperstitioRelic extends CustomRelic implements updateDescriptionAdvanced {
    public static final String DEFAULT_RELIC = "default_relic";
    private Object[] descriptionArgs;

    public SuperstitioRelic(String id, RelicTier relicTier, LandingSound landingSound) {
        super(id, ImageMaster.loadImage(makeImgPath(id)), ImageMaster.loadImage(makeImgPathOutLine(id)), relicTier, landingSound);
        this.largeImg = ImageMaster.loadImage(makeImgPathLarge(id));
    }

    private static String makeImgPath(final String id) {
        return DataManager.makeImgPath(DEFAULT_RELIC, DataManager::makeImgFilesPath_Relic, id);
    }

    private static String makeImgPathOutLine(final String id) {
        return DataManager.makeImgPath(DEFAULT_RELIC, DataManager::makeImgFilesPath_RelicOutline, id);
    }

    private static String makeImgPathLarge(final String id) {
        return DataManager.makeImgPath(DEFAULT_RELIC, DataManager::makeImgFilesPath_RelicLarge, id);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player instanceof BaseCharacter;
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