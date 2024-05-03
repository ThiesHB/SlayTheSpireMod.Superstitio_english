package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class DelayDamage extends AbstractLupaPower implements HealthBarRenderPower {
    public static final String POWER_ID = DataManager.MakeTextID(DelayDamage.class.getSimpleName());
    private final AbstractCreature giver;
    private final int damageAmount;

    public DelayDamage(final AbstractCreature owner, int amount, int damageAmount, AbstractCreature giver) {
        super(POWER_ID, owner, amount);
        this.giver = giver;
        this.damageAmount = damageAmount;
    }

    @Override
    public void atEndOfRound() {
        addToBot_AutoRemoveOne(this);
    }

    @Override
    public void onRemove() {
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
    }


    @Override
    public Color getColor() {
        return Color.ORANGE.cpy();
    }
}
