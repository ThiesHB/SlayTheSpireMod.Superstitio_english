package superstitio.relics.a_starter.Maso;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnLoseHpRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.powers.SexualHeat;
import superstitio.relics.SuperstitioRelic;
import superstitio.utils.CardUtility;

/**
 * 右键点击切换S和M形态。
 * S：每造成超过SadismModeRate的伤害获得一点快感。
 * M：每受到超过MasochismModeRate的伤害获得一点快感。
 */
@AutoAdd.Seen
public class MasochismMode extends SuperstitioRelic implements BetterOnLoseHpRelic {
    public static final String ID = DataManager.MakeTextID(MasochismMode.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int MasochismModeDamageNeed = 3;

    private static final int MasochismModeSexualHeatRate = 1;

    public MasochismMode() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public int betterOnLoseHp(DamageInfo damageInfo, int i) {
        if (CardUtility.isNotInBattle()) return i;
        if (damageInfo.type == DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType) return i;
        if (damageInfo.type == DataManager.CanOnlyDamageDamageType.NoTriggerMasoRelicDamageType) return i;
//        AddSexualHeat(MasochismModeSexualHeatRate);
        this.flash();
        if (i < MasochismModeDamageNeed) return i;
        AddSexualHeat(i / MasochismModeDamageNeed * MasochismModeSexualHeatRate);
        return i;
    }

    private void AddSexualHeat(int amount) {
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SexualHeat(AbstractDungeon.player, amount)));
    }


    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public String getDescriptionStrings() {
        return String.format(this.DESCRIPTIONS[0], MasochismModeSexualHeatRate, MasochismModeDamageNeed, MasochismModeSexualHeatRate);
    }

}