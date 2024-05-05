package superstitio.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.utils.ActionUtility;
import superstitio.utils.CardUtility;

/**
 * 每次受到攻击伤害时，获得1随机状态牌。所有消耗牌会回到抽牌堆
 */
public class Ku_Koro extends AbstractLupaPower {
    public static final String POWER_ID = DataManager.MakeTextID(Ku_Koro.class.getSimpleName());

    public Ku_Koro(final AbstractCreature owner) {
        super(POWER_ID, owner, -1);
    }

    @Override
    public void onExhaust(AbstractCard card) {
        ActionUtility.addToBot_makeTempCardInBattle(card, AbstractLupaCard.BattleCardPlace.Discard);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.owner != AbstractDungeon.player && info.type == DamageInfo.DamageType.NORMAL) {
            final AbstractCard card = CardUtility.getRandomCurseCard(true, true);
            ActionUtility.addToBot_makeTempCardInBattle(card, AbstractLupaCard.BattleCardPlace.Hand);
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void updateDescriptionArgs() {

    }
}
