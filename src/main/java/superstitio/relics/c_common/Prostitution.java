package superstitio.relics.c_common;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.relics.AbstractLupaRelic;
import superstitio.relics.interFace.Countup;

public class Prostitution extends AbstractLupaRelic implements Countup {
    public static final String ID = DataManager.MakeTextID(Prostitution.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int SexAmount = 5;
    private static final int CashAmount = 1;

    public Prostitution() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void atBattleStart() {
        setCounter(Countup.Zero);
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (targetCard instanceof FuckJob_Card)
            CountAdd();
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(SexAmount, CashAmount);
    }

    @Override
    public void onCountMax() {
        this.flash();
        AbstractDungeon.effectList.add(new RainingGoldEffect(CashAmount * 2, true));
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.player.gainGold(CashAmount);
    }

    @Override
    public int getMaxNum() {
        return SexAmount;
    }
}