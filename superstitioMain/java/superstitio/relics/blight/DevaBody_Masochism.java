package superstitio.relics.blight;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import superstitio.DataManager;
import superstitio.delayHpLose.DelayHpLosePatch;
import superstitio.delayHpLose.DelayHpLosePower_HealOnVictory;
import superstitio.relics.BecomeBlight;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.DataUtility;
import superstitioapi.relic.BlightWithRelic;
import superstitioapi.utils.ActionUtility;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static superstitio.DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType;
import static superstitioapi.utils.ActionUtility.addToTop_applyPower;

public class DevaBody_Masochism extends SuperstitioRelic implements BecomeBlight {
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
    public void updateDescriptionArgs() {
    }

    @Override
    public BlightWithRelic makeNewBlightWithRelic() {
        return new BlightWithRelic_DevaBody_Maso();
    }

    public static class BlightWithRelic_DevaBody_Maso extends BlightWithRelic {

        public static final String ID = DataUtility.MakeTextID(BlightWithRelic_DevaBody_Maso.class);

        public BlightWithRelic_DevaBody_Maso() {
            super(ID);
        }

        @Override
        public AbstractRelic makeRelic() {
            return new DevaBody_Masochism();
        }

        @Override
        public void atBattleStart() {
            this.flash();
            ActionUtility.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this.relic));
            DevaBody_Masochism.SetPlayerImmunity();
        }
    }
}



