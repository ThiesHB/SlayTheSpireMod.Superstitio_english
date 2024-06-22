package superstitio.relics.defect;

import basemod.AutoAdd;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnLoseHpRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import superstitio.DataManager;
import superstitio.powers.SexualHeat;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.utils.ActionUtility;

/**
 * 右键点击切换S和M形态。
 * S：每造成超过SadismModeRate的伤害获得一点快感。
 * M：每受到超过MasochismModeRate的伤害获得一点快感。
 */
@AutoAdd.Ignore
public class SorM extends SuperstitioRelic implements ClickableRelic, CustomSavable<Integer>, BetterOnLoseHpRelic {
    public static final String ID = DataManager.MakeTextID(SorM.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int SadismModeDamageNeed = 15;
    private static final int MasochismModeDamageNeed = 3;

    private static final int MasochismModeSexualHeatRate = 1;
    private static final int SadismModeSexualHeatRate = 2;
    private int ClickTime = 0;
    private boolean MasochismMode;
    private boolean SadismMode;


    public SorM() {
        super(ID, RELIC_TIER, LANDING_SOUND);
        MasochismMode = false;
        SadismMode = false;
        //Default();
        //updateDesAndImg();
    }

    private void Default() {
        MasochismMode = true;
        SadismMode = false;
    }

//    @Override
//    public void updateDescription(AbstractPlayer.PlayerClass c) {
//        updateDesAndImg();
//    }

    private void updateDesAndImg() {
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
        if (!MasochismMode && !SadismMode) Default();
    }

    @Override
    public void atBattleStart() {
        updateDesAndImg();
    }

    @Override
    public void onLoseHp(int damageAmount) {


    }

    @Override
    public int betterOnLoseHp(DamageInfo damageInfo, int i) {
        if (ActionUtility.isNotInBattle()) return i;
        if (!MasochismMode) return i;
        if (i < MasochismModeDamageNeed) return i;
        if (damageInfo.type == DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType) return i;
        AddSexualHeat(i / MasochismModeDamageNeed * MasochismModeSexualHeatRate);
        this.flash();
        return i;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!SadismMode) return;
        if (damageAmount < SadismModeDamageNeed) return;
        AddSexualHeat(SadismModeSexualHeatRate);
//        AddSexualHeat(damageAmount / SadismModeRate * SexualHeatRate);
        this.flash();
    }

    private void AddSexualHeat(int amount) {
        SexualHeat.addToBot_addSexualHeat(AbstractDungeon.player, amount);
    }

    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public String getDescriptionStrings() {
        if (MasochismMode && SadismMode)
            return String.format(this.DESCRIPTIONS[3], MasochismModeDamageNeed, MasochismModeSexualHeatRate, SadismModeDamageNeed,
                    MasochismModeSexualHeatRate);
        if (SadismMode)
            return String.format(this.DESCRIPTIONS[2], SadismModeDamageNeed, MasochismModeSexualHeatRate);
        if (MasochismMode)
            return String.format(this.DESCRIPTIONS[1], MasochismModeDamageNeed, MasochismModeSexualHeatRate);
        return String.format(this.DESCRIPTIONS[0], MasochismModeDamageNeed, MasochismModeSexualHeatRate, SadismModeDamageNeed,
                MasochismModeSexualHeatRate);

    }

    @Override
    public void onRightClick() {
        if (!ActionUtility.isNotInBattle()) return;
        this.flash();
        if (ClickTime >= 99) {
            MasochismMode = true;
            SadismMode = true;
            return;
        }
        ClickTime++;
        MasochismMode = !MasochismMode;
        SadismMode = !SadismMode;
        updateDesAndImg();
    }

    @Override
    public Integer onSave() {
        if (MasochismMode && SadismMode)
            return 30000 + ClickTime;
        if (MasochismMode)
            return 10000 + ClickTime;
        if (SadismMode)
            return 20000 + ClickTime;
        return 0;
    }

    @Override
    public void onLoad(Integer integer) {
        this.ClickTime = integer % 10000;
        switch (integer - this.ClickTime) {
            case 30000:
                MasochismMode = true;
                SadismMode = true;
                break;
            case 10000:
                MasochismMode = true;
                SadismMode = false;
                break;
            case 20000:
                MasochismMode = false;
                SadismMode = true;
                break;
            default:
                MasochismMode = false;
                SadismMode = false;
                break;
        }
    }
}