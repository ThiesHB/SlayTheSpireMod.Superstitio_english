package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import SuperstitioMod.InBattleDataManager;
import SuperstitioMod.actions.AutoDoneAction;
import SuperstitioMod.powers.interFace.HasAllCardCostModifyEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class AllCardCostModifier_PerCard extends AllCardCostModifier {
    public static final String POWER_ID = DataManager.MakeTextID(AllCardCostModifier_PerCard.class.getSimpleName() );

    public AllCardCostModifier_PerCard(final AbstractCreature owner, int decreasedCost, int useTimes, HasAllCardCostModifyEffect holder) {
        super(POWER_ID, owner, decreasedCost, useTimes, holder);
        amount = useTimes;
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(decreasedCost, amount,(!isActive() ? powerStringsSet.getRightVersion().DESCRIPTIONS[1] : ""));
    }


    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (!isActive()) return;
        if (!isCostDecreased(card)) return;
        AutoDoneAction.addToBotAbstract(() -> {
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
