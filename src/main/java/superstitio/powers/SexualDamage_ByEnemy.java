package superstitio.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cards.DamageActionMaker;
import superstitio.cards.modifiers.damage.RemoveSexualDamage_ByEnemyDamage;
import superstitio.characters.Lupa;
import superstitio.powers.interfaces.DecreaseHealthBarNumberPower;

public class SexualDamage_ByEnemy extends AbstractLupaPower
        implements HealthBarRenderPower, DecreaseHealthBarNumberPower, NonStackablePower, InvisiblePower {
    public static final String POWER_ID = DataManager.MakeTextID(SexualDamage_ByEnemy.class.getSimpleName());
    protected final AbstractCreature giver;
    private int Turn;
    private Color barColor = Color.PINK.cpy();

    public SexualDamage_ByEnemy(final AbstractCreature owner, int amount, AbstractCreature giver) {
        super(POWER_ID, owner, amount);
        this.giver = giver;
        ReflectionHacks.setPrivate(this, SexualDamage_ByEnemy.class, "greenColor", Color.PINK.cpy());
        Turn = 1;
    }

    @Override
    public void atStartOfTurnPostDraw() {
    }

    @Override
    public void onVictory() {
        addToBot_removeSpecificPower(this);
    }

//    @Override
//    public Color getColor() {
//        if (Turn >= 0)
//            return new Color(191.0f / 255.0f, 15.0f / 255.0f, 110.0f / 255.0f, 0.0f);
//        return super.getColor();
//    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {

    }

    @Override
    public void atStartOfTurn() {
        if (Turn <= 0) {
            AbstractPower self = this;

            DamageActionMaker.maker(this.giver, this.amount, this.owner)
                    .setDamageModifier(new RemoveSexualDamage_ByEnemyDamage(() -> {
                        self.amount = 0;
                    }))
                    .setEffect(AbstractGameAction.AttackEffect.POISON)
                    .setDamageType(Lupa.CanOnlyDamageDamageType.UnBlockAbleDamageType).addToBot();
            addToBot_removeSpecificPower(this);
        }

    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        Turn--;
        this.barColor = new Color(1.0F, 0.5F, 0.0F, 1.0F);
    }

    @Override
    public int getHealthBarAmount() {
        return this.amount;
    }

    @Override
    public Color getColor() {
        return barColor;
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
