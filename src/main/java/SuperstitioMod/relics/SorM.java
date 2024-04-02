package SuperstitioMod.relics;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.powers.SexualHeat;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

/**
 * 右键点击切换S和M形态。S：每造成超过SadismModeRate的伤害获得一点快感。M：每受到超过MasochismModeRate的伤害获得一点快感。
 */
public class SorM extends CustomRelic implements ClickableRelic, CustomSavable<Integer> {
    public static final String ID = SuperstitioModSetup.MakeTextID(SorM.class.getSimpleName() + "Relic");
    private static final String IMG_PATH = SuperstitioModSetup.getImgFilesPath() + "relics/default_relic.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int SadismModeRate = 10;
    private static final int MasochismModeRate = 5;
    private int ClickTime = 0;
    private boolean MasochismMode;
    private boolean SadismMode;


    public SorM() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        MasochismMode = false;
        SadismMode = false;
        //Default();
        //updateDesAndImg();
    }

    private void Default() {
        MasochismMode = true;
        SadismMode = false;
    }

    private void updateDesAndImg() {
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
        if (!MasochismMode && !SadismMode) Default();
    }

//    @Override
//    public void updateDescription(AbstractPlayer.PlayerClass c) {
//        updateDesAndImg();
//    }


    @Override
    public void atBattleStart() {
        updateDesAndImg();
    }

    @Override
    public void onLoseHp(int damageAmount) {
        if (!MasochismMode) return;
        if (damageAmount < MasochismModeRate) return;
        AddSexualHeat(damageAmount / MasochismModeRate);
        this.flash();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!SadismMode) return;
        if (damageAmount < SadismModeRate) return;
        AddSexualHeat(damageAmount / SadismModeRate);
        this.flash();
    }

    private void UpdateTitleAndImg() {
//        if (MasochismMode && SadismMode)
//            this.name = this.DESCRIPTIONS[3];
//        if (MasochismMode)
//            return String.format(this.DESCRIPTIONS[0], MasochismModeRate);
//        if (SadismMode)
//            return String.format(this.DESCRIPTIONS[1], SadismModeRate);
    }


    private void AddSexualHeat(int amount) {
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SexualHeat(AbstractDungeon.player, amount)));
    }

    @Override
    public String getUpdatedDescription() {
        if (MasochismMode && SadismMode)
            return String.format(this.DESCRIPTIONS[3], MasochismModeRate, SadismModeRate);
        if (MasochismMode)
            return String.format(this.DESCRIPTIONS[1], MasochismModeRate);
        if (SadismMode)
            return String.format(this.DESCRIPTIONS[2], SadismModeRate);
        return String.format(this.DESCRIPTIONS[0], MasochismModeRate, SadismModeRate);
    }

    @Override
    public void onRightClick() {
        updateDesAndImg();
        this.flash();
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) return;
        if (ClickTime >= 99) {
            MasochismMode = true;
            SadismMode = true;
            return;
        }
        ClickTime++;
        MasochismMode = !MasochismMode;
        SadismMode = !SadismMode;
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