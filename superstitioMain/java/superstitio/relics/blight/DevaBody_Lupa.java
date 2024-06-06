package superstitio.relics.blight;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.lupa.OnAddSemenPower;
import superstitio.delayHpLose.DelayHpLosePatch;
import superstitio.delayHpLose.DelayHpLosePower_ApplyOnAttacked;
import superstitio.powers.lupaOnly.FloorSemen;
import superstitio.powers.lupaOnly.InsideSemen;
import superstitio.powers.lupaOnly.OutsideSemen;
import superstitioapi.relicToBlight.BecomeBlight;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.DataUtility;
import superstitioapi.relicToBlight.BlightWithRelic;
import superstitioapi.utils.ActionUtility;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static superstitio.DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType;
import static superstitioapi.utils.ActionUtility.addToTop_applyPower;

@AutoAdd.Seen
public class DevaBody_Lupa extends SuperstitioRelic implements BecomeBlight {
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

    private static void addToBot_AddSemen(AbstractCard card) {
        boolean shouldApply = true;
        for (AbstractPower power : player.powers) {
            if (power instanceof OnAddSemenPower && !((OnAddSemenPower) power).onAddSemen_shouldApply(getSemenType(card)))
                shouldApply = false;
        }
        if (shouldApply) {
            addToTop_applyPower(getSemenType(card));
        }
    }

    private static AbstractPower getSemenType(AbstractCard card) {
        if (card.type != AbstractCard.CardType.ATTACK) return null;
        if (!(card instanceof FuckJob_Card && card instanceof SuperstitioCard))
            return new FloorSemen(player, 1);
        else if (card.cardID.contains("Fuck_"))
            return new InsideSemen(player, 1);
        else if (card.cardID.contains("Job_"))
            return new OutsideSemen(player, 1);
        return new FloorSemen(player, 1);
    }


    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public BlightWithRelic makeNewBlightWithRelic() {
        return new BlightWithRelic_DevaBody_Lupa();
    }

    public static class BlightWithRelic_DevaBody_Lupa extends BlightWithRelic {

        public static final String ID = DataUtility.MakeTextID(BlightWithRelic_DevaBody_Lupa.class);

        public BlightWithRelic_DevaBody_Lupa() {
            super(ID);
        }

        @Override
        public AbstractRelic makeRelic() {
            return new DevaBody_Lupa();
        }

        @Override
        public void atBattleStart() {
            this.flash();
            ActionUtility.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this.relic));
            DevaBody_Lupa.SetPlayerImmunity();
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m) {
            if (card.type == AbstractCard.CardType.ATTACK)
                addToBot_AddSemen(card);
        }

    }

}