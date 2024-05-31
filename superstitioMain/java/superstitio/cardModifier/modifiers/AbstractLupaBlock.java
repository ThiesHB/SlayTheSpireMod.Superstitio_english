package superstitio.cardModifier.modifiers;

import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import superstitio.DataManager;
import superstitio.customStrings.HasSFWVersion;
import superstitio.customStrings.ModifierStringsSet;

import java.util.ArrayList;

public abstract class AbstractLupaBlock extends AbstractBlockModifier {
    public final ModifierStringsSet blockStrings;
    TooltipInfo tooltip = null;

    public AbstractLupaBlock(String id) {
        this.blockStrings = getBlockStringsWithSFW(id);
        this.automaticBindingForCards = true;
    }

    public static ModifierStringsSet getBlockStringsWithSFW(String cardName) {
        return HasSFWVersion.getCustomStringsWithSFW(cardName, DataManager.modifiers, ModifierStringsSet.class);
    }

    public AbstractLupaBlock removeAutoBind() {
        this.automaticBindingForCards = false;
        return this;
    }

    @Override
    public final String getName() {
        return blockStrings.getNAME();
    }

    @Override
    public final ArrayList<TooltipInfo> getCustomTooltips() {
        if (tooltip == null) {
            tooltip = new TooltipInfo(blockStrings.getBasicInfo(), blockStrings.getDESCRIPTION());
        }
        ArrayList<TooltipInfo> tooltipInfos = new ArrayList<>();
        tooltipInfos.add(tooltip);
        return tooltipInfos;
    }

    @Override
    public final String getDescription() {
        return blockStrings.getDESCRIPTION();
    }

    @Override
    public final String getCardDescriptor() {
        return blockStrings.getNAME();
    }

    @Override
    public boolean isInherent() {
        return true;
    }
}
