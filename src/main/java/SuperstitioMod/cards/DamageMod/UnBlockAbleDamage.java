package SuperstitioMod.cards.DamageMod;

import SuperstitioMod.DataManager;
import SuperstitioMod.customStrings.DamageModifierWithSFW;
import SuperstitioMod.customStrings.HasSFWVersion;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.ArrayList;

public class UnBlockAbleDamage extends AbstractDamageModifier {
    public static final String ID = DataManager.MakeTextID(UnBlockAbleDamage.class.getSimpleName());
    public final DamageModifierWithSFW damageStrings = getDamageStringsWithSFW(ID);

    TooltipInfo tooltip = null;

    public UnBlockAbleDamage() {
        this(true);
    }

    //这样子就可以不自动绑定
    public UnBlockAbleDamage(boolean autoBind) {
        this.automaticBindingForCards = autoBind;
    }

    public static DamageModifierWithSFW getDamageStringsWithSFW(String cardName) {
        try {
            return HasSFWVersion.getCustomStringsWithSFW(cardName, DataManager.damage_modifiers, DamageModifierWithSFW.class);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
    public ArrayList<TooltipInfo> getCustomTooltips() {
        if (tooltip == null) {
            tooltip = new TooltipInfo(damageStrings.getBasicInfo(), damageStrings.getDESCRIPTION());
        }
        ArrayList<TooltipInfo> tooltipInfos = new ArrayList<>();
        tooltipInfos.add(tooltip);
        return tooltipInfos;
    }

    @Override
    public String getCardDescriptor() {
        return damageStrings.getNAME();
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new UnBlockAbleDamage();
    }

    public boolean isInherent() {
        return true;
    }
}