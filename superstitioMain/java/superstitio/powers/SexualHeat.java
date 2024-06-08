package superstitio.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import superstitio.DataManager;
import superstitio.InBattleDataManager;
import superstitio.Logger;
import superstitio.SuperstitioImg;
import superstitio.powers.patchAndInterface.interfaces.orgasm.*;
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
import superstitioapi.utils.RenderInBattle;
import superstitioapi.utils.ShaderUtility;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static superstitio.InBattleDataManager.OrgasmTimesInTurn;
import static superstitio.InBattleDataManager.OrgasmTimesTotal;
import static superstitioapi.actions.AutoDoneInstantAction.addToBotAbstract;
import static superstitioapi.powers.AllCardCostModifier.*;
import static superstitioapi.utils.ActionUtility.VoidSupplier;
import static superstitioapi.utils.ShaderUtility.*;

@SuperstitioImg.NoNeedImg
public class SexualHeat extends AbstractSuperstitioPower implements
        HasAllCardCostModifyEffect, OnPostApplyThisPower, HasBarRenderOnCreature_Power,
        InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount,
        InvisiblePower_InvisibleApplyPowerEffect, InvisiblePower_InvisibleRemovePowerEffect,
        OnOrgasm_onSuccessfullyPreventOrgasm, OnOrgasm_onOrgasm, OnOrgasm_onEndOrgasm,
        OnOrgasm_onSquirt, OnOrgasm_onContinuallyOrgasm {
    public static final String POWER_ID = DataManager.MakeTextID(SexualHeat.class);
    public static final int HEAT_REQUIREDOrigin = 10;
    public static final float HEIGHT = 55 * Settings.scale;
    private static final int DRAW_CARD_INContinueOrgasm = 0;
    //绘制相关
    private static final Color PINK = new Color(1f, 0.7529f, 0.7961f, 1.0f);
    public final Color barOrgasmShadowColor;
    private final Color barShadowColorOrigin;
    private int heatRequired = HEAT_REQUIREDOrigin;
    private int heatAmount;

    protected SexualHeat(final AbstractCreature owner, final int heatAmount) {
        super(POWER_ID, owner, -1, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);
        this.heatAmount = heatAmount;
        this.barOrgasmShadowColor = Color.YELLOW.cpy();
        this.barOrgasmShadowColor.a = 0.6f;
        this.barShadowColorOrigin = this.setupBarShadowColor();
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
        }
        else {
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
                .setDetail(bar -> {
                    if (bar instanceof BarRenderOnThing)
                        ((BarRenderOnThing) bar).barShadowColor = this.isInOrgasm() ?
                                this.barOrgasmShadowColor : this.barShadowColorOrigin;
                });
    }

    @Override
    public BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, ? extends RenderOnThing> makeNewBarRenderOnCreature() {
        return (hitbox, power) -> {
            if (canUseShader && this.owner instanceof AbstractPlayer)
                return new BarRenderOnThing_Ring_Text(hitbox, power);
            else
                return new BarRenderOnThing(hitbox, power);
        };
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
            addToBot_reduceSexualHeat(this.owner, getHeatRequired());
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
        if (this.heatAmount < getHeatRequired()) ForceEndOrgasm();
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
        if (!isPlayer) return;
        this.reduceSexualHeat(this.heatAmount);
        ForceEndOrgasm();
//            this.addToBot_removeSpecificPower(this);
//            this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, this.amount));
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
    public float Height() {
        return HEIGHT;
    }

//    @Override
//    public String uuidPointTo() {
//        return OutsideSemen.POWER_ID;
//    }

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
        PowerUtility.BubbleMessage(this.getBarRenderHitBox(), isDeBuffVer, message, -this.owner.hb.width / 2, PowerUtility.BubbleMessageHigher_HEIGHT + Height());
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
        RenderOrgasm.addToBot_addOrgasmEffect(this);
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

    public static class RenderOrgasm implements RenderInBattle {
        SexualHeat sexualHeat;
        private float level;
        private float levelTarget;

        private ArrayList<SubRender> subRenders = new ArrayList<>();

        private RenderOrgasm(SexualHeat sexualHeat) {
            this.sexualHeat = sexualHeat;
            Logger.info("addRender" + this);
            level = 3.0f;
            levelTarget = 3.5f;
            levelUp();
        }

        public static void addToBot_addOrgasmEffect(SexualHeat sexualHeat) {
            if (!canUseShader) return;
            List<RenderInBattle> renderGroup =
                    RenderInBattle.getRenderGroup(RenderType.Stance);
            Optional<RenderOrgasm> renderOrgasm = renderGroup.stream()
                    .filter(renderInBattle -> renderInBattle instanceof RenderOrgasm)
                    .map(renderInBattle -> (RenderOrgasm) renderInBattle)
                    .findAny();
            if (renderOrgasm.isPresent())
                addToBotAbstract(() ->
                        renderOrgasm.get().levelUp());
            else
                RenderInBattle.Register(RenderType.Stance, new RenderOrgasm(sexualHeat));

        }

        private void levelUp() {
            levelTarget += 1.0f;
            if (subRenders.size() >= 12) return;
            int newIndex = (int) Math.min(12, Math.ceil(level));
            subRenders.add(new SubRender(
                    0.1f + 0.1f * newIndex,
                    1.4f - 0.08f * newIndex,
                    new Vector2(4 + newIndex, 4 + newIndex)));
        }


        @Override
        public void render(SpriteBatch sb) {
            ShaderUtility.originShader = sb.getShader();
            float density = (float) (1.0f / Math.pow(level, 0.5f));

            this.subRenders.forEach(subRender ->
                    subRender.drawHeartStream(sb, (float) (0.8 * density),
                            new Vector2((Gdx.graphics.getWidth() / 2.0f - this.sexualHeat.owner.drawX) / Gdx.graphics.getWidth(), 0.0f)));
        }

        @Override
        public boolean shouldRemove() {
            if (!InBattleDataManager.InOrgasm && subRenders.stream().allMatch(subRender -> subRender.anim_timer < 0.0f))
                return true;
            return false;
        }

        @Override
        public void update() {
            if (level != levelTarget)
                level = MathHelper.uiLerpSnap(this.level, this.levelTarget);
            subRenders.forEach(SubRender::update);
        }

        private static class SubRender {
            public static final float ALPHA_TIME = 2.0f;
            private final float startTime;
            private final float speed;
            private final Vector2 tileTimes;
            private float anim_timer = 0.0f;

            private SubRender(float startTime, float speed, Vector2 tileTimes) {
                this.startTime = startTime;
                this.speed = speed;
                this.tileTimes = tileTimes;
            }

            public void update() {
                if (InBattleDataManager.InOrgasm) {
                    anim_timer += Gdx.graphics.getDeltaTime();
                    return;
                }
                if (anim_timer >= ALPHA_TIME)
                    anim_timer = ALPHA_TIME;
                anim_timer -= Gdx.graphics.getDeltaTime();
            }

            private void drawHeartStream(SpriteBatch sb, float density, Vector2 offset) {
                sb.setShader(heartStream);
                float spawnRemoveTimer = Math.min(ALPHA_TIME, Math.max(anim_timer, 0.0f)) / ALPHA_TIME;
                sb.getShader().setUniformf("u_density", density);
                sb.getShader().setUniformf("u_startTime", startTime);
                sb.getShader().setUniformf("u_speed", speed);
                sb.getShader().setUniformf("u_tileTimes", tileTimes);
                sb.getShader().setUniformf("u_time", anim_timer);
                float height = Gdx.graphics.getHeight();
                float width = Gdx.graphics.getWidth();
                sb.getShader().setUniformf("u_whRate", width / height);
                sb.getShader().setUniformf("u_offset", offset);
                sb.getShader().setUniformf("u_spawnRemoveTimer", spawnRemoveTimer);
                sb.draw(NOISE_TEXTURE, 0, 0, width, height);
                sb.setShader(originShader);
            }
        }
    }
}