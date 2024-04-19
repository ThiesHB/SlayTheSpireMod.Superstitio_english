package SuperstitioMod.powers;

import SuperstitioMod.InBattleDataManager;
import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.actions.AbstractAutoDoneAction;
import SuperstitioMod.powers.interFace.HasTempDecreaseCostEffect;
import SuperstitioMod.powers.interFace.InvisiblePower_StillRenderAmount;
import SuperstitioMod.powers.interFace.OnOrgasm;
import SuperstitioMod.powers.interFace.OnPostApplyThisPower;
import SuperstitioMod.utils.CardUtility;
import SuperstitioMod.utils.PowerUtility;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SexualHeat extends AbstractLupaPower implements InvisiblePower_StillRenderAmount, HasTempDecreaseCostEffect, OnPostApplyThisPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(SexualHeat.class.getSimpleName() +
            "Power");
    public static final int HeatReducePerCard_Origin = 6;
    public static final int HEAT_REQUIREDOrigin = 10;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int DRAW_CARD_INContinueOrgasm = 1;
    //绘制相关
    private static final float BAR_HEIGHT = 20.0f * Settings.scale;
    private static final float BG_OFFSET_X = 31.0f * Settings.scale;
    private static final float BAR_OFFSET_Y = -28.0f * Settings.scale;
    private static final float TEXT_OFFSET_Y = 11.0f * Settings.scale;
    private static final Color PINK = new Color(1f, 0.7529f, 0.7961f, 1.0f);
    private static final Color BarTextColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public Color barBgColor;
    public Color barShadowColor;
    public Color barTextColor;
    public Color barOrginColor;
    public Color barOrgasmShadowColor;
    public int orgasmTime = 0;
    public Hitbox hitbox;
    private int HeatReduce_PerCard = HeatReducePerCard_Origin;
    private int heatRequired = HEAT_REQUIREDOrigin;

    public SexualHeat(final AbstractCreature owner, final int amount) {
        super(POWER_ID, powerStrings.NAME, owner, amount, owner.isPlayer ? PowerType.BUFF : PowerType.DEBUFF, false);

        this.hitbox = new Hitbox(this.owner.hb.width + BAR_HEIGHT * 3f, BAR_HEIGHT * 1.5f);
        this.hitbox.move(this.owner.hb.cX, this.owner.hb.cY + this.owner.hb.height + BAR_OFFSET_Y);

        this.barBgColor = new Color(0.0f, 0.0f, 0.0f, 0.3f);
        this.barShadowColor = this.barBgColor;
        this.barTextColor = BarTextColor;
        this.barOrginColor = PINK;
        this.barOrgasmShadowColor = Color.YELLOW;
    }

    public static boolean isInOrgasm(AbstractCreature creature) {
        Optional<SexualHeat> power = ActivateSexualHeat(creature);
        return power.isPresent() && power.get().isInOrgasm();
    }

    public static Optional<SexualHeat> ActivateSexualHeat(AbstractCreature creature) {
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

    private float barWidth() {
        return this.owner.hb.width * (this.amount % getHeatRequired()) / getHeatRequired();
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        this.hitbox.update();
        if (this.hitbox.hovered) {
            TipHelper.renderGenericTip(this.hitbox.cX + 96.0F * Settings.scale,
                    this.hitbox.cY + 64.0F * Settings.scale, this.name, this.description);
        }

        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        float OwnerX = this.owner.hb.cX - this.owner.hb.width / 2.0F;
        float OwnerY = this.owner.hb.cY + this.owner.hb.height;
        this.renderAmountBarBackGround(sb, OwnerX, OwnerY);
        this.renderAmountBar(sb, OwnerX, OwnerY);
        this.renderOrgasmText(sb, OwnerY);
    }

    @Override
    public void onRemove() {
        EndOrgasm();
    }

    private void renderAmountBarBackGround(final SpriteBatch sb, final float x, final float y) {
        if (this.isInOrgasm()) {
            sb.setColor(this.barOrgasmShadowColor);
        } else {
            sb.setColor(this.barShadowColor);
        }
        sb.draw(ImageMaster.HB_SHADOW_L, x - BAR_HEIGHT, y - BG_OFFSET_X + 3.0f * Settings.scale, BAR_HEIGHT,
                BAR_HEIGHT);
        sb.draw(ImageMaster.HB_SHADOW_B, x, y - BG_OFFSET_X + 3.0f * Settings.scale, this.owner.hb.width,
                BAR_HEIGHT);
        sb.draw(ImageMaster.HB_SHADOW_R, x + this.owner.hb.width, y - BG_OFFSET_X + 3.0f * Settings.scale,
                BAR_HEIGHT, BAR_HEIGHT);
        sb.setColor(this.barBgColor);
        sb.draw(ImageMaster.HEALTH_BAR_L, x - BAR_HEIGHT, y + BAR_OFFSET_Y, BAR_HEIGHT, BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_B, x, y + BAR_OFFSET_Y, this.owner.hb.width, BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_R, x + this.owner.hb.width, y + BAR_OFFSET_Y, BAR_HEIGHT, BAR_HEIGHT);

    }

    private void renderOrgasmText(final SpriteBatch sb, final float y) {
        final float tmp = this.barTextColor.a;
        FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont,
                this.amount + "/" + getHeatRequired() + "(" + this.orgasmTime + ")",
                this.owner.hb.cX, y + BAR_OFFSET_Y + TEXT_OFFSET_Y, this.barTextColor);
        this.barTextColor.a = tmp;
    }

    private void renderAmountBar(final SpriteBatch sb, final float x, final float y) {
        sb.setColor(this.barOrginColor);
        sb.draw(ImageMaster.HEALTH_BAR_L, x - BAR_HEIGHT, y + BAR_OFFSET_Y, BAR_HEIGHT,
                BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_B, x, y + BAR_OFFSET_Y, this.barWidth(), BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_R, x + this.barWidth(), y + BAR_OFFSET_Y, BAR_HEIGHT,
                BAR_HEIGHT);
    }

    @Override
    public void updateDescription() {
        if (this.owner.isPlayer)
            this.description = String.format(SexualHeat.powerStrings.DESCRIPTIONS[0], getHeatRequired());
        else
            this.description = String.format(SexualHeat.powerStrings.DESCRIPTIONS[1], getHeatRequired());
        if (this.isInOrgasm()) {
            this.description = this.description + String.format(SexualHeat.powerStrings.DESCRIPTIONS[2],
                    this.orgasmTime);
        }
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
        AllOnOrgasm().forEach(power -> power.onCheckOrgasm(this));

        int shouldOrgasm = this.amount / getHeatRequired();
        if (orgasmTime >= shouldOrgasm) return;
        this.StartOrgasm();
    }

    private void StartOrgasm() {
        if (AllOnOrgasm().anyMatch(power -> power.preventOrgasm(this)))
            return;

        Orgasm();
        AllOnOrgasm().forEach(power -> power.afterOrgasm(this));

        CheckOrgasm();
    }

    private Stream<OnOrgasm> AllOnOrgasm() {
        ArrayList<OnOrgasm> onOrgasms =
                this.owner.powers.stream().filter(OnOrgasm.class::isInstance).map(power -> (OnOrgasm) power).collect(Collectors.toCollection(ArrayList::new));
        if (this.owner.isPlayer){
            onOrgasms.addAll(CardUtility.AllCardInBattle().stream().filter(OnOrgasm.class::isInstance).map(card -> (OnOrgasm) card).collect(Collectors.toList()));
            onOrgasms.addAll(AbstractDungeon.player.relics.stream().filter(OnOrgasm.class::isInstance).map(relic -> (OnOrgasm) relic).collect(Collectors.toList()));
        }
        return onOrgasms.stream();
    }

    private void Orgasm() {
        orgasmTime++;
        boolean IsContinueOrgasm = orgasmTime > 1;
        if (IsContinueOrgasm)
            this.addToBot(new DrawCardAction(DRAW_CARD_INContinueOrgasm));
        AbstractPower power = this;
        this.addToBot(new AbstractAutoDoneAction() {
            @Override
            public void autoDoneUpdate() {
                PowerUtility.BubbleMessageHigher(power, false, powerStrings.DESCRIPTIONS[IsContinueOrgasm ? 4 : 3]);
            }
        });
        if (!this.owner.isPlayer) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StunMonsterPower((AbstractMonster) this.owner)));
            return;
        }
        SexualHeat p = this;
        this.addToBot(new AbstractAutoDoneAction() {
            @Override
            public void autoDoneUpdate() {
                TempDecreaseCost.RemoveAllByHolder(p);
            }
        });
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new TempDecreaseCost(this.owner, orgasmTime, this), orgasmTime));
        InBattleDataManager.InOrgasm = true;
    }

    private void CheckEndOrgasm() {
        if (amount <= 0)
            EndOrgasm();
    }

    private void EndOrgasm() {
        if (isInOrgasm())
            PowerUtility.BubbleMessageHigher(this, true, powerStrings.DESCRIPTIONS[5]);
        AllOnOrgasm().forEach(power -> power.afterOrgasm(this));
        this.orgasmTime = 0;
        if (this.owner.isPlayer) {
            SexualHeat p = this;
            this.addToBot(new AbstractAutoDoneAction() {
                @Override
                public void autoDoneUpdate() {
                    TempDecreaseCost.RemoveAllByHolder(p);
                }
            });
            InBattleDataManager.InOrgasm = false;
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (!this.isInOrgasm() || !this.owner.isPlayer) return;
        if (!this.getActiveEffectHold().isPresent()) return;
        int reduceAmount = (this.getActiveEffectHold().get().getOriginCost(card) - card.costForTurn) * getHeatReduce_PerCard();
        if (reduceAmount <= 0)
            return;

        Squirt(reduceAmount);
    }

    private void Squirt(int reduceAmount) {
        AllOnOrgasm().forEach(power -> ((OnOrgasm) power).beforeSquirt(this));
        PowerUtility.BubbleMessageHigher(this, true, powerStrings.DESCRIPTIONS[6]);
        this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, reduceAmount));
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
        if (heatRequired > 0)
            this.heatRequired = heatRequired;
        else
            this.heatRequired = 1;
    }

    public int getHeatReduce_PerCard() {
        return HeatReduce_PerCard;
    }

    public void setHeatReducePerCard(int heatReduce_PerCard) {
        HeatReduce_PerCard = heatReduce_PerCard;
    }

    @Override
    public Optional<TempDecreaseCost> getActiveEffectHold() {
        return TempDecreaseCost.AllCostModifierPowerByHolder(this).filter(TempDecreaseCost::isActive).findAny();
    }
}