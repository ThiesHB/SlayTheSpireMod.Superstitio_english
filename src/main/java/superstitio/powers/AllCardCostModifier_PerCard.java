package superstitio.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.InBattleDataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.powers.interfaces.HasAllCardCostModifyEffect;


public class AllCardCostModifier_PerCard extends AllCardCostModifier {
    public static final String POWER_ID = DataManager.MakeTextID(AllCardCostModifier_PerCard.class.getSimpleName());

    public AllCardCostModifier_PerCard(final AbstractCreature owner, int decreasedCost, int useTimes, HasAllCardCostModifyEffect holder) {
        super(POWER_ID, owner, decreasedCost, useTimes, holder);
        amount = useTimes;
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(decreasedCost, amount, (!isActive() ? powerStrings.getRightVersion().DESCRIPTIONS[1] : ""));
    }


    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (!isActive()) return;
        if (!isCostDecreased(card)) return;
        AutoDoneInstantAction.addToBotAbstract(() -> {
            this.flash();
            this.amount--;
            this.amount = Integer.max(0, amount);
            if (amount == 0)
                removeSelf();
        });
    }

    public boolean isCostDecreased(AbstractCard card) {
        return InBattleDataManager.costMap.containsKey(card.uuid) && getOriginCost(card) < card.costForTurn && !card.freeToPlayOnce;
    }
}
