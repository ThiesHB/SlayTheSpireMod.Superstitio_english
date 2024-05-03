package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import SuperstitioMod.actions.AutoDoneAction;
import SuperstitioMod.powers.interFace.HasAllCardCostModifyEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class AllCardCostModifier_PerEnergy extends AllCardCostModifier {
    public static final String POWER_ID = DataManager.MakeTextID(AllCardCostModifier_PerEnergy.class.getSimpleName() );

    public AllCardCostModifier_PerEnergy(final AbstractCreature owner, int decreasedCost, int totalEnergy, HasAllCardCostModifyEffect holder) {
        super(POWER_ID, owner, decreasedCost, totalEnergy, holder);
        amount = totalEnergy;
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(decreasedCost, amount, (!isActive() ? powerStringsSet.getRightVersion().DESCRIPTIONS[1] : ""));
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (!isActive()) return;
        if (totalCostDecreased(card) == 0) return;
        AutoDoneAction.addToBotAbstract(() -> {
            this.flash();
            this.amount -= totalCostDecreased(card);
            this.amount = Integer.max(0, amount);
            if (amount < decreasedCost) {
                this.decreasedCost = amount;
                this.CostToOriginAllCards();
                if (amount == 0)
                    this.removeSelf();
                else
                    this.tryUseEffect();
            }
        });
    }

    public int totalCostDecreased(AbstractCard card) {
        if (!costMap.containsKey(card.uuid) || getOriginCost(card) <= card.costForTurn || card.freeToPlayOnce) {
            return 0;
        }
        return getOriginCost(card) - card.costForTurn;
    }

}
