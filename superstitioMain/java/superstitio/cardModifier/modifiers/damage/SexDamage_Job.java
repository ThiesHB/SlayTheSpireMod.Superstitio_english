package superstitio.cardModifier.modifiers.damage;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.AbstractLupaDamage;
import superstitio.powers.SexualDamage;

public class SexDamage_Job extends AbstractLupaDamage {
    public static final String ID = DataManager.MakeTextID(SexDamage_Job.class);
    private final static int OnlyDealDamage = 1;

    public SexDamage_Job() {
        super(ID);
    }

    // 在这里使用onAttackToChangeDamage将获取本应造成的伤害，并允许我们修改它，返回0
    // 由于我们在这一函数处能获取伤害量，我们也可以简单地将等于这个量的power应用于目标，或进行其他操作
    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount <= OnlyDealDamage || target instanceof AbstractPlayer) return damageAmount;
        this.addToTop(new ApplyPowerAction(target, info.owner, new SexualDamage(target, damageAmount - OnlyDealDamage, info.owner)));
        return OnlyDealDamage;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new SexDamage_Job();
    }
}