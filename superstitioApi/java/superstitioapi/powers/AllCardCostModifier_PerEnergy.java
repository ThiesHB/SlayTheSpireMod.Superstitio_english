package superstitioapi.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitioapi.DataUtility;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.powers.interfaces.HasAllCardCostModifyEffect;
import superstitioapi.renderManager.inBattleManager.InBattleDataManager;


public class AllCardCostModifier_PerEnergy extends AllCardCostModifier {
    public static final String POWER_ID = DataUtility.MakeTextID(AllCardCostModifier_PerEnergy.class);
    public static PowerStrings powerStrings = SuperstitioApiPower.getPowerStrings(POWER_ID);

    public AllCardCostModifier_PerEnergy(final AbstractCreature owner, int decreasedCost, int totalEnergy, HasAllCardCostModifyEffect holder) {
        super(POWER_ID, owner, decreasedCost, totalEnergy, holder);
        amount = totalEnergy;
    }

    public int totalCostDecreased(AbstractCard card) {
        if (!InBattleDataManager.costMap.containsKey(card.uuid) || getOriginCost(card) <= card.costForTurn || card.freeToPlay()) {
            return 0;
        }
        return getOriginCost(card) - card.costForTurn;
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(decreasedCost, amount, (!isActive() ? powerStrings.DESCRIPTIONS[1] : ""));
    }

    @Override
    public String getDescriptionStrings() {
        return powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (!isActive()) return;
        if (totalCostDecreased(card) == 0) return;
        AutoDoneInstantAction.addToBotAbstract(() -> {
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

}
