package superstitio.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cards.DamageActionMaker;
import superstitio.characters.Lupa;

public class SexualDamage_ByEnemy extends SexualDamage implements NonStackablePower, InvisiblePower {
    public static final String POWER_ID = DataManager.MakeTextID(SexualDamage_ByEnemy.class.getSimpleName());
    private int Turn;

    public SexualDamage_ByEnemy(final AbstractCreature owner, int amount, AbstractCreature giver) {
        super(owner, amount, giver);
        this.ID = POWER_ID;
        Turn = 2;
    }

    @Override
    public void atStartOfTurnPostDraw() {
    }

    @Override
    public void onVictory() {
        addToBot_removeSpecificPower(this);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {

    }

//    @Override
//    public Color getColor() {
//        if (Turn >= 0)
//            return new Color(191.0f / 255.0f, 15.0f / 255.0f, 110.0f / 255.0f, 0.0f);
//        return super.getColor();
//    }

    @Override
    public void atStartOfTurn() {
        Turn--;
        if (Turn <= 0) {
            DamageActionMaker.maker(this.giver, this.amount, this.owner).setDamageType(Lupa.CanOnlyDamageDamageType.UnBlockAbleDamageType).addToBot();
            addToBot_removeSpecificPower(this);
        }

    }
}
