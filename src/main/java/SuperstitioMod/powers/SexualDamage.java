package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class SexualDamage extends AbstractLupaPower implements HealthBarRenderPower {
    public static final String POWER_ID = DataManager.MakeTextID(SexualDamage.class.getSimpleName());
    private final AbstractCreature giver;

    public SexualDamage(final AbstractCreature owner, int amount, AbstractCreature giver) {
        super(POWER_ID, owner, amount);
        this.giver = giver;
    }

    @Override
    public void atStartOfTurn() {
//        this.owner.damage(BindingHelper.makeInfo(new DamageModContainer(this, new UnBlockAbleDamage()), giver, amount, DamageType.HP_LOSS));
        addToBot(new LoseHPAction(this.owner,giver,this.amount, AbstractGameAction.AttackEffect.POISON));
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
}
