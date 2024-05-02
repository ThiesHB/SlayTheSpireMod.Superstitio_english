package SuperstitioMod.relics;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.Lupa.AbstractLupaCard_FuckJob;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Prostitution extends AbstractLupaRelic {
    public static final String ID = DataManager.MakeTextID(Prostitution.class.getSimpleName() + "Relic");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int SexAmount = 5;
    private static final int CashAmount = 1;

    public Prostitution() {
        super(ID, RELIC_TIER, LANDING_SOUND);
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
        }
        else if (this.counter == SexAmount - 1) {
            this.beginPulse();
            this.pulse = true;
        }
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(SexAmount, CashAmount);
    }
}