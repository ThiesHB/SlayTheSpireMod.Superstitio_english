package superstitio.relics.blight;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import superstitio.DataManager;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.relicToBlight.InfoBlight;

import static superstitio.relics.blight.EnjoyAilment.DoubleRemoveHpLostWhenHasVulnerablePower.BLOCK_RATE;
import static superstitio.relics.blight.EnjoyAilment.DoubleRemoveHpLostWhenHasVulnerablePower.DAMAGE_RATE;
import static superstitioapi.utils.ActionUtility.addToBot_applyPower;

@AutoAdd.Seen
public class EnjoyAilment extends SuperstitioRelic implements InfoBlight.BecomeInfoBlight {
    public static final String ID = DataManager.MakeTextID(EnjoyAilment.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public EnjoyAilment() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(BLOCK_RATE, DAMAGE_RATE, BLOCK_RATE, DAMAGE_RATE);
    }

    @Override
    public void atBattleStart() {
        addToBot_applyPower(new DoubleRemoveHpLostWhenHasVulnerablePower(AbstractDungeon.player));
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

    public static class DoubleRemoveHpLostWhenHasVulnerablePower extends AbstractSuperstitioPower implements InvisiblePower {
        public static final String POWER_ID = DataManager.MakeTextID(DoubleRemoveHpLostWhenHasVulnerablePower.class);
        protected static final int BLOCK_RATE = 3;
        protected static final int DAMAGE_RATE = 3;

        private DoubleRemoveHpLostWhenHasVulnerablePower(AbstractCreature owner) {
            super(POWER_ID, owner, -1);
        }

        @Override
        public float modifyBlockLast(float blockAmount) {
            float blockLast = super.modifyBlockLast(blockAmount);
            if (owner.powers.stream().anyMatch(power -> power instanceof WeakPower)) {
                blockLast *= (1 + (float) 1 / BLOCK_RATE);
            }
            if (owner.powers.stream().anyMatch(power -> power instanceof VulnerablePower)) {
                blockLast *= (1 + (float) 1 / BLOCK_RATE);
            }
            return blockLast;
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type) {
            float damageLast = super.atDamageGive(damage, type);
            if (owner.powers.stream().anyMatch(power -> power instanceof FrailPower)) {
                damageLast *= (1 + (float) 1 / DAMAGE_RATE);
            }
            if (owner.powers.stream().anyMatch(power -> power instanceof VulnerablePower)) {
                damageLast *= (1 + (float) 1 / DAMAGE_RATE);
            }
            return damageLast;
        }

        @Override
        public void updateDescriptionArgs() {
        }
    }
}