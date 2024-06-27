package superstitio.relics.a_starter;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import superstitio.DataManager;
import superstitio.cards.general.TempCard.VulnerableTogether;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.utils.ActionUtility;

import static superstitioapi.utils.ActionUtility.addToBot_applyPower;
import static superstitioapi.utils.ActionUtility.addToBot_makeTempCardInBattle;

@AutoAdd.Seen
public class DoubleBlockWithVulnerable extends SuperstitioRelic {
    public static final String ID = DataManager.MakeTextID(DoubleBlockWithVulnerable.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public DoubleBlockWithVulnerable() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        ActionUtility.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot_applyPower(new DoubleRemoveHpLostWhenHasVulnerablePower(AbstractDungeon.player));
        addToBot_makeTempCardInBattle(new VulnerableTogether(), ActionUtility.BattleCardPlace.Hand);
    }

    @Override
    public void updateDescriptionArgs() {
    }

    public static class DoubleRemoveHpLostWhenHasVulnerablePower extends AbstractSuperstitioPower implements InvisiblePower {
        public static final String POWER_ID = DataManager.MakeTextID(DoubleRemoveHpLostWhenHasVulnerablePower.class);

        private DoubleRemoveHpLostWhenHasVulnerablePower(AbstractCreature owner) {
            super(POWER_ID, owner, -1);
        }

        @Override
        public float modifyBlockLast(float blockAmount) {
            if (owner.powers.stream().anyMatch(power -> power instanceof VulnerablePower))
                return super.modifyBlockLast(blockAmount * 1.25f);
            return super.modifyBlockLast(blockAmount);
        }

        @Override
        public void updateDescriptionArgs() {
        }
    }
}