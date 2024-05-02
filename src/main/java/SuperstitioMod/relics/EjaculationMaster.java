package SuperstitioMod.relics;

import SuperstitioMod.DataManager;
import SuperstitioMod.powers.SexualHeat;
import SuperstitioMod.powers.interFace.OnOrgasm.OnOrgasm_afterOrgasm;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class EjaculationMaster extends AbstractLupaRelic implements OnOrgasm_afterOrgasm {
    public static final String ID = DataManager.MakeTextID(EjaculationMaster.class.getSimpleName() + "Relic");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int DrawAmount = 1;
    private static final int EjaculationMax = 2;

    public EjaculationMaster() {
        super(ID, RELIC_TIER, LANDING_SOUND);
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
    public void updateDescriptionArgs() {
        setDescriptionArgs(EjaculationMax,DrawAmount);
    }

    @Override
    public void afterTriggerOrgasm(SexualHeat SexualHeatPower) {
        Ejaculation();
    }
}