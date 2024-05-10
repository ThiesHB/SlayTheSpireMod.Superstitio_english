package superstitio.relics.a_starter;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.relics.OnCreateBlockInstanceRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.delayHpLose.DelayHpLosePatch;
import superstitio.relics.AbstractLupaRelic;
import superstitio.utils.ActionUtility;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

@AutoAdd.Seen
public class DevaBody extends AbstractLupaRelic {
    public static final String ID = DataManager.MakeTextID(DevaBody.class.getSimpleName());
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public DevaBody() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void atBattleStart() {
        DevaBody self = this;
        DelayHpLosePatch.IsImmunityFields.checkShouldImmunity.set(
                player, ((player, damageInfo, damageAmount) -> {
                    if (damageInfo.type == DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType) {
                        return false;
                    }
                    self.flash();
                    ActionUtility.addToTop_applyPower(new DelayHpLosePower(AbstractDungeon.player, damageAmount));
                    return true;
                }));
    }

    @Override
    public void updateDescriptionArgs() {
    }

}