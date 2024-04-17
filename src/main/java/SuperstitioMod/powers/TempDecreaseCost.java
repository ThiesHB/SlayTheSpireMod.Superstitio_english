package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.powers.interFace.HasTempDecreaseCostEffect;
import SuperstitioMod.powers.interFace.OnPostApplyThisPower;
import SuperstitioMod.utils.CardUtility;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * 请使用TempDecreaseCostApplyAction进行添加
 */
public class TempDecreaseCost extends AbstractLupaPower implements NonStackablePower, OnPostApplyThisPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(TempDecreaseCost.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final Map<UUID, Integer> costMap = new HashMap<>();
    private final HasTempDecreaseCostEffect holder;
    public int order = 0;
    private boolean active = false;

    public TempDecreaseCost(final AbstractCreature owner, int amount, HasTempDecreaseCostEffect holder) {
        super(POWER_ID, powerStrings, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF);
        this.holder = holder;
    }

    public static Stream<TempDecreaseCost> getAllTempDecreaseCost(AbstractCreature owner) {
        return owner.powers.stream()
                .filter(power -> Objects.equals(power.ID, TempDecreaseCost.POWER_ID) && power instanceof TempDecreaseCost && power.amount > 0)
                .map(power -> (TempDecreaseCost) power);
    }

    public static void RemoveAllByHolder(HasTempDecreaseCostEffect aimHolder) {
        AllCostModifierPowerByHolder(aimHolder).forEach(TempDecreaseCost::remove);
    }

    public static Stream<TempDecreaseCost> AllCostModifierPowerByHolder(HasTempDecreaseCostEffect aimHolder) {
        return getAllTempDecreaseCost().filter(power -> Objects.equals(power.holder.IDAsHolder(), aimHolder.IDAsHolder()));
    }

    public static Stream<TempDecreaseCost> getAllTempDecreaseCost() {
        return getAllTempDecreaseCost(AbstractDungeon.player);
    }

    public static TempDecreaseCost getActivateOne(AbstractCreature owner) {
        return getAllTempDecreaseCost(owner).filter(TempDecreaseCost::isActive).findAny().orElse(null);
    }

    public static void tryActivateLowestOrder() {
        getAllTempDecreaseCost().filter(TempDecreaseCost::ifIsTheMinOrder).findAny().ifPresent(TempDecreaseCost::activateEffect);
    }

    public static void addToBot_TryActivateLowestOrder() {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                tryActivateLowestOrder();
                isDone = true;
            }
        });
    }

    @Override
    public void onRemove() {
        super.onRemove();
    }

    public boolean ifIsTheMinOrder() {
        return getAllTempDecreaseCost().noneMatch(power -> power.amount != 0 && power.order > this.order);
    }

//    private void removeEffectIfActivate() {
//
//        this.amount = 0;
//    }


    private void activateEffect() {
        SuperstitioModSetup.logger.info("remove TempCostModifier");
        this.setActive();
        tryUseEffect();
        updateDescription();
    }

    /**
     * 可以随意使用
     */
    public void tryUseEffect() {
        if (!this.isActive()) return;
        if (this.amount == 0) return;
        AllCardsCheaper();
    }

    public void remove() {
        SuperstitioModSetup.logger.info("remove TempCostModifier");
        if (this.isActive() && this.amount != 0) {
            AllCardsCostToOrigin();
        }
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        this.amount = 0;
        this.active = false;
        addToBot_TryActivateLowestOrder();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(TempDecreaseCost.powerStrings.DESCRIPTIONS[0]
                + (!isActive() ? TempDecreaseCost.powerStrings.DESCRIPTIONS[1] : ""), amount);
    }

    /**
     * 无可用性检测
     */
    private void AllCardsCheaper() {
        CardUtility.AllCardInBattle().forEach(this::CardCostCheaper);
    }

    private void CardCostCheaper(AbstractCard card) {
        if (card == null)
            return;
        if (card.costForTurn <= 0)
            return;
        if (costMap.keySet().stream().noneMatch(uuidInMap -> card.uuid == uuidInMap))
            costMap.put(card.uuid, card.costForTurn);
        final int newCost = getOriginCost(card) - this.amount;
        if (card.costForTurn == newCost)
            return;
        card.setCostForTurn(Math.max(newCost, 0));
        card.isCostModifiedForTurn = true;
        card.flash();
    }

    private void AllCardsCostToOrigin() {
        CardUtility.AllCardInBattle().forEach(this::ACardCostToOrigin);
        costMap.clear();
    }

    private void ACardCostToOrigin(AbstractCard card) {
        if (card == null)
            return;
        if (getOriginCost(card) < card.costForTurn) return;
        if (!costMap.containsKey(card.uuid))
            return;
        card.flash();

        SuperstitioModSetup.logger.info("card" + card.costForTurn + "costmap" + getOriginCost(card));
        card.setCostForTurn(getOriginCost(card));
        card.isCostModifiedForTurn = false;
    }

    public int getOriginCost(AbstractCard card) {
        if (card == null)
            return 0;
        if (costMap.get(card.uuid) == null)
            return card.cost;
        return costMap.get(card.uuid);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (!this.isActive()) return;

        this.CardCostCheaper(card);
    }

    @Override
    public void InitializePostApplyThisPower() {
        this.order = TempDecreaseCost.getAllTempDecreaseCost().map(p -> p.order).min(Integer::compareTo).orElse(0);
//        if (TempDecreaseCost.getAllTempDecreaseCost().findAny().isPresent()
//                || TempDecreaseCost.getAllTempDecreaseCost().noneMatch(TempDecreaseCost::isActive))
        TempDecreaseCost.addToBot_TryActivateLowestOrder();
    }

    public boolean isActive() {
        return active;
    }

    private void setActive() {
        this.active = true;
    }
}
