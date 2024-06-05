package superstitio.delayHpLose;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import superstitio.DataManager;
import superstitio.Logger;
import superstitio.SuperstitioImg;
import superstitio.powers.AbstractSuperstitioPower;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.cards.DamageActionMaker;
import superstitioapi.powers.interfaces.DecreaseHealthBarNumberPower;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleApplyPowerEffect;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleRemovePowerEffect;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleTips;
import superstitioapi.utils.PowerUtility;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.stream.Stream;

@SuperstitioImg.NoNeedImg
public abstract class DelayHpLosePower extends AbstractSuperstitioPower implements
        HealthBarRenderPower, DecreaseHealthBarNumberPower,
        InvisiblePower_InvisibleIconAndAmount, InvisiblePower_InvisibleTips,
        InvisiblePower_InvisibleApplyPowerEffect, InvisiblePower_InvisibleRemovePowerEffect {
    private static final Color OriginColor = new Color(1.0F, 0.85f, 0.90f, 1.0f);

    protected boolean isRemovedForApplyDamage = false;

    public DelayHpLosePower(String id, final AbstractCreature owner, int amount) {
        super(id, owner, amount, PowerType.BUFF, false);
        this.updateDescription();
    }

    public static <T extends DelayHpLosePower> Stream<T> findAll(AbstractCreature target, Class<T> tClass) {
        return target.powers.stream()
                .filter(tClass::isInstance)
                .map(power -> (T) power);
    }
    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
    }

    /**
     * 只会对每个子类的移除方法调用一次
     *
     * @param powerClass  优先移除的类
     * @param amount      移除数量
     * @param removeOther 是否移除其他的
     */
    public static <T extends DelayHpLosePower> void addToBot_removePower(Class<T> powerClass, final int amount, AbstractCreature owner, boolean removeOther) {
        if (amount <= 0) return;
        int lastAmount = 0;
        if (Modifier.isAbstract(powerClass.getModifiers())) {
            Logger.warning("class " + powerClass.getSimpleName() + " is abstract, pls check usages of DelayHpLosePower.addToBot_removePower()");
        }
        ArrayList<Class<? extends DelayHpLosePower>> hasRemovedClass = new ArrayList<>();
        for (AbstractPower power : owner.powers) {
            if (!(powerClass.isInstance(power))) continue;
            DelayHpLosePower delayHpLosePower = ((DelayHpLosePower) power);
            lastAmount = delayHpLosePower.addToBot_removeDelayHpLoss(amount, removeOther);
            hasRemovedClass.add(delayHpLosePower.getClass());
            break;
        }
        if (lastAmount <= 0) return;
        if (!removeOther) return;

        for (AbstractPower power : owner.powers) {
            if (!(power instanceof DelayHpLosePower)) continue;
            if (hasRemovedClass.stream().anyMatch(tClass -> tClass.isInstance(power))) continue;
            DelayHpLosePower delayHpLosePower = ((DelayHpLosePower) power);
            lastAmount = delayHpLosePower.addToBot_removeDelayHpLoss(lastAmount, removeOther);
            hasRemovedClass.add(delayHpLosePower.getClass());
            if (lastAmount <= 0) break;
        }
    }

    public static void addToBot_removePower(final int amount, AbstractCreature owner, boolean removeOther) {
        if (amount <= 0) return;
        int lastAmount = amount;
        ArrayList<Class<? extends DelayHpLosePower>> hasRemovedClass = new ArrayList<>();
        for (AbstractPower power : owner.powers) {
            if (!(power instanceof DelayHpLosePower)) continue;
            if (hasRemovedClass.stream().anyMatch(tClass -> tClass.isInstance(power))) continue;
            DelayHpLosePower delayHpLosePower = ((DelayHpLosePower) power);
            lastAmount = delayHpLosePower.addToBot_removeDelayHpLoss(lastAmount, removeOther);
            hasRemovedClass.add(delayHpLosePower.getClass());
            if (!removeOther) break;
            if (lastAmount <= 0) break;
        }
    }

    /**
     * @param amount      需要移除的值
     * @param removeOther 是否移除其他的
     * @return 剩余的amount
     */
    protected abstract int addToBot_removeDelayHpLoss(int amount, boolean removeOther);

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
        AbstractPower self = this;
        AutoDoneInstantAction.addToBotAbstract(() -> {
            immediate_applyDamage(self);
        });
//        getDelayHpLoseDamageActionMaker().addToBot();
//        AutoDoneInstantAction.addToBotAbstract(() -> this.amount = 0);
        addToBot_removeSpecificPower(this);
    }

//    protected void addToTop_applyDamage() {
//        this.isRemovedForApplyDamage = true;
//        AbstractPower self = this;
//        AutoDoneInstantAction.addToTopAbstract(() -> {
//            immediate_applyDamage(self);
//        });
//        addToBot_removeSpecificPower(this);
//    }

    protected void immediate_applyDamage(AbstractPower self) {
        if (self.amount <= 0) return;
        PowerUtility.BubbleMessage(self, true, pureName());
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05f);
        AbstractDungeon.player.damage(UnBlockAbleDamage.damageInfo(this, self.amount));
        self.amount = 0;
    }

    private DamageActionMaker getDelayHpLoseDamageActionMaker() {
        return DamageActionMaker.maker(this.amount, this.owner)
                .setDamageModifier(this, new UnBlockAbleDamage())
                .setEffect(AbstractGameAction.AttackEffect.LIGHTNING)
                .setDamageType(DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType);
    }

//    public void triggerNothingDamage(AbstractPlayer player, DamageInfo info) {
//        int damageAmount = info.output;
//        boolean hadBlock = true;
//        if (player.currentBlock == 0) {
//            hadBlock = false;
//        }
//
//        if (damageAmount < 0) {
//            damageAmount = 0;
//        }
//
//        if (damageAmount > 1 && player.hasPower("IntangiblePlayer")) {
//            damageAmount = 1;
//        }
//
//
//        if (damageAmount > 0) {
//
//            if (info.owner != player) {
//                player.useStaggerAnimation();
//            }
//
//            player.currentHealth -= damageAmount;
//
//            AbstractDungeon.effectList.add(new StrikeEffect(player, player.hb.cX, player.hb.cY, damageAmount));
//            if (player.currentHealth < 0) {
//                player.currentHealth = 0;
//            }
//            else if (player.currentHealth < player.maxHealth / 4) {
//                AbstractDungeon.topLevelEffects.add(new BorderFlashEffect(new Color(1.0F, 0.1F, 0.05F, 0.0F)));
//            }
//
//            player.healthBarUpdatedEvent();
//            if ((float) player.currentHealth <= (float) player.maxHealth / 2.0F && !player.isBloodied) {
//                player.isBloodied = true;
//                player.relics.forEach(AbstractRelic::onBloodied);
//            }
//
//            if (player.currentHealth < 1) {
//                if (!player.hasRelic("Mark of the Bloom")) {
//                    if (player.hasPotion("FairyPotion")) {
//                        player.potions.stream()
//                        var4 = player.potions.iterator();
//
//                        while (var4.hasNext()) {
//                            AbstractPotion p = (AbstractPotion) var4.next();
//                            if (p.ID.equals("FairyPotion")) {
//                                p.flash();
//                                player.currentHealth = 0;
//                                p.use(player);
//                                AbstractDungeon.topPanel.destroyPotion(p.slot);
//                                return;
//                            }
//                        }
//                    }
//                    else if (player.hasRelic("Lizard Tail") && ((LizardTail) player.getRelic("Lizard Tail")).counter == -1) {
//                        player.currentHealth = 0;
//                        player.getRelic("Lizard Tail").onTrigger();
//                        return;
//                    }
//                }
//
//                player.isDead = true;
//                AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
//                player.currentHealth = 0;
//                if (player.currentBlock > 0) {
//                    player.loseBlock();
//                    AbstractDungeon.effectList.add(new HbBlockBrokenEffect(player.hb.cX - player.hb.width / 2.0F + BLOCK_ICON_X, player.hb.cY - player.hb.height / 2.0F + BLOCK_ICON_Y));
//                }
//            }
//        }
//        else if (player.currentBlock > 0) {
//            AbstractDungeon.effectList.add(new BlockedWordEffect(player, player.hb.cX, player.hb.cY, uiStrings.TEXT[0]));
//        }
//        else if (hadBlock) {
//            AbstractDungeon.effectList.add(new BlockedWordEffect(player, player.hb.cX, player.hb.cY, uiStrings.TEXT[0]));
//            AbstractDungeon.effectList.add(new HbBlockBrokenEffect(player.hb.cX - player.hb.width / 2.0F + BLOCK_ICON_X, player.hb.cY - player.hb.height / 2.0F + BLOCK_ICON_Y));
//        }
//        else {
//            AbstractDungeon.effectList.add(new StrikeEffect(player, player.hb.cX, player.hb.cY, 0));
//        }
//    }

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
