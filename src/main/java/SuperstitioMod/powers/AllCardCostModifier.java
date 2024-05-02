package SuperstitioMod.powers;

import SuperstitioMod.Logger;
import SuperstitioMod.actions.AutoDoneAction;
import SuperstitioMod.powers.interFace.HasAllCardCostModifyEffect;
import SuperstitioMod.powers.interFace.OnPostApplyThisPower;
import SuperstitioMod.utils.ActionUtility;
import SuperstitioMod.utils.CardUtility;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;


public abstract class AllCardCostModifier extends AbstractLupaPower implements NonStackablePower, OnPostApplyThisPower {
    public static final Map<UUID, Integer> costMap = new HashMap<>();
    private final HasAllCardCostModifyEffect holder;
    public int order = 0;
    public int decreasedCost;
    private boolean active = false;

    public AllCardCostModifier(String id, final AbstractCreature owner, int decreasedCost, int useAmount, HasAllCardCostModifyEffect holder) {
        super(id, owner, useAmount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF);
        this.decreasedCost = decreasedCost;
        this.holder = holder;
    }

    public static void RemoveAllByHolder(HasAllCardCostModifyEffect aimHolder) {
        getAllByHolder(aimHolder).forEach(AllCardCostModifier::removeSelf);
    }

    public static Stream<AllCardCostModifier> getAllByHolder(HasAllCardCostModifyEffect aimHolder) {
        return getAll().filter(power -> Objects.equals(power.holder.IDAsHolder(), aimHolder.IDAsHolder()));
    }

    public static Stream<AllCardCostModifier> getAll() {
        return AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof AllCardCostModifier && power.amount > 0)
                .map(power -> (AllCardCostModifier) power);
    }

    public static Optional<AllCardCostModifier> getActivateOne() {
        return getAll().filter(AllCardCostModifier::isActive).findAny();
    }

    public static void addToBot_TryActivateLowestOrder() {
        AutoDoneAction.addToBotAbstract(() ->
                getAll().filter(AllCardCostModifier::ifIsTheMinOrder).findAny()
                        .ifPresent(AllCardCostModifier::activateEffect));
    }

    public static <T extends AllCardCostModifier> void addToBot_AddNew(HasAllCardCostModifyEffect holder, int decreasedCost, int canUseAmount, Constructor<T> powerType) {
        try {
            ActionUtility.addToBot_applyPowerToPlayer(powerType.newInstance(AbstractDungeon.player, decreasedCost, canUseAmount, holder));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends AllCardCostModifier> void addToBot_EditAmount_FirstByHolder(HasAllCardCostModifyEffect holder, int decreasedCost, int canUseAmount, Constructor<T> powerType) {
        addToBot_EditAmount_FirstByHolder(holder, decreasedCost, power -> canUseAmount, powerType);
    }

    public static <T extends AllCardCostModifier> void addToBot_EditAmount_FirstByHolder(
            HasAllCardCostModifyEffect holder, int decreasedCost, Function<Optional<AllCardCostModifier>, Integer> canUseAmountProcessor, Constructor<T> powerType) {
        AllCardCostModifier allCardCostModifier = getAllByHolder(holder).findAny().orElse(null);
        if (allCardCostModifier == null) {
            addToBot_AddNew(holder, decreasedCost, canUseAmountProcessor.apply(Optional.empty()), powerType);
            return;
        }
        final AllCardCostModifier finalAllCardCostModifier = allCardCostModifier;
        AutoDoneAction.addToBotAbstract(() -> {
            finalAllCardCostModifier.amount = canUseAmountProcessor.apply(Optional.of(finalAllCardCostModifier));
            finalAllCardCostModifier.decreasedCost = decreasedCost;
        });
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        if (this.decreasedCost <= 0) return;
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.decreasedCost), x, y + 15.0F * Settings.scale, this.fontScale, c);
    }

    public boolean ifIsTheMinOrder() {
        return getAll().noneMatch(power -> power.amount != 0 && power.order > this.order);
    }

    private void activateEffect() {
        Logger.info("add CostModifier");
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
        CheaperAllCards();
    }

    public void removeSelf() {
        Logger.info("remove CostModifier");
        if (this.isActive() && this.amount != 0) {
            CostToOriginAllCards();
        }
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        this.amount = 0;
        this.active = false;
        addToBot_TryActivateLowestOrder();
    }

    /**
     * 无可用性检测
     */
    private void CheaperAllCards() {
        CardUtility.AllCardInBattle().forEach(this::CheaperOneCard);
    }

    private void CheaperOneCard(AbstractCard card) {
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
        CardUtility.flashIfInHand(card);
    }

    protected void CostToOriginAllCards() {
        CardUtility.AllCardInBattle().forEach(this::CostToOriginOneCard);
        costMap.clear();
    }

    private void CostToOriginOneCard(AbstractCard card) {
        if (card == null)
            return;
        if (getOriginCost(card) < card.costForTurn) return;
        if (!costMap.containsKey(card.uuid))
            return;
        CardUtility.flashIfInHand(card);

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

        this.CheaperOneCard(card);
    }

    @Override
    public void InitializePostApplyThisPower() {
        this.order = AllCardCostModifier.getAll().map(p -> p.order).min(Integer::compareTo).orElse(0);
//        if (TempDecreaseCost.getAllTempDecreaseCost().findAny().isPresent()
//                || TempDecreaseCost.getAllTempDecreaseCost().noneMatch(TempDecreaseCost::isActive))
        AllCardCostModifier.addToBot_TryActivateLowestOrder();
    }

    public boolean isActive() {
        return active;
    }

    private void setActive() {
        this.active = true;
    }
}
