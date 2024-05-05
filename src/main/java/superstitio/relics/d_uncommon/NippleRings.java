package superstitio.relics.d_uncommon;

import superstitio.DataManager;
import superstitio.powers.SexualHeat;
import superstitio.powers.interfaces.orgasm.OnOrgasm_afterOrgasm;
import superstitio.relics.AbstractLupaRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class NippleRings extends AbstractLupaRelic implements OnOrgasm_afterOrgasm {
    public static final String ID = DataManager.MakeTextID(NippleRings.class.getSimpleName());
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int Amount = 2;

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
    public void afterTriggerOrgasm(SexualHeat SexualHeatPower) {
        this.flash();
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, Amount)));
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseStrengthPower(AbstractDungeon.player, Amount)));
    }
}