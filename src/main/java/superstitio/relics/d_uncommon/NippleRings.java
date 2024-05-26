package superstitio.relics.d_uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import superstitio.DataManager;
import superstitio.powers.SexualHeat;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm;
import superstitio.relics.SuperstitioRelic;

public class NippleRings extends SuperstitioRelic implements OnOrgasm_onOrgasm {
    public static final String ID = DataManager.MakeTextID(NippleRings.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int Amount = 1;

    public NippleRings() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void atBattleStart() {
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(Amount);
    }

    @Override
    public void onOrgasm(SexualHeat SexualHeatPower) {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, Amount)));
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseStrengthPower(AbstractDungeon.player, Amount)));
    }
}