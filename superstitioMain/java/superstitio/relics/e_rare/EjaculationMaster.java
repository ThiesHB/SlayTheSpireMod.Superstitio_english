package superstitio.relics.e_rare;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.powers.SexualHeat;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onSquirt;
import superstitio.relics.SuperstitioRelic;
import superstitio.relics.interFace.Countdown;

public class EjaculationMaster extends SuperstitioRelic implements OnOrgasm_onSquirt, Countdown {
    public static final String ID = DataManager.MakeTextID(EjaculationMaster.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.RARE;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int DrawAmount = 1;
    private static final int EjaculationMax = 2;

    public EjaculationMaster() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void atBattleStart() {
        setCounter(Countdown.Zero);
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(EjaculationMax, DrawAmount);
    }

    @Override
    public void onCountZero() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new DrawCardAction(DrawAmount));
    }

    @Override
    public int getStarterNum() {
        return EjaculationMax;
    }

    @Override
    public void onSquirt(SexualHeat SexualHeatPower, AbstractCard card) {
        CountReduce();
    }
}