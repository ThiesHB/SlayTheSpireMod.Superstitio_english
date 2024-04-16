package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.powers.interFace.HasTempDecreaseCostEffect;
import SuperstitioMod.utils.CardUtility;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class TempDecreaseCost extends AbstractPower implements NonStackablePower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(TempDecreaseCost.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final Map<UUID, Integer> costMap = new HashMap<>();
    private final HasTempDecreaseCostEffect holder;
    public int order = 0;
    private boolean activate = false;

    public TempDecreaseCost(final AbstractCreature owner, HasTempDecreaseCostEffect holder, int amount) {
        this(owner, holder, amount, getAllTempDecreaseCost(owner).map(power -> power.order).max(Integer::compare).orElse(0));
    }

    public TempDecreaseCost(final AbstractCreature owner, HasTempDecreaseCostEffect holder, int amount, int order) {
        this.name = TempDecreaseCost.powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.holder = holder;

        this.type = PowerType.BUFF;

        this.amount = amount;
        // 添加一大一小两张能力图
        String path128 = SuperstitioModSetup.makeImgFilesPath_Power("default84");
        String path48 = SuperstitioModSetup.makeImgFilesPath_Power("default32");
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();

        if (!getAllTempDecreaseCost().findAny().isPresent()) {
            this.activateEffect();
            return;
        }
        this.order = order;
    }

    private static Stream<TempDecreaseCost> getAllTempDecreaseCost(AbstractCreature owner) {
        return owner.powers.stream()
                .filter(power -> Objects.equals(power.ID, TempDecreaseCost.POWER_ID) && power instanceof TempDecreaseCost && power.amount > 0)
                .map(power -> (TempDecreaseCost) power);
    }

    public static TempDecreaseCost getActivateOne(AbstractCreature owner) {
        return getAllTempDecreaseCost(owner).filter(power -> power.activate).findAny().orElse(null);
    }

    private Stream<TempDecreaseCost> getAllTempDecreaseCost() {
        return this.owner.powers.stream()
                .filter(power -> Objects.equals(power.ID, this.ID) && power instanceof TempDecreaseCost)
                .map(power -> (TempDecreaseCost) power);
    }

    public boolean ifIsTheMinOrder() {
        return getAllTempDecreaseCost().noneMatch(power -> power.order > this.order);
    }

    private void activateEffect() {
        if (this.activate)
            return;
        this.activate = true;
        AllCardsCheaper();
    }

//    private void removeEffectIfActivate() {
//
//        this.amount = 0;
//    }

    public void remove() {
        SuperstitioModSetup.logger.info("remove TempCost");
        AllCardsCostToOrigin();
        if (this.activate && this.amount != 0)
            getAllTempDecreaseCost().filter(TempDecreaseCost::ifIsTheMinOrder).findAny().ifPresent(TempDecreaseCost::activateEffect);
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(TempDecreaseCost.powerStrings.DESCRIPTIONS[0], amount);
    }

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
        if (!this.activate) return;

        this.CardCostCheaper(card);
    }

//    @Override
//    public void onPlayCard(AbstractCard card, AbstractMonster m) {
//        if (!this.activate) return;
////        holder.TempCost_OnPlayCard(card, m);
//        //ACardCostToOrigin(card);
//    }
}
