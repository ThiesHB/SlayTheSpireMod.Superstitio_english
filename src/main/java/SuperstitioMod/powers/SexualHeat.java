package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.utils.PowerUtility;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SexualHeat extends AbstractPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(SexualHeat.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int HEAT_REQUIRED = 10;
    public static final int HEAT_REDUCE_RATE = 4;
    private static final int DRAW_CARD = 1;

    //绘制相关
    private static final float BAR_HEIGHT = 20.0f * Settings.scale;
    private static final float BG_OFFSET_X = 31.0f * Settings.scale;
    private static final float BAR_OFFSET_Y = -28.0f * Settings.scale;
    private static final float TEXT_OFFSET_Y = 11.0f * Settings.scale;

    private static final Color PINK = new Color(1f, 0.7529f, 0.7961f, 1.0f);

    private static final Color BarTextColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    private static final int maxBarAmount = HEAT_REQUIRED;
    private final Map<AbstractCard, Integer> costMap = new HashMap<>();
    public Color barBgColor;
    public Color barShadowColor;
    public Color barTextColor;
    public Color barOrginColor;
    public Color barOrgasmShadowColor;
    private boolean InOrgasm = false;
    private int LastOrgasmTime = 0;


    public SexualHeat(final AbstractCreature owner, final int amount) {
        this.name = SexualHeat.powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        this.amount = amount;
        if (AbstractDungeon.player.powers.stream().noneMatch(abstractPower -> Objects.equals(abstractPower.ID, SexualHeat.POWER_ID)))
            CheckOrgasm();

        // 添加一大一小两张能力图
        String path128 = SuperstitioModSetup.getImgFilesPath() + "powers/default84.png";
        String path48 = SuperstitioModSetup.getImgFilesPath() + "powers/default32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();

        this.barBgColor = new Color(0.0f, 0.0f, 0.0f, 0.3f);
        this.barShadowColor = this.barBgColor;
        this.barTextColor = BarTextColor;
        this.barOrginColor = PINK;
        this.barOrgasmShadowColor = Color.YELLOW;
    }

    private float barWidth() {
        return this.owner.hb.width * (this.amount % maxBarAmount) / maxBarAmount;
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        float OwnerX = this.owner.hb.cX - this.owner.hb.width / 2.0F;
        float OwnerY = this.owner.hb.cY + this.owner.hb.height;
        this.renderAmountBarBackGround(sb, OwnerX, OwnerY);
        this.renderAmountBar(sb, OwnerX, OwnerY);
        this.renderOrgasmText(sb, OwnerY);
    }

    private void renderAmountBarBackGround(final SpriteBatch sb, final float x, final float y) {
        if (this.InOrgasm) {
            sb.setColor(this.barOrgasmShadowColor);
        } else {
            sb.setColor(this.barShadowColor);
        }
        sb.draw(ImageMaster.HB_SHADOW_L, x - BAR_HEIGHT, y - BG_OFFSET_X + 3.0f * Settings.scale, BAR_HEIGHT, BAR_HEIGHT);
        sb.draw(ImageMaster.HB_SHADOW_B, x, y - BG_OFFSET_X + 3.0f * Settings.scale, this.owner.hb.width, BAR_HEIGHT);
        sb.draw(ImageMaster.HB_SHADOW_R, x + this.owner.hb.width, y - BG_OFFSET_X + 3.0f * Settings.scale, BAR_HEIGHT, BAR_HEIGHT);
        sb.setColor(this.barBgColor);
        if (this.amount == HEAT_REQUIRED) {
            return;
        }
        sb.draw(ImageMaster.HEALTH_BAR_L, x - BAR_HEIGHT, y + BAR_OFFSET_Y, BAR_HEIGHT, BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_B, x, y + BAR_OFFSET_Y, this.owner.hb.width, BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_R, x + this.owner.hb.width, y + BAR_OFFSET_Y, BAR_HEIGHT, BAR_HEIGHT);

    }

    private void renderOrgasmText(final SpriteBatch sb, final float y) {
        final float tmp = this.barTextColor.a;
        FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, this.amount + "/" + HEAT_REQUIRED + "(" + this.getOrgasmTimes() + ")",
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
        this.description = String.format(SexualHeat.powerStrings.DESCRIPTIONS[0], HEAT_REQUIRED, DRAW_CARD, HEAT_REDUCE_RATE);
        if (this.InOrgasm) {
            this.description = this.description + String.format(SexualHeat.powerStrings.DESCRIPTIONS[1], this.getOrgasmTimes());
        }
    }

    @Override
    public void stackPower(final int stackAmount) {
        super.stackPower(stackAmount);
        CheckOrgasm();
    }

    private void CheckOrgasm() {
        int shouldOrgasm = this.amount / HEAT_REQUIRED;
        if (LastOrgasmTime < shouldOrgasm) {
            int d = shouldOrgasm - LastOrgasmTime;
            LastOrgasmTime = shouldOrgasm;
            for (int i = 0; i < d; i++) {
                this.StartOrgasm();
            }
        }
    }

    private void StartOrgasm() {
        InOrgasm = true;
        HandCardsCheaper();
        this.addToBot(new DrawCardAction(DRAW_CARD));
        AbstractPower power = this;
        boolean IsOrgasm = InOrgasm;
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                PowerUtility.BubbleMessage(power, false, powerStrings.DESCRIPTIONS[IsOrgasm ? 3 : 2]);
                updateDescription();
            }
        });
    }

    private void EndOrgasm() {
        PowerUtility.BubbleMessage(this, true, powerStrings.DESCRIPTIONS[4]);
        this.HandCardsCostToOrigin();
        this.InOrgasm = false;
        this.LastOrgasmTime = 0;
    }

    private void HandCardsCheaper() {
        for (final AbstractCard card : AbstractDungeon.player.hand.group) {
            this.CardCostCheaper(card);
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (this.InOrgasm)
            this.CardCostCheaper(card);
    }

    /**
     * 只会返回当前最高的连续高潮次数，直到清零。
     */
    public int getOrgasmTimes() {
        if (!this.InOrgasm)
            return 0;
        return LastOrgasmTime;
    }

    private void CardCostCheaper(AbstractCard card) {
        if (card.costForTurn <= 0)
            return;
        costMap.computeIfAbsent(card, c -> c.costForTurn);
        if (getOriginCost(card) >= this.getOrgasmTimes()) {
            final int newCost = getOriginCost(card) - this.getOrgasmTimes();
            if (card.costForTurn != newCost) {
                card.costForTurn = newCost;
                card.isCostModified = true;
            }
        } else {
            card.freeToPlayOnce = true;
            card.isCostModified = true;
        }
    }

    private void HandCardsCostToOrigin() {
        costMap.forEach((card, integer) -> {
            if (card == null)
                return;
            card.costForTurn = getOriginCost(card);
            card.isCostModified = false;
        });
        AbstractDungeon.player.hand.group.forEach(card -> {
            if (card == null)
                return;
            card.costForTurn = getOriginCost(card);
            card.isCostModified = false;
        });
        costMap.clear();
    }

    private int getOriginCost(AbstractCard card) {
        if (costMap.get(card) == null)
            return card.cost;
        return costMap.get(card);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        if (this.InOrgasm) {
            if (getOriginCost(card) < card.costForTurn)
                return;
            int reduceAmount = (getOriginCost(card) - card.costForTurn) * HEAT_REDUCE_RATE;
            int a = this.amount;
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    if (a <= reduceAmount) {
                        SuperstitioModSetup.logger.info("即将还原费用：");
                        costMap.forEach((c, integer) -> SuperstitioModSetup.logger.info(c.name + String.format("%d", getOriginCost(c))));
                        EndOrgasm();
                    }
                    this.isDone = true;
                }
            });
            this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, reduceAmount));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        EndOrgasm();
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }
}