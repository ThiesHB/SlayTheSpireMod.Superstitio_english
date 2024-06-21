package superstitio.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import superstitio.DataManager;
import superstitio.Logger;
import superstitio.SuperstitioConfig;
import superstitio.SuperstitioImg;
import superstitio.powers.patchAndInterface.interfaces.orgasm.*;
import superstitio.powers.sexualHeatNeedModifier.RefractoryPeriod;
import superstitio.powers.sexualHeatNeedModifier.SexualHeatNeedModifier;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.powers.AllCardCostModifier;
import superstitioapi.powers.AllCardCostModifier_PerEnergy;
import superstitioapi.powers.barIndepend.*;
import superstitioapi.powers.interfaces.HasAllCardCostModifyEffect;
import superstitioapi.powers.interfaces.OnPostApplyThisPower;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleApplyPowerEffect;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleRemovePowerEffect;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleTips;
import superstitioapi.utils.PowerUtility;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static superstitio.InBattleDataManager.OrgasmTimesInTurn;
import static superstitio.InBattleDataManager.OrgasmTimesTotal;
import static superstitioapi.actions.AutoDoneInstantAction.addToBotAbstract;
import static superstitioapi.powers.AllCardCostModifier.*;
import static superstitioapi.shader.heart.HeartStreamShader.RenderHeartStream.addToBot_addHeartStreamEffect;
import static superstitioapi.shader.ShaderUtility.canUseShader;
import static superstitioapi.utils.ActionUtility.VoidSupplier;

@SuperstitioImg.NoNeedImg
public class SexualHeat extends AbstractSuperstitioPower implements
        HasAllCardCostModifyEffect, OnPostApplyThisPower<SexualHeat>, HasBarRenderOnCreature_Power,
        InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount,
        InvisiblePower_InvisibleApplyPowerEffect, InvisiblePower_InvisibleRemovePowerEffect,
        OnOrgasm_onSuccessfullyPreventOrgasm, OnOrgasm_onOrgasm, OnOrgasm_onEndOrgasm,
        OnOrgasm_onSquirt, OnOrgasm_onContinuallyOrgasm {
    public static final String POWER_ID = DataManager.MakeTextID(SexualHeat.class);
    public static final int HEAT_REQUIREDOrigin = 10;
    public static final float HEIGHT = 55 * Settings.scale;
    public static final int MIN_HEAT_REQUIRE = 2;
    private static final int DRAW_CARD_INContinueOrgasm = 0;
    //绘制相关
    private static final Color PINK = new Color(1f, 0.7529f, 0.7961f, 1.0f);
    public final Color barOrgasmShadowColor;
    private final Color barShadowColorOrigin;
    private int heatAmount;

    protected SexualHeat(final AbstractCreature owner, final int heatAmount) {
        super(POWER_ID, owner, -1, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
        this.heatAmount = heatAmount;
        this.barOrgasmShadowColor = Color.YELLOW.cpy();
        this.barOrgasmShadowColor.a = 0.6f;
        this.barShadowColorOrigin = this.setupBarShadowColor();
    }

    public static int getMAX_ORGASM_HEART_STREAM_LEVEL() {
        if (SuperstitioConfig.isEnablePerformanceMode())
            return 3;
        else
            return 8;
    }

    public static void addToBot_addSexualHeat(AbstractCreature target, int heatAmount) {
        addAction_addSexualHeat(target, heatAmount, AutoDoneInstantAction::addToBotAbstract);
    }

    public static void addAction_addSexualHeat(AbstractCreature target, int heatAmount,
                                               Consumer<VoidSupplier> action) {
        if (heatAmount < 0) {
            Logger.warning("add error number " + SexualHeat.class.getSimpleName() + ". Amount: " + heatAmount);
            return;
        }
        Optional<SexualHeat> activeSexualHeat = getActiveSexualHeat(target);
        if (activeSexualHeat.isPresent()) {
            action.accept(() -> activeSexualHeat.get().addSexualHeat(heatAmount));
        } else {
            SexualHeat sexualHeat = new SexualHeat(target, 0);
            action.accept(() -> {
                target.addPower(sexualHeat);
                sexualHeat.addSexualHeat(heatAmount);
            });
        }
    }

    public static void addToBot_reduceSexualHeat(AbstractCreature target, int heatAmount) {
        addAction_reduceSexualHeat(target, heatAmount, AutoDoneInstantAction::addToBotAbstract);
    }

    public static void addAction_reduceSexualHeat(AbstractCreature target, int heatAmount, Consumer<VoidSupplier> action) {
        if (heatAmount < 0) {
            Logger.warning("reduce error number " + SexualHeat.class.getSimpleName() + ". Amount: " + heatAmount);
            return;
        }
        getActiveSexualHeat(target).ifPresent(power ->
                action.accept(() -> power.reduceSexualHeat(heatAmount)));
    }

    public static boolean isInOrgasm(AbstractCreature creature) {
        Optional<SexualHeat> power = getActiveSexualHeat(creature);
        return power.isPresent() && power.get().isInOrgasm();
    }

    public static Optional<SexualHeat> getActiveSexualHeat(AbstractCreature creature) {
        return Optional.ofNullable((SexualHeat) creature.powers.stream()
                .filter(power -> power instanceof SexualHeat).findAny().orElse(null));
    }

    private static int getOrgasmTimesInTurn() {
        return OrgasmTimesInTurn;
    }

    private static void AddOrgasmTime() {
        OrgasmTimesInTurn++;
        OrgasmTimesTotal++;
    }

    private static void addToBot_addOrgasmEffect(SexualHeat sexualHeat) {
        addToBot_addHeartStreamEffect(getMAX_ORGASM_HEART_STREAM_LEVEL(), () -> !Orgasm.isPlayerInOrgasm(),
                () -> sexualHeat.owner.hb);
    }

    @Override
    public Hitbox getBarRenderHitBox() {
        if (!(owner instanceof AbstractPlayer)) return owner.hb;
        Hitbox tipHitbox = ReflectionHacks.getPrivate(AbstractDungeon.overlayMenu.energyPanel, EnergyPanel.class, "tipHitbox");
        if (tipHitbox != null) return tipHitbox;
        Logger.warning("no EnergyPanel found when " + this + "get hitbox.");
        return owner.hb;
        //        return EnergyPanel.tipHitbox;
    }

    protected void addSexualHeat(int heatAmount) {
        this.heatAmount += heatAmount;
        if (this.heatAmount < 0)
            this.heatAmount = 0;
        CheckOrgasm();
        updateDescription();
        AbstractDungeon.onModifyPower();
    }

    protected void reduceSexualHeat(int heatAmount) {
        this.heatAmount -= heatAmount;
        if (this.heatAmount < 0)
            this.heatAmount = 0;
        CheckEndOrgasm();
        updateDescription();
        AbstractDungeon.onModifyPower();
    }

    @Override
    public int getAmountForDraw() {
        return this.heatAmount;
    }

    @Override
    public String IDAsHolder() {
        return POWER_ID;
    }

    @Override
    public void InitializePostApplyThisPower(SexualHeat addedPower) {
        CheckOrgasm();
        updateDescription();
    }

    public boolean isInOrgasm() {
        return Orgasm.isInOrgasm(owner);
    }

    @Override
    public void onRemove() {
        ForceEndOrgasm();
    }

    @Override
    public BarRenderUpdateMessage makeMessage() {
        return HasBarRenderOnCreature_Power.super.makeMessage()
                .setDetail(bar -> {
                    if (bar instanceof BarRenderOnThing)
                        ((BarRenderOnThing) bar).barShadowColor = this.isInOrgasm() ?
                                this.barOrgasmShadowColor : this.barShadowColorOrigin;
                });
    }

//    @Override
//    public void stackPower(final int stackAmount) {
//        if (this.amount < 0) this.amount = 0;
//        super.stackPower(stackAmount);
//        CheckOrgasm();
//    }
//
//    @Override
//    public void reducePower(int reduceAmount) {
//        if (this.amount < 0) this.amount = 0;
//        super.reducePower(reduceAmount);
//        CheckEndOrgasm();
//    }

    @Override
    public BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, ? extends RenderOnThing> makeNewBarRenderOnCreature() {
        return (hitbox, power) -> {
            if (canUseShader && this.owner instanceof AbstractPlayer)
                return new BarRenderOnThing_Ring_Text(hitbox, power);
            else
                return new BarRenderOnThing(hitbox, power);
        };
    }

    public void CheckOrgasm() {
        OnOrgasm.AllOnOrgasm(owner).forEach(power -> power.onCheckOrgasm(this));
        if (getOrgasmTimesInTurn() >= (this.heatAmount / getHeatRequired())) return;
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
//            addToBot_reduceSexualHeat(this.owner, this.heatAmount);
            this.reduceSexualHeat(this.heatAmount);
            ForceEndOrgasm();
            addToBot_applyPower(new RefractoryPeriod(this.owner, HEAT_REQUIREDOrigin));
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

        Orgasm.startOrgasm(owner);
    }

    private void CheckEndOrgasm() {
        if (this.heatAmount < getHeatRequired()) ForceEndOrgasm();
    }

    private void ForceEndOrgasm() {
        if (!isInOrgasm()) return;
//        OrgasmTimesInTurn = 0;
        OnOrgasm.AllOnOrgasm(owner).filter(power -> !(power instanceof SexualHeat)).forEach(power -> power.onEndOrgasm(this));
        this.onEndOrgasm(this);
        Orgasm.endOrgasm(owner);
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
        if (!isPlayer) return;
        this.reduceSexualHeat(this.heatAmount);
        ForceEndOrgasm();
//            this.addToBot_removeSpecificPower(this);
//            this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, this.amount));
    }

    private int getHeatRequired() {
        return Math.max(HEAT_REQUIREDOrigin -
                this.owner.powers.stream().filter(power -> power instanceof SexualHeatNeedModifier)
                        .mapToInt(power -> ((SexualHeatNeedModifier) power).reduceSexualHeatNeeded()).sum(), MIN_HEAT_REQUIRE);
    }

//    public void setHeatRequired(int heatRequired) {
//        this.heatRequired = heatRequired > 0 ? heatRequired : MIN_HEAT_REQUIRE;
//    }

    @Override
    public Optional<AllCardCostModifier> getActiveEffectHold() {
        return getAllByHolder(this).filter(AllCardCostModifier::isActive).findAny();
    }

    @Override
    public AbstractPower getSelf() {
        return this;
    }

//    @Override
//    public String uuidPointTo() {
//        return OutsideSemen.POWER_ID;
//    }

    @Override
    public float Height() {
        return HEIGHT;
    }

    @Override
    public Color setupBarOrginColor() {
        return PINK;
    }

    @Override
    public int maxBarAmount() {
        return getHeatRequired();
    }

    private void bubbleMessage(boolean isDeBuffVer, int messageIndex) {
        bubbleMessage(isDeBuffVer, powerStrings.getDESCRIPTIONS()[messageIndex]);
    }

    private void bubbleMessage(boolean isDeBuffVer, String message) {
        PowerUtility.BubbleMessage(this.getBarRenderHitBox(), isDeBuffVer, message, -this.owner.hb.width / 2,
                PowerUtility.BubbleMessageHigher_HEIGHT + Height());
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
        addToBotAbstract(() -> bubbleMessage(false, IsContinueOrgasm() ? 4 : 3));
        if (!isInOrgasm() || !(this.owner instanceof AbstractPlayer)) return;
        addToBot_addOrgasmEffect(this);
    }

    @Override
    public void onEndOrgasm(SexualHeat SexualHeatPower) {
        bubbleMessage(true, 5);
        if (!this.owner.isPlayer) return;
        SexualHeat power = this;
        addToBotAbstract(() -> RemoveAllByHolder(power));
    }

    @Override
    public void onSquirt(SexualHeat SexualHeatPower, AbstractCard card) {
        bubbleMessage(false, 6);
    }

    @Override
    public String makeBarText() {
        return "%d/%d" + String.format("(%d)", getOrgasmTimesInTurn());
    }

    @Override
    public void onSuccessfullyPreventOrgasm(SexualHeat SexualHeatPower) {
        bubbleMessage(false, 7);
    }

    @Override
    public void onContinuallyOrgasm(SexualHeat SexualHeatPower) {
        this.addToBot(new DrawCardAction(DRAW_CARD_INContinueOrgasm));
    }


    public static class Orgasm {
        public static void endOrgasm(AbstractCreature creature) {
            OrgasmField.isInOrgasm.set(creature, false);
        }

        public static boolean isPlayerInOrgasm() {
            return OrgasmField.isInOrgasm.get(AbstractDungeon.player);
        }

        public static void startOrgasm(AbstractCreature creature) {
            OrgasmField.isInOrgasm.set(creature, true);
        }

        public static boolean isInOrgasm(AbstractCreature creature) {
            return OrgasmField.isInOrgasm.get(creature);
        }

        @SpirePatch(
                clz = AbstractCreature.class,
                method = "<class>"
        )
        private static class OrgasmField {
            public static SpireField<Boolean> isInOrgasm = new SpireField<>(() -> false);
        }
    }
}