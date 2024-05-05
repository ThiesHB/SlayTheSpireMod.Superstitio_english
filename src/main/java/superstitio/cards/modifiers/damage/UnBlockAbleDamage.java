package superstitio.cards.modifiers.damage;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.cards.modifiers.AbstractLupaDamage;

public class UnBlockAbleDamage extends AbstractLupaDamage {
    public static final String ID = DataManager.MakeTextID(UnBlockAbleDamage.class.getSimpleName());

    public UnBlockAbleDamage() {
        super(ID);
    }


    @Override
    public boolean ignoresBlock(AbstractCreature target) {
        return true;
    }

    @Override
    public boolean ignoresTempHP(AbstractCreature target) {
        return true;
    }

    @Override
    public boolean ignoresThorns() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new UnBlockAbleDamage();
    }
}