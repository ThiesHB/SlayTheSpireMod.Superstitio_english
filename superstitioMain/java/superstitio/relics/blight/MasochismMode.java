package superstitio.relics.blight;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.SexualHeat;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasmFirst;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.relicToBlight.InfoBlight;
import superstitioapi.utils.ActionUtility;

@AutoAdd.Seen
public class MasochismMode extends SuperstitioRelic implements InfoBlight.BecomeInfoBlight, OnOrgasm_onOrgasmFirst {
    public static final String ID = DataManager.MakeTextID(MasochismMode.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int MasochismModeDamageNeed = 3;

    private static final int MasochismModeSexualHeatRate = 2;
    private static final int ReduceDelayHpLose = 3;

    public MasochismMode() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    private static void AddSexualHeat(int amount) {
        SexualHeat.useConsumer_addSexualHeat(AbstractDungeon.player, amount, AutoDoneInstantAction::addToTopAbstract);
    }

    @Override
    public void onOrgasmFirst(SexualHeat SexualHeatPower) {
        DelayHpLosePower.addToBot_removePower(ReduceDelayHpLose, AbstractDungeon.player, true);
    }

    @Override
    public void updateDescriptionArgs() {
    }
    @Override
    public String getDescriptionStrings() {
        return String.format(this.DESCRIPTIONS[0], MasochismModeDamageNeed, MasochismModeSexualHeatRate, ReduceDelayHpLose);
    }

    @Override
    public void atBattleStart() {
        ActionUtility.addToBot_applyPower(new MasochismModePower(AbstractDungeon.player));
    }

    @Override
    public void obtain() {
        InfoBlight.obtain(this);
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        InfoBlight.instanceObtain(this, callOnEquip);
    }

    @Override
    public void instantObtain() {
        InfoBlight.instanceObtain(this, true);
    }

    private static class MasochismModePower extends AbstractSuperstitioPower implements InvisiblePower {
        public static final String POWER_ID = DataManager.MakeTextID(MasochismModePower.class);

        private MasochismModePower(AbstractCreature owner) {
            super(POWER_ID, owner, -1);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount) {
            if (ActionUtility.isNotInBattle()) return;
            if (info.type == DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType) return;
            if (info.type == DataManager.CanOnlyDamageDamageType.NoTriggerLupaAndMasoRelicHpLose) return;
            this.flash();
            if (damageAmount < MasochismModeDamageNeed) return;
            AddSexualHeat(damageAmount / MasochismModeDamageNeed * MasochismModeSexualHeatRate);
        }

        @Override
        public void updateDescriptionArgs() {
        }
    }
}