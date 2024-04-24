package SuperstitioMod.relics;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.powers.SexualHeat;
import SuperstitioMod.powers.interFace.OnOrgasm;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class EjaculationMaster extends CustomRelic implements OnOrgasm {
    public static final String ID = SuperstitioModSetup.MakeTextID(EjaculationMaster.class.getSimpleName() + "Relic");
    private static final String IMG_PATH = SuperstitioModSetup.makeImgFilesPath_Relic("default_relic");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int DrawAmount = 1;
    private static final int EjaculationMax = 2;

    public EjaculationMaster() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        this.counter = EjaculationMax;
    }

    private void Ejaculation() {
        this.counter--;
        if (this.counter > 0) {
            return;
        }
        this.counter = EjaculationMax;
        this.addToBot(new DrawCardAction(DrawAmount));
        this.flash();
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        this.counter = EjaculationMax;
        this.grayscale = true;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], EjaculationMax, DrawAmount);
    }

    @Override
    public void onCheckOrgasm(SexualHeat SexualHeatPower) {
    }

    @Override
    public void afterOrgasm(SexualHeat SexualHeatPower) {
        Ejaculation();
    }

    @Override
    public void afterEndOrgasm(SexualHeat SexualHeatPower) {

    }

    @Override
    public boolean preventOrgasm(SexualHeat SexualHeatPower) {
        return false;
    }

    @Override
    public void beforeSquirt(SexualHeat SexualHeatPower) {

    }
}