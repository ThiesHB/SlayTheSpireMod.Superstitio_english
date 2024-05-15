package superstitio.delayHpLose;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.AbstractLupaDamage;

public class UnBlockAbleDamage extends AbstractLupaDamage {
    public static final String ID = DataManager.MakeTextID(UnBlockAbleDamage.class);

    public UnBlockAbleDamage() {
        super(ID);
    }

    @Override
    public boolean ignoresBlock(AbstractCreature target) {
        return true;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        return super.atDamageGive(damage, DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType, target, card);
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