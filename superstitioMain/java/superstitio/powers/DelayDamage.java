package superstitio.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;

public class DelayDamage extends AbstractSuperstitioPower implements HealthBarRenderPower {
    public static final String POWER_ID = DataManager.MakeTextID(DelayDamage.class);
    private static final Color BarColor = Color.ORANGE.cpy();
    private final int damageAmount;
    private AbstractCreature giver = AbstractDungeon.player;
    private AbstractDamageModifier damageModifier;

    public DelayDamage(final AbstractCreature owner, int amount, int damageAmount) {
        super(POWER_ID, owner, amount);
        this.damageAmount = damageAmount;
    }

    public DelayDamage setupGiver(AbstractCreature giver) {
        this.giver = giver;
        return this;
    }

    public DelayDamage setupDamageModifier(AbstractDamageModifier damageModifier) {
        this.damageModifier = damageModifier;
        return this;
    }

    @Override
    public void atEndOfRound() {
        addToBot_AutoRemoveOne(this);
    }

    @Override
    public void onRemove() {
        if (damageModifier != null)
            addToBot(new DamageAction(this.owner, BindingHelper.makeInfo(new DamageModContainer(this, damageModifier), giver, this.damageAmount,
                    DamageInfo.DamageType.NORMAL)));
        addToBot(new DamageAction(this.owner, new DamageInfo(giver, this.damageAmount, DamageInfo.DamageType.NORMAL)));
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount);
    }

    @Override
    public int getHealthBarAmount() {
        return this.damageAmount;
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        renderAmount2(sb, x, y, c, this.damageAmount);
//        AbstractDungeon.actionManager.currentAction.target
    }

    @Override
    public Color getColor() {
        return BarColor;
    }
}
