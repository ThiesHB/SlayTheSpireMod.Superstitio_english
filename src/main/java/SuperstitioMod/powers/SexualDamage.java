package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.DamageMod.UnBlockAbleDamage;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static com.megacrit.cardcrawl.cards.DamageInfo.DamageType;

public class SexualDamage extends AbstractLupaPower implements HealthBarRenderPower {
    public static final String POWER_ID = DataManager.MakeTextID(SexualDamage.class.getSimpleName());
    private final AbstractCreature giver;

    public SexualDamage(final AbstractCreature owner, int amount, AbstractCreature giver) {
        super(POWER_ID, owner, amount);
        this.giver = giver;
    }

    @Override
    public void atStartOfTurn() {
        check();
    }

    private void check() {
        if (this.owner.currentHealth <= this.amount)
            this.owner.damage(BindingHelper.makeInfo(new DamageModContainer(this, new UnBlockAbleDamage()), giver, amount, DamageType.NORMAL));
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
