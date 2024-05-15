package superstitio.relics.a_starter.Lupa;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.AbstractCard_FuckJob;
import superstitio.delayHpLose.DelayHpLosePatch;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.relics.AbstractLupaRelic;
import superstitio.utils.ActionUtility;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static superstitio.cards.general.AbstractCard_FuckJob.addToTop_gainSexMark_Inside;
import static superstitio.cards.general.AbstractCard_FuckJob.addToTop_gainSexMark_Outside;

@AutoAdd.Seen
public class DevaBody_Lupa extends AbstractLupaRelic {
    public static final String ID = DataManager.MakeTextID(DevaBody_Lupa.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public DevaBody_Lupa() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    public static void SetPlayerImmunity() {
        DelayHpLosePatch.IsImmunityFields.checkShouldImmunity.set(
                player, ((player, damageInfo, damageAmount) -> {
                    if (damageInfo.type == DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType) {
                        return false;
                    }
                    ActionUtility.addToTop_applyPower(new DelayHpLosePower(AbstractDungeon.player, damageAmount));
                    return true;
                }));
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        SetPlayerImmunity();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card instanceof AbstractCard_FuckJob) {
            if (card.cardID.contains("Fuck_")) {
                addToTop_gainSexMark_Inside(card.name);
                return;
            }
            if (card.cardID.contains("Job_")) {
                addToTop_gainSexMark_Outside(((AbstractCard_FuckJob) card).getEXTENDED_DESCRIPTION()[0]);
                return;
            }
        }
    }

    @Override
    public void updateDescriptionArgs() {
    }

}