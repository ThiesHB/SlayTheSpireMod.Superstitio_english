package superstitio.relics.a_starter.Maso;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import superstitio.DataManager;
import superstitio.delayHpLose.DelayHpLosePatch;
import superstitio.delayHpLose.DelayHpLosePower_HealOnVictory;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.relics.AbstractLupaRelic;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static superstitio.DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType;
import static superstitio.utils.ActionUtility.addToBot_applyPower;
import static superstitio.utils.ActionUtility.addToTop_applyPower;

@AutoAdd.Seen
public class DevaBody_Masochism extends AbstractLupaRelic {
    public static final String ID = DataManager.MakeTextID(DevaBody_Masochism.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public DevaBody_Masochism() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot_applyPower(new DevaBody_Masochism.DoubleRemoveHpLostWhenHasVulnerablePower(AbstractDungeon.player));
        DevaBody_Masochism.SetPlayerImmunity();
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
    public void updateDescriptionArgs() {
    }

    private static class DoubleRemoveHpLostWhenHasVulnerablePower extends AbstractSuperstitioPower implements InvisiblePower {
        public static final String POWER_ID = DataManager.MakeTextID(DoubleRemoveHpLostWhenHasVulnerablePower.class);

        DoubleRemoveHpLostWhenHasVulnerablePower(AbstractCreature owner) {
            super(POWER_ID, owner, -1);
        }

        @Override
        public float modifyBlockLast(float blockAmount) {
            if (owner.powers.stream().anyMatch(power -> power instanceof VulnerablePower))
                return super.modifyBlockLast(blockAmount * 2);
            return super.modifyBlockLast(blockAmount);
        }

        @Override
        public void updateDescriptionArgs() {
        }
    }
}