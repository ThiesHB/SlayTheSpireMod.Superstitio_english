package superstitio.delayHpLose;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.DamageActionMaker;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.patchAndInterface.interfaces.DecreaseHealthBarNumberPower;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleApplyPowerEffect;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleRemovePowerEffect;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleTips;
import superstitio.utils.PowerUtility;

public abstract class DelayHpLosePower extends AbstractSuperstitioPower implements
        HealthBarRenderPower, DecreaseHealthBarNumberPower,
        InvisiblePower_InvisibleIconAndAmount, InvisiblePower_InvisibleTips,
        InvisiblePower_InvisibleApplyPowerEffect, InvisiblePower_InvisibleRemovePowerEffect {
    private static final Color OriginColor = new Color(1.0F, 0.85f, 0.90f, 1.0f);

    protected boolean isRemovedForApplyDamage = false;

    public DelayHpLosePower(String id, final AbstractCreature owner, int amount) {
        super(id, owner, amount);
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if (!isRemovedForApplyDamage)
            playRemoveEffect();
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (!isRemovedForApplyDamage)
            playRemoveEffect();
    }

    protected void playRemoveEffect() {
        AbstractDungeon.effectList.add(
                new PowerBuffEffect(this.owner.hb.cX - this.owner.animX, this.owner.hb.cY + this.owner.hb.height / 2.0F,
                        pureName() + CardCrawlGame.languagePack.getUIString("ApplyPowerAction").TEXT[0]));
    }

    protected String pureName() {
        return this.name.replace("#r", "");
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount);
    }

    @Override
    public int getHealthBarAmount() {
        return this.amount;
    }

    protected void addToBot_applyDamage() {
        this.isRemovedForApplyDamage = true;
        AutoDoneInstantAction.addToBotAbstract(() -> {
            PowerUtility.BubbleMessage(this, true, pureName());
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05f);
        });
        getDelayHpLoseDamageActionMaker().addToBot();
        addToBot_removeSpecificPower(this);
    }

    private DamageActionMaker getDelayHpLoseDamageActionMaker() {
        return DamageActionMaker.maker(this.amount, this.owner)
                .setDamageModifier(this, new UnBlockAbleDamage())
                .setEffect(AbstractGameAction.AttackEffect.LIGHTNING)
                .setDamageType(DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType);
    }

    @Override
    public Color getColor() {
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
}
