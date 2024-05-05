package superstitio.cards.abstracts;

import superstitio.DataManager;
import superstitio.customStrings.ModifierStringsWithSFW;
import superstitio.customStrings.HasSFWVersion;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;

import java.util.ArrayList;

public abstract class AbstractLupaDamage extends AbstractDamageModifier {
    public final ModifierStringsWithSFW damageStrings;

    TooltipInfo tooltip = null;

    public AbstractLupaDamage(String id) {
        this.damageStrings = getDamageStringsWithSFW(id);
        this.automaticBindingForCards = true;
    }

    public static ModifierStringsWithSFW getDamageStringsWithSFW(String cardName) {
        return HasSFWVersion.getCustomStringsWithSFW(cardName, DataManager.modifiers, ModifierStringsWithSFW.class);
    }

    public AbstractLupaDamage removeAutoBind() {
        this.automaticBindingForCards = false;
        return this;
    }

    @Override
    public final ArrayList<TooltipInfo> getCustomTooltips() {
        if (tooltip == null) {
            tooltip = new TooltipInfo(damageStrings.getBasicInfo(), damageStrings.getDESCRIPTION());
        }
        ArrayList<TooltipInfo> tooltipInfos = new ArrayList<>();
        tooltipInfos.add(tooltip);
        return tooltipInfos;
    }

    @Override
    public final String getCardDescriptor() {
        return damageStrings.getNAME();
    }

    @Override
    public boolean isInherent() {
        return true;
    }

}