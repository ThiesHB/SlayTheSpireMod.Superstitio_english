package superstitio.relics.blight;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import superstitio.DataManager;
import superstitio.delayHpLose.DelayHpLosePatch;
import superstitio.delayHpLose.DelayHpLosePower_HealOnVictory;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.relicToBlight.InfoBlight;
import superstitioapi.utils.ActionUtility;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static superstitio.DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType;
import static superstitio.relics.blight.DevaBody_Masochism.DoubleRemoveHpLostWhenHasVulnerablePower.BLOCK_RATE;
import static superstitioapi.utils.ActionUtility.addToBot_applyPower;
import static superstitioapi.utils.ActionUtility.addToTop_applyPower;

@AutoAdd.Seen
public class DevaBody_Masochism extends SuperstitioRelic implements InfoBlight.BecomeInfoBlight {
    public static final String ID = DataManager.MakeTextID(DevaBody_Masochism.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public DevaBody_Masochism() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    public static void SetPlayerImmunity() {
        DelayHpLosePatch.IsImmunityFields.checkShouldImmunity.set(
                player, ((player, damageInfo, damageAmount) -> {
                    if (damageInfo.type == UnBlockAbleDamageType) {
                        return false;
                    }
                    addToTop_applyPower(new DelayHpLosePower_HealOnVictory(AbstractDungeon.player, damageAmount));
                    return true;
                }));
    }

    @Override
    public void atBattleStart() {
        this.flash();
        ActionUtility.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot_applyPower(new DoubleRemoveHpLostWhenHasVulnerablePower(AbstractDungeon.player));
        DevaBody_Masochism.SetPlayerImmunity();
    }


    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(BLOCK_RATE);
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
        protected static final float BLOCK_RATE = 1.5f;

        private DoubleRemoveHpLostWhenHasVulnerablePower(AbstractCreature owner) {
            super(POWER_ID, owner, -1);
        }

        @Override
        public float modifyBlockLast(float blockAmount) {
            if (owner.powers.stream().anyMatch(power -> power instanceof VulnerablePower))
                return super.modifyBlockLast(blockAmount * BLOCK_RATE);
            return super.modifyBlockLast(blockAmount);
        }

        @Override
        public void updateDescriptionArgs() {
        }
    }
}



