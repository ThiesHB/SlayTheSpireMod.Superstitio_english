package superstitio.delayHpLose;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.AbstractLupaDamage;

public class UnBlockAbleIgnoresTempHPDamage extends AbstractLupaDamage {
    public static final String ID = DataManager.MakeTextID(UnBlockAbleIgnoresTempHPDamage.class);

    public UnBlockAbleIgnoresTempHPDamage() {
        super(ID);
    }

    public static DamageInfo damageInfo(Object instigator, int amount) {
        return BindingHelper.makeInfo(new DamageModContainer(instigator, new UnBlockAbleIgnoresTempHPDamage()), null, amount, DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType);
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
        return new UnBlockAbleIgnoresTempHPDamage();
    }
}