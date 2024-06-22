package superstitio.cardModifier.modifiers.damage;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.AbstractLupaDamage;

public class UnBlockAbleHpLoseLikeDamage extends AbstractLupaDamage {
    public static final String ID = DataManager.MakeTextID(UnBlockAbleHpLoseLikeDamage.class);

    public UnBlockAbleHpLoseLikeDamage() {
        super(ID);
    }

    public static DamageInfo damageInfo(Object instigator, int amount) {
        return BindingHelper.makeInfo(new DamageModContainer(instigator, new UnBlockAbleHpLoseLikeDamage()), null, amount,
                DataManager.CanOnlyDamageDamageType.NoTriggerLupaAndMasoRelicHpLose);
    }

    @Override
    public boolean ignoresBlock(AbstractCreature target) {
        return true;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        return super.atDamageGive(damage, DataManager.CanOnlyDamageDamageType.NoTriggerLupaAndMasoRelicHpLose, target, card);
    }

    @Override
    public boolean ignoresThorns() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new UnBlockAbleHpLoseLikeDamage();
    }
}