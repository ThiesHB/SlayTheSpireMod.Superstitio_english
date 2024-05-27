package superstitio.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.InBattleDataManager;
import superstitio.Logger;
import superstitio.powers.patchAndInterface.barIndepend.BarRenderUpdateMessage;
import superstitio.powers.patchAndInterface.barIndepend.HasBarRenderOnCreature_Power;
import superstitio.powers.patchAndInterface.interfaces.HasAllCardCostModifyEffect;
import superstitio.powers.patchAndInterface.interfaces.OnPostApplyThisPower;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleApplyPowerEffect;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleRemovePowerEffect;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleTips;
import superstitio.powers.patchAndInterface.interfaces.orgasm.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static superstitio.InBattleDataManager.OrgasmTimesInTurn;
import static superstitio.InBattleDataManager.OrgasmTimesTotal;
import static superstitio.actions.AutoDoneInstantAction.addToBotAbstract;
import static superstitio.powers.AllCardCostModifier.*;
import static superstitio.utils.PowerUtility.BubbleMessageHigher;

public class SexualHeat extends AbstractSuperstitioPower implements
        HasAllCardCostModifyEffect, OnPostApplyThisPower, HasBarRenderOnCreature_Power,
        InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount,
        InvisiblePower_InvisibleApplyPowerEffect, InvisiblePower_InvisibleRemovePowerEffect,
        OnOrgasm_onSuccessfullyPreventOrgasm, OnOrgasm_onOrgasm, OnOrgasm_onEndOrgasm, OnOrgasm_onSquirt, OnOrgasm_onContinuallyOrgasm {
    public static final String POWER_ID = DataManager.MakeTextID(SexualHeat.class);
    public static final int HEAT_REQUIREDOrigin = 10;
    private static final int DRAW_CARD_INContinueOrgasm = 0;
    //绘制相关
    private static final Color PINK = new Color(1f, 0.7529f, 0.7961f, 1.0f);
    public final Color barOrgasmShadowColor;
    private final Color barShadowColorOrigin;
    private int heatRequired = HEAT_REQUIREDOrigin;

    public SexualHeat(final AbstractCreature owner, final int amount) {
        super(POWER_ID, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
        this.barOrgasmShadowColor = Color.YELLOW.cpy();
        this.barShadowColorOrigin = this.setupBarShadowColor();
    }

    public static boolean isInOrgasm(AbstractCreature creature) {
        Optional<SexualHeat> power = getActiveSexualHeat(creature);
        return power.isPresent() && power.get().isInOrgasm();
    }

    public static Optional<SexualHeat> getActiveSexualHeat(AbstractCreature creature) {
        return Optional.ofNullable((SexualHeat) creature.powers.stream().filter(power -> power instanceof SexualHeat).findAny().orElse(null));
    }

    private static int getOrgasmTimesInTurn() {
        return OrgasmTimesInTurn;
    }

    private static void AddOrgasmTime() {
        OrgasmTimesInTurn++;
        OrgasmTimesTotal++;
    }

    @Override
    public String IDAsHolder() {
        return POWER_ID;
    }

    @Override
    public void InitializePostApplyThisPower(AbstractPower addedPower) {
        CheckOrgasm();
        updateDescription();
    }

    public boolean isInOrgasm() {
        return InBattleDataManager.InOrgasm;
    }

    @Override
    public void onRemove() {
        ForceEndOrgasm();
    }

    @Override
    public BarRenderUpdateMessage makeMessage() {
        return HasBarRenderOnCreature_Power.super.makeMessage()
                .setDetail(bar -> bar.barShadowColor = this.isInOrgasm() ? this.barOrgasmShadowColor : this.barShadowColorOrigin);
    }

    @Override
    public void stackPower(final int stackAmount) {
        if (this.amount < 0) this.amount = 0;
        super.stackPower(stackAmount);
        CheckOrgasm();
    }

    @Override
    public void reducePower(int reduceAmount) {
        if (this.amount < 0) this.amount = 0;
        super.reducePower(reduceAmount);
        CheckEndOrgasm();
    }

    public void CheckOrgasm() {
        OnOrgasm.AllOnOrgasm(owner).forEach(power -> power.onCheckOrgasm(this));
        if (getOrgasmTimesInTurn() >= (this.amount / getHeatRequired())) return;
        this.StartOrgasm();
    }

    private void StartOrgasm() {
        if (OnOrgasm.AllOnOrgasm(owner).anyMatch(power -> power.preventOrgasm(this))) {
            OnOrgasm.AllOnOrgasm(owner).forEach(power -> power.onSuccessfullyPreventOrgasm(this));
            return;
        }
        Orgasm();
        OnOrgasm.AllOnOrgasm(owner).forEach(power -> power.onOrgasm(this));
        if (IsContinueOrgasm())
            OnOrgasm.AllOnOrgasm(owner).forEach(power -> power.onContinuallyOrgasm(this));
        CheckOrgasm();
    }

    private boolean IsContinueOrgasm() {
        return getOrgasmTimesInTurn() > 1;
    }

    private void Orgasm() {
        AddOrgasmTime();
        if (!this.owner.isPlayer) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StunMonsterPower((AbstractMonster) this.owner)));
            return;
        }
        final int decreaseCost = Math.min(getOrgasmTimesInTurn(), AbstractDungeon.player.energy.energyMaster);


        addToBotAbstract(() -> {
            try {
                addTo_Bot_EditAmount_Top_FirstByHolder(this, decreaseCost, power -> {
                    if (power.isPresent())
                        return 1;
                    else
                        return 1;
                }, AllCardCostModifier_PerEnergy.class.getConstructor(AbstractCreature.class, int.class, int.class,
                        HasAllCardCostModifyEffect.class));
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                Logger.error(e);
            }
        });

        InBattleDataManager.InOrgasm = true;
    }

    private void CheckEndOrgasm() {
        if (amount < getHeatRequired()) ForceEndOrgasm();
    }

    private void ForceEndOrgasm() {
        if (!isInOrgasm()) return;
//        OrgasmTimesInTurn = 0;
        OnOrgasm.AllOnOrgasm(owner).filter(power -> !(power instanceof SexualHeat)).forEach(power -> power.onEndOrgasm(this));
        this.onEndOrgasm(this);
        InBattleDataManager.InOrgasm = false;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (!this.isInOrgasm() || !this.owner.isPlayer) return;
        AtomicInteger reduceEnergyAmount = new AtomicInteger();
        this.getActiveEffectHold().ifPresent(power -> reduceEnergyAmount.set(power.getOriginCost(card) - card.costForTurn));
        if (reduceEnergyAmount.get() <= 0) return;
        OnOrgasm.AllOnOrgasm(owner).forEach(power -> power.onSquirt(this, card));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, this.amount));
    }

    private int getHeatRequired() {
        return heatRequired;
    }

    public void setHeatRequired(int heatRequired) {
        this.heatRequired = heatRequired > 0 ? heatRequired : 1;
    }

    @Override
    public Optional<AllCardCostModifier> getActiveEffectHold() {
        return getAllByHolder(this).filter(AllCardCostModifier::isActive).findAny();
    }

    @Override
    public AbstractPower getSelf() {
        return this;
    }

    @Override
    public String uuidOfSelf() {
        return this.ID;
    }

//    @Override
//    public String uuidPointTo() {
//        return OutsideSemen.POWER_ID;
//    }

    @Override
    public float Height() {
        return -20 * Settings.scale;
    }

    @Override
    public Color setupBarOrginColor() {
        return PINK;
    }

    @Override
    public int maxBarAmount() {
        return getHeatRequired();
    }


    @Override
    public String getDescriptionStrings() {
        return powerStrings.getDESCRIPTIONS()[this.owner.isPlayer ? 0 : 1];
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(getHeatRequired(), this.isInOrgasm() ? String.format(powerStrings.getDESCRIPTIONS()[2],
                getOrgasmTimesInTurn()) : "");
    }

    @Override
    public void onOrgasm(SexualHeat SexualHeatPower) {
        addToBotAbstract(() -> BubbleMessageHigher(this, false, powerStrings.getDESCRIPTIONS()[IsContinueOrgasm() ? 4 : 3]));
    }

    @Override
    public void onEndOrgasm(SexualHeat SexualHeatPower) {
        BubbleMessageHigher(this, true, powerStrings.getDESCRIPTIONS()[5]);
        if (!this.owner.isPlayer) return;
        SexualHeat power = this;
        addToBotAbstract(() -> RemoveAllByHolder(power));
    }

    @Override
    public void onSquirt(SexualHeat SexualHeatPower, AbstractCard card) {
        BubbleMessageHigher(this, true, powerStrings.getDESCRIPTIONS()[6]);
    }

    @Override
    public String makeBarText() {
        return "%d/%d" + String.format("(%d)", getOrgasmTimesInTurn());
    }

    @Override
    public void onSuccessfullyPreventOrgasm(SexualHeat SexualHeatPower) {
        BubbleMessageHigher(this, false, powerStrings.getDESCRIPTIONS()[7]);
    }

    @Override
    public void onContinuallyOrgasm(SexualHeat SexualHeatPower) {
        this.addToBot(new DrawCardAction(DRAW_CARD_INContinueOrgasm));
    }
}