package superstitio.relics.blight;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.delayHpLose.DelayHpLosePatch;
import superstitio.delayHpLose.DelayHpLosePower_HealOnVictory;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.relicToBlight.InfoBlight;
import superstitioapi.utils.ActionUtility;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static superstitio.DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType;
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
    public void updateDescriptionArgs() {
    }

    @Override
    public void atBattleStart() {
        this.flash();
        ActionUtility.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        DevaBody_Masochism.SetPlayerImmunity();
    }

    @Override
    public void obtain() {
        AInfoBlightOnOrgasm.obtain(this);
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        AInfoBlightOnOrgasm.instanceObtain(this, callOnEquip);
    }

    @Override
    public void instantObtain() {
        AInfoBlightOnOrgasm.instanceObtain(this, true);
    }


}



