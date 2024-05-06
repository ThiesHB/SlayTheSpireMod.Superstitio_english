package superstitio.relics.a_starter;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import superstitio.DataManager;
import superstitio.powers.SexualHeat;
import superstitio.powers.interfaces.orgasm.OnOrgasm_onOrgasm;
import superstitio.relics.AbstractLupaRelic;
import superstitio.relics.interFace.Countdown;

@AutoAdd.Seen
public class EjaculationMaster extends AbstractLupaRelic implements OnOrgasm_onOrgasm, Countdown {
    public static final String ID = DataManager.MakeTextID(EjaculationMaster.class.getSimpleName());
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
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
    public void onOrgasm(SexualHeat SexualHeatPower) {
        CountReduce();
    }

    @Override
    public void onCountZero() {
        this.addToBot(new DrawCardAction(DrawAmount));
    }

    @Override
    public int getStarterNum() {
        return EjaculationMax;
    }
}