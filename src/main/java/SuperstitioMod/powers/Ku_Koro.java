package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.utils.CardUtility;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

/**
 * 每次受到攻击伤害时，获得1随机状态牌。所有消耗牌会回到抽牌堆
 */
public class Ku_Koro extends AbstractLupaPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(Ku_Koro.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public Ku_Koro(final AbstractCreature owner) {
        super(POWER_ID, powerStrings, owner, -1);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(Ku_Koro.powerStrings.DESCRIPTIONS[0]);
    }

    @Override
    public void onExhaust(AbstractCard card) {
        AbstractLupaCard.makeTempCardInBattle(card, AbstractLupaCard.BattleCardPlace.Discard);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.owner != AbstractDungeon.player && info.type == DamageInfo.DamageType.NORMAL) {
            final AbstractCard card = CardUtility.getRandomCurseCard(true, true);
            AbstractLupaCard.makeTempCardInBattle(card, AbstractLupaCard.BattleCardPlace.Hand);
        }

        return super.onAttacked(info, damageAmount);
    }
}
