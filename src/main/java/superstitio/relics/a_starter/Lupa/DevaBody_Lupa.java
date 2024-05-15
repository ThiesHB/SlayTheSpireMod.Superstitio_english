package superstitio.relics.a_starter.Lupa;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.FuckJob_Card;
import superstitio.delayHpLose.DelayHpLosePatch;
import superstitio.delayHpLose.DelayHpLosePower_ApplyOnVictory;
import superstitio.relics.AbstractLupaRelic;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static superstitio.DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType;
import static superstitio.cards.general.FuckJob_Card.*;
import static superstitio.utils.ActionUtility.addToTop_applyPower;

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
                    if (damageInfo.type == UnBlockAbleDamageType) {
                        return false;
                    }
                    addToTop_applyPower(new DelayHpLosePower_ApplyOnVictory(AbstractDungeon.player, damageAmount));
                    return true;
                }));
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        DevaBody_Lupa.SetPlayerImmunity();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card instanceof FuckJob_Card && card instanceof SuperstitioCard) {
            if (card.cardID.contains("Fuck_")) {
                addToTop_Semen_Inside();
                return;
            }
            if (card.cardID.contains("Job_")) {
                addToTop_Semen_Outside();
                return;
            }
            return;
        }
        if (card.type == AbstractCard.CardType.ATTACK)
            addToTop_Semen_Normal();
    }

    @Override
    public void updateDescriptionArgs() {
    }

}