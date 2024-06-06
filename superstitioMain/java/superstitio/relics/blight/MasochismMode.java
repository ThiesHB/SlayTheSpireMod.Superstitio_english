package superstitio.relics.blight;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import superstitio.DataManager;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.SexualHeat;
import superstitioapi.relicToBlight.BecomeBlight;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.DataUtility;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.relicToBlight.BlightWithRelic;
import superstitioapi.utils.ActionUtility;
import superstitioapi.utils.CardUtility;

public class MasochismMode extends SuperstitioRelic implements BecomeBlight {
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

    private static void AddSexualHeat(int amount) {
        SexualHeat.addAction_addSexualHeat(AbstractDungeon.player, amount, AutoDoneInstantAction::addToTopAbstract);
    }

    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public String getDescriptionStrings() {
        return String.format(this.DESCRIPTIONS[0], MasochismModeDamageNeed, MasochismModeSexualHeatRate);
    }

    @Override
    public BlightWithRelic makeNewBlightWithRelic() {
        return new BlightWithRelic_MasochismMode();
    }

    public static class BlightWithRelic_MasochismMode extends BlightWithRelic {

        public static final String ID = DataUtility.MakeTextID(BlightWithRelic_MasochismMode.class);

        public BlightWithRelic_MasochismMode() {
            super(ID);
        }

        @Override
        public AbstractRelic makeRelic() {
            return new MasochismMode();
        }

        @Override
        public void atBattleStart() {
            ActionUtility.addToBot_applyPower(new MasochismModePower(AbstractDungeon.player));
        }
    }

    private static class MasochismModePower extends AbstractSuperstitioPower implements InvisiblePower {
        public static final String POWER_ID = DataManager.MakeTextID(MasochismModePower.class);

        private MasochismModePower(AbstractCreature owner) {
            super(POWER_ID, owner, -1);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount) {
            if (CardUtility.isNotInBattle()) return;
            if (info.type == DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType) return;
            if (info.type == DataManager.CanOnlyDamageDamageType.NoTriggerMasoRelicDamageType) return;
            this.flash();
            if (damageAmount < MasochismModeDamageNeed) return;
            AddSexualHeat(damageAmount / MasochismModeDamageNeed * MasochismModeSexualHeatRate);
        }

        @Override
        public void updateDescriptionArgs() {
        }
    }
}