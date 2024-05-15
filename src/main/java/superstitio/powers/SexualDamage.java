package superstitio.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.powers.patchAndInterface.interfaces.DecreaseHealthBarNumberPower;

public class SexualDamage extends AbstractLupaPower implements HealthBarRenderPower, DecreaseHealthBarNumberPower {
    public static final String POWER_ID = DataManager.MakeTextID(SexualDamage.class);
    protected final AbstractCreature giver;

    public SexualDamage(final AbstractCreature owner, int amount, AbstractCreature giver) {
        super(POWER_ID, owner, amount);
        this.giver = giver;
//        ReflectionHacks.setPrivate(this, DelayHpLosePower.class, "greenColor", Color.PINK.cpy());
    }

    @Override
    public void atStartOfTurn() {
//        this.owner.damage(BindingHelper.makeInfo(new DamageModContainer(this, new UnBlockAbleDamage()), giver, amount, DamageType.HP_LOSS));
        this.flash();
        addToBot(new LoseHPAction(this.owner, giver, this.amount, AbstractGameAction.AttackEffect.POISON));
        addToBot_removeSpecificPower(this);
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount);
    }

    @Override
    public int getHealthBarAmount() {
        return this.amount;
    }

    @Override
    public Color getColor() {
        return Color.PINK.cpy();
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
        float temp = this.fontScale;
        this.fontScale *= 1.5f;
        super.renderAmount(sb, x, y, c);
        this.fontScale = temp;
    }
}
