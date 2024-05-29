package superstitio.relics.a_starter.Lupa;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.lupa.OnAddSemenPower;
import superstitio.delayHpLose.DelayHpLosePatch;
import superstitio.delayHpLose.DelayHpLosePower_ApplyOnAttacked;
import superstitio.delayHpLose.DelayHpLosePower_ApplyOnlyOnVictory;
import superstitio.powers.lupaOnly.FloorSemen;
import superstitio.powers.lupaOnly.InsideSemen;
import superstitio.powers.lupaOnly.OutsideSemen;
import superstitio.relics.SuperstitioRelic;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static superstitio.DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType;
import static superstitio.utils.ActionUtility.addToBot_applyPower;
import static superstitio.utils.ActionUtility.addToTop_applyPower;

@AutoAdd.Seen
public class DevaBody_Lupa extends SuperstitioRelic {
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
                    addToTop_applyPower(new DelayHpLosePower_ApplyOnAttacked(AbstractDungeon.player, damageAmount));
                    return true;
                }));
    }

    private static void addToBot_Cum_Inside() {
        addToBot_applyPower(new InsideSemen(player, FuckJob_Card.InsideSemenRate));
    }

    private static void addToBot_Cum_Outside() {
        addToBot_applyPower(new OutsideSemen(player, FuckJob_Card.OutsideSemenRate));
    }

    private static void addToBot_Cum_Normal() {
        addToBot_applyPower(new FloorSemen(player, FuckJob_Card.FloorSemenRate));
    }

    private static void addToBot_AddSemen(AbstractCard card) {
        boolean shouldApply = true;
        for (AbstractPower power : player.powers) {
            if (power instanceof OnAddSemenPower && !((OnAddSemenPower) power).onAddSemen_shouldApply(power))
                shouldApply = false;
        }
        if (shouldApply) {
            forceAddToBot_AddSemen(card);
        }
    }

    private static void forceAddToBot_AddSemen(AbstractCard card) {
        if (card.type != AbstractCard.CardType.ATTACK) return;
        if (!(card instanceof FuckJob_Card && card instanceof SuperstitioCard))
            addToBot_Cum_Normal();
        else if (card.cardID.contains("Fuck_"))
            addToBot_Cum_Inside();
        else if (card.cardID.contains("Job_"))
            addToBot_Cum_Outside();
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        DevaBody_Lupa.SetPlayerImmunity();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.type == AbstractCard.CardType.ATTACK)
            addToBot_AddSemen(card);
    }

    @Override
    public void updateDescriptionArgs() {
    }

}