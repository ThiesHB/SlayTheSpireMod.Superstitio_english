package superstitio.delayHpLose;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.DamageActionMaker;
import superstitio.powers.AbstractLupaPower;
import superstitio.powers.interfaces.DecreaseHealthBarNumberPower;
import superstitio.powers.interfaces.invisible.InvisiblePower_InvisibleApplyPowerEffect;
import superstitio.powers.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount;
import superstitio.powers.interfaces.invisible.InvisiblePower_InvisibleRemovePowerEffect;
import superstitio.powers.interfaces.invisible.InvisiblePower_InvisibleTips;
import superstitio.utils.PowerUtility;

public class DelayHpLosePower extends AbstractLupaPower implements
        HealthBarRenderPower, DecreaseHealthBarNumberPower,
        InvisiblePower_InvisibleIconAndAmount, InvisiblePower_InvisibleTips,
        InvisiblePower_InvisibleApplyPowerEffect, InvisiblePower_InvisibleRemovePowerEffect {
    private static final String POWER_ID = DataManager.MakeTextID(DelayHpLosePower.class.getSimpleName());
    private static final Color ReadyToRemoveColor = new Color(1.0F, 0.5F, 0.0F, 1.0F);
    private static final Color ForAWhileColor = new Color(0.9412F, 0.4627f, 0.5451f, 1.0f);
    private static final Color OriginColor = new Color(1.0F, 0.85f, 0.90f, 1.0f);
    private static final int TURN_INIT = 1;
    private static final int TURN_READY = 0;

    private int Turn;
    private boolean atEnemyTurn;

    public DelayHpLosePower(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.Turn = TURN_INIT;
        this.ID = getUniqueID();
        ReflectionHacks.setPrivate(this, DelayHpLosePower.class, "greenColor", Color.PINK.cpy());
    }

    public static String getUniqueIDCanHandleThisTurn() {
        return POWER_ID + TURN_READY;
    }

    public String getUniqueID() {
        return POWER_ID + this.Turn;
    }

    @Override
    public boolean checkShouldInvisibleTips() {
        return this.Turn > 0;
    }

    @Override
    public void onVictory() {
        addToBot_removeSpecificPower(this);
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        playRemoveEffect();
    }

    @Override
    public void onRemove() {
        super.onRemove();
        playRemoveEffect();
    }

    private void playRemoveEffect() {
        AbstractDungeon.effectList.add(
                new PowerBuffEffect(this.owner.hb.cX - this.owner.animX, this.owner.hb.cY + this.owner.hb.height / 2.0F,
                        pureName() + CardCrawlGame.languagePack.getUIString("ApplyPowerAction").TEXT[0]));
    }

    private String pureName() {
        return this.name.replace("#r", "");
    }

    @Override
    public void atStartOfTurn() {
        if (Turn <= 0) {
            PowerUtility.BubbleMessage(this, true, pureName());
//            DamageActionMaker.maker(this.owner, this.amount, this.owner)
//                    .setDamageModifier(new UnBlockAbleDamage())
//                    .setEffect(AbstractGameAction.AttackEffect.POISON)
//                    .setDamageType(DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType)
//                    .addToBot();
            AutoDoneInstantAction.addToBotAbstract(() -> {
                AbstractDungeon.effectList.add(
                        new FlashAtkImgEffect(this.owner.hb.cX,this.owner.hb.cY, AbstractGameAction.AttackEffect.POISON));
                owner.currentHealth -= amount;
            });
            addToBot_removeSpecificPower(this);
        }
        Turn--;
        atEnemyTurn = false;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.ID = getUniqueID();
        super.updateDescription();
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        atEnemyTurn = true;
    }

    @Override
    public int getHealthBarAmount() {
        return this.amount;
    }

    @Override
    public Color getColor() {
        if (Turn <= 0)
            return atEnemyTurn ? ReadyToRemoveColor : ForAWhileColor;
        return OriginColor;
    }

    @Override
    public int getDecreaseAmount() {
        return this.amount;
    }

    @Override
    public void setDecreaseAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        renderAmount2(sb, x, y, c, this.Turn);
    }
}
