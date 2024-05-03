package SuperstitioMod.cards.DamageMod;

import SuperstitioMod.DataManager;
import SuperstitioMod.customStrings.DamageModifierWithSFW;
import SuperstitioMod.customStrings.HasSFWVersion;
import SuperstitioMod.powers.SexualDamage;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.ArrayList;

public class SexDamage extends AbstractDamageModifier {
    public static final String ID = DataManager.MakeTextID(SexDamage.class.getSimpleName());
    public final DamageModifierWithSFW damageStrings = getDamageStringsWithSFW(ID);

    TooltipInfo poisonTooltip = null;

    public SexDamage() {
        this(true);
    }

    //这样子就可以不自动绑定
    public SexDamage(boolean autoBind) {
        this.automaticBindingForCards = autoBind;
    }

    public static DamageModifierWithSFW getDamageStringsWithSFW(String cardName) {
        try {
            return HasSFWVersion.getCustomStringsWithSFW(cardName, DataManager.damage_modifiers, DamageModifierWithSFW.class);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // 在这里使用onAttackToChangeDamage将获取本应造成的伤害，并允许我们修改它，返回0
    // 由于我们在这一函数处能获取伤害量，我们也可以简单地将等于这个量的power应用于目标，或进行其他操作
    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0)
            this.addToTop(new ApplyPowerAction(target, info.owner, new SexualDamage(target, damageAmount, info.owner)));
        return 0;
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        if (poisonTooltip == null) {
            poisonTooltip = new TooltipInfo(damageStrings.getBasicInfo(), damageStrings.getDESCRIPTION());
        }
        ArrayList<TooltipInfo> tooltipInfos = new ArrayList<>();
        tooltipInfos.add(poisonTooltip);
        return tooltipInfos;
    }

    @Override
    public String getCardDescriptor() {
        return damageStrings.getNAME();
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new SexDamage();
    }

    public boolean isInherent() {
        return true;
    }
}