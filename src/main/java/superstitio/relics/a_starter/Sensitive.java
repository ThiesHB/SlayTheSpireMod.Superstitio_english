package superstitio.relics.a_starter;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.powers.SexualHeat;
import superstitio.relics.AbstractLupaRelic;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

@AutoAdd.Seen
public class Sensitive extends AbstractLupaRelic {
    public static final String ID = DataManager.MakeTextID(Sensitive.class.getSimpleName());
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int SexualHeatRate = 2;
    private int hpLose = 0;

    public Sensitive() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

//    @Override
//    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
//        if (CardUtility.isNotInBattle()) return super.onAttackedToChangeDamage(info, damageAmount);
//        if (info.type == DamageInfo.DamageType.HP_LOSS) return super.onAttackedToChangeDamage(info, damageAmount);
//        if (damageAmount == 0) return super.onAttackedToChangeDamage(info, damageAmount);
//        ActionUtility.addToBot_applyPower(new SexualDamage_ByEnemy(player, damageAmount, info.owner));
//        return super.onAttackedToChangeDamage(info, 0);
//    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        int amount = 0;
        if (card.costForTurn >= 1)
            amount += card.costForTurn;
        if (card.costForTurn == -1)
            amount += card.energyOnUse;
        if (amount == 0) return;
        this.addToTop(new ApplyPowerAction(player, player, new SexualHeat(player, amount * SexualHeatRate)));
    }
    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(SexualHeatRate);
    }
//
//    @Override
//    public void atBattleStart() {
//        this.hpLose = 0;
//    }
//
////    @Override
////    public int onAttacked(DamageInfo info, int damageAmount) {
////
////        return super.onAttacked(info, damageAmount);
////    }
//
//    @Override
//    public void atTurnStart() {
//        if (this.hpLose != 0) {
//            addToBot(new HealAction(player, player, this.hpLose));
//            ActionUtility.addToBot_applyPower(new SexualDamage_ByEnemy(player, hpLose, player));
//        }
//        this.hpLose = 0;
//    }
//
//    @Override
//    public void onVictory() {
//        if (this.hpLose != 0)
//            addToBot(new HealAction(player, player, this.hpLose));
//        this.hpLose = 0;
//    }
//
//
//
//    @Override
//    public int betterOnLoseHp(DamageInfo damageInfo, int i) {
//        if (DamageModifierManager.getDamageMods(damageInfo).stream().noneMatch(damageModifier -> Objects.equals(damageModifier, new UnBlockAbleDamage())))
//            this.hpLose += i;
//        return i;
//    }
//
//    @Override
//    public boolean onPlayerDeath(AbstractPlayer abstractPlayer, DamageInfo damageInfo) {
//        return DamageModifierManager.getDamageMods(damageInfo).stream().anyMatch(damageModifier -> Objects.equals(damageModifier, new UnBlockAbleDamage()));
//    }
}