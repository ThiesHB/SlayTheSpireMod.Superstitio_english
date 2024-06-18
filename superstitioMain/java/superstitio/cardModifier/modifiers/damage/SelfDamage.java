package superstitio.cardModifier.modifiers.damage;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.AbstractLupaDamage;

public class SelfDamage extends AbstractLupaDamage {
    public static final String ID = DataManager.MakeTextID(SelfDamage.class);

    public SelfDamage() {
        super(ID);
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target instanceof AbstractPlayer)
            return super.onAttackToChangeDamage(info, damageAmount, target) / 2;
        return super.onAttackToChangeDamage(info, damageAmount, target);
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new SelfDamage();
    }
}