package SuperstitioMod.relics;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.powers.SexualHeat;
import SuperstitioMod.powers.interFace.OnOrgasm;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class NippleRings extends CustomRelic implements OnOrgasm {
    public static final String ID = SuperstitioModSetup.MakeTextID(NippleRings.class.getSimpleName() + "Relic");
    private static final String IMG_PATH = SuperstitioModSetup.makeImgFilesPath_Relic("default_relic");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int Amount = 2;

    public NippleRings() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void atBattleStart() {
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], Amount);
    }

    @Override
    public void onCheckOrgasm(SexualHeat SexualHeatPower) {

    }

    @Override
    public void afterOrgasm(SexualHeat SexualHeatPower) {
        this.flash();
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, Amount)));
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseStrengthPower(AbstractDungeon.player, Amount)));
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