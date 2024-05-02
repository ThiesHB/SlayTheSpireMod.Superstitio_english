package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import SuperstitioMod.InBattleDataManager;
import SuperstitioMod.Logger;
import SuperstitioMod.actions.AutoDoneAction;
import SuperstitioMod.powers.interFace.HasAllCardCostModifyEffect;
import SuperstitioMod.powers.interFace.OnOrgasm.*;
import SuperstitioMod.powers.interFace.OnPostApplyThisPower;
import SuperstitioMod.utils.PowerUtility;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class SexualHeat extends AbstractWithBarPower implements
        HasAllCardCostModifyEffect, OnPostApplyThisPower,
        OnOrgasm_afterPrevent, OnOrgasm_afterOrgasm, OnOrgasm_onOrgasm, OnOrgasm_onSquirt {
    public static final String POWER_ID = DataManager.MakeTextID(SexualHeat.class.getSimpleName() + "Power");
    public static final int HEAT_REQUIREDOrigin = 10;
    private static final int DRAW_CARD_INContinueOrgasm = 1;
    //绘制相关
    private static final Color PINK = new Color(1f, 0.7529f, 0.7961f, 1.0f);
    private static final Color BarTextColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public Color barOrgasmShadowColor;
    public int orgasmTime = 0;
    private int heatRequired = HEAT_REQUIREDOrigin;

    public SexualHeat(final AbstractCreature owner, final int amount) {
        super(POWER_ID, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
        this.barOrgasmShadowColor = Color.YELLOW;
    }

    public static boolean isInOrgasm(AbstractCreature creature) {
        Optional<SexualHeat> power = getActiveSexualHeat(creature);
        return power.isPresent() && power.get().isInOrgasm();
    }

    public static Optional<SexualHeat> getActiveSexualHeat(AbstractCreature creature) {
        return Optional.ofNullable((SexualHeat) creature.powers.stream().filter(power -> power instanceof SexualHeat).findAny().orElse(null));
    }

    @Override
    public String IDAsHolder() {
        return POWER_ID;
    }

    @Override
    public void InitializePostApplyThisPower() {
        CheckOrgasm();
        updateDescription();
    }

    public boolean isInOrgasm() {
        return orgasmTime != 0;
    }

    @Override
    public void onRemove() {
        ForceEndOrgasm();
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        if (this.isInOrgasm()) {
            this.barShadowColor = this.barOrgasmShadowColor.cpy();
        }
        else {
            this.barShadowColor = setupBarShadowColor().cpy();
        }
        super.renderAmount(sb, x, y, c);
    }

    @Override
    public void stackPower(final int stackAmount) {
        if (this.amount < 0)
            this.amount = 0;
        super.stackPower(stackAmount);
        CheckOrgasm();
    }

    @Override
    public void reducePower(int reduceAmount) {
        if (this.amount < 0)
            this.amount = 0;
        super.reducePower(reduceAmount);
        CheckEndOrgasm();
    }

    public void CheckOrgasm() {
        OnOrgasm.AllOnOrgasm(owner).forEach(power -> power.onCheckOrgasm(this));
        if (orgasmTime >= (this.amount / getHeatRequired())) return;
        this.StartOrgasm();
    }

    private void StartOrgasm() {
        if (OnOrgasm.AllOnOrgasm(owner).anyMatch(power -> power.preventOrgasm(this))) {
            OnOrgasm.AllOnOrgasm(owner).forEach(power -> power.afterPreventOrgasm(this));
            return;
        }
        Orgasm();
        OnOrgasm.AllOnOrgasm(owner).forEach(power -> power.afterTriggerOrgasm(this));
        CheckOrgasm();
    }

    private void Orgasm() {
        orgasmTime++;
        if (!this.owner.isPlayer) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StunMonsterPower((AbstractMonster) this.owner)));
            return;
        }
        try {
            AllCardCostModifier.addToBot_EditAmount_FirstByHolder(
                    this, this.orgasmTime, power -> power
                            .map(allCardCostModifier -> allCardCostModifier.amount + (this.orgasmTime * this.orgasmTime - (this.orgasmTime - 1) * (this.orgasmTime - 1)))
                            .orElseGet(() -> this.orgasmTime * this.orgasmTime - (this.orgasmTime - 1) * (this.orgasmTime - 1)),
                    AllCardCostModifier_PerEnergy.class.getConstructor(AbstractCreature.class, int.class, int.class, HasAllCardCostModifyEffect.class));
        } catch (NoSuchMethodException e) {
            Logger.error(e);
        }
        InBattleDataManager.InOrgasm = true;
    }

    private void CheckEndOrgasm() {
        if (amount <= getHeatRequired())
            ForceEndOrgasm();
    }

    private void ForceEndOrgasm() {
        this.orgasmTime = 0;
        if (!isInOrgasm()) return;
        OnOrgasm.AllOnOrgasm(owner).forEach(power -> power.onEndOrgasm(this));
        InBattleDataManager.InOrgasm = false;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (!this.isInOrgasm() || !this.owner.isPlayer) return;
        AtomicInteger reduceAmount = new AtomicInteger();
        this.getActiveEffectHold().ifPresent(power -> reduceAmount.set(power.getOriginCost(card) - card.costForTurn));
        if (reduceAmount.get() <= 0) return;
        OnOrgasm.AllOnOrgasm(owner).forEach(power -> power.onSquirt(this, card));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer)
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    private int getHeatRequired() {
        return heatRequired;
    }

    public void setHeatRequired(int heatRequired) {
        this.heatRequired = heatRequired > 0 ? heatRequired : 1;
    }

    @Override
    public Optional<AllCardCostModifier> getActiveEffectHold() {
        return AllCardCostModifier.getAllByHolder(this).filter(AllCardCostModifier::isActive).findAny();
    }

    @Override
    protected float Height() {
        return 0 * Settings.scale;
    }

    @Override
    protected Color setupBarBgColor() {
        return new Color(0f, 0f, 0f, 0.3f);
    }

    @Override
    protected Color setupBarShadowColor() {
        return new Color(0f, 0f, 0f, 0.3f);
    }

    @Override
    protected Color setupBarTextColor() {
        return BarTextColor;
    }

    @Override
    protected Color setupBarOrginColor() {
        return PINK;
    }

    @Override
    protected int maxBarAmount() {
        return getHeatRequired();
    }

    @Override
    public void updateDescriptionArgs() {
        if (this.isInOrgasm())
            setDescriptionArgs(getHeatRequired(), String.format(powerStrings.DESCRIPTIONS[2], this.orgasmTime));
        else
            setDescriptionArgs(getHeatRequired(), "");
    }

    @Override
    public String getDescriptionStrings() {
        return powerStrings.DESCRIPTIONS[this.owner.isPlayer ? 0 : 1];
    }

    @Override
    public void afterTriggerOrgasm(SexualHeat SexualHeatPower) {
        boolean IsContinueOrgasm = orgasmTime > 1;
        if (IsContinueOrgasm)
            this.addToBot(new DrawCardAction(DRAW_CARD_INContinueOrgasm));
        AutoDoneAction.addToBotAbstract(() ->
                PowerUtility.BubbleMessageHigher(this, false, powerStrings.DESCRIPTIONS[IsContinueOrgasm ? 4 : 3]));
    }

    @Override
    public void onEndOrgasm(SexualHeat SexualHeatPower) {
        PowerUtility.BubbleMessageHigher(this, true, powerStrings.DESCRIPTIONS[5]);
        if (!this.owner.isPlayer) return;
        SexualHeat p = this;
        AutoDoneAction.addToBotAbstract(() -> AllCardCostModifier.RemoveAllByHolder(p));
    }

    @Override
    public void onSquirt(SexualHeat SexualHeatPower, AbstractCard card) {
        PowerUtility.BubbleMessageHigher(this, true, powerStrings.DESCRIPTIONS[6]);
    }


    @Override
    public void afterPreventOrgasm(SexualHeat SexualHeatPower) {
        PowerUtility.BubbleMessageHigher(this, false, powerStrings.DESCRIPTIONS[7]);
    }
}