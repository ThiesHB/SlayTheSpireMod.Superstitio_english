package superstitio.relics.d_uncommon;

import superstitio.DataManager;
import superstitio.powers.OutsideSemen;
import superstitio.relics.AbstractLupaRelic;
import superstitio.relics.interFace.Countup;
import superstitio.utils.ActionUtility;
import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class CumOnShoes extends AbstractLupaRelic implements OnApplyPowerRelic, Countup {
    public static final String ID = DataManager.MakeTextID(CumOnShoes.class.getSimpleName());
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int NeedAmount = 10;

    public CumOnShoes() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void atBattleStart() {
        setCounter(Countup.Zero);
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(NeedAmount);
    }


    @Override
    public boolean onApplyPower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (abstractPower instanceof OutsideSemen)
            this.counter += abstractPower.amount;
        return true;
    }

    @Override
    public void onCountMax() {
        ActionUtility.addToBot_applyPower(new DexterityPower(AbstractDungeon.player, 1));
    }

    @Override
    public int getMaxNum() {
        return NeedAmount;
    }
}