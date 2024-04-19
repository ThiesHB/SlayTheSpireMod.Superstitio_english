package SuperstitioMod.relics;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard_FuckJob;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class Prostitution extends CustomRelic {
    public static final String ID = SuperstitioModSetup.MakeTextID(Prostitution.class.getSimpleName() + "Relic");
    private static final String IMG_PATH = SuperstitioModSetup.makeImgFilesPath_Relic("default_relic");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int SexAmount = 5;
    private static final int CashAmount = 1;

    public Prostitution() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (targetCard instanceof AbstractLupaCard_FuckJob)
            checkPay();
    }

    private void checkPay() {
        ++this.counter;
        if (this.counter == SexAmount) {
            this.counter = 0;
            this.flash();
            this.pulse = false;
            AbstractDungeon.player.gainGold(CashAmount);
        } else if (this.counter == SexAmount - 1) {
            this.beginPulse();
            this.pulse = true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], SexAmount, CashAmount);
    }
}