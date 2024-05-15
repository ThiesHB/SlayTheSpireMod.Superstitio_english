package superstitio.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import superstitio.DataManager;

import java.util.Objects;

public abstract class EasyBuildAbstractPowerForPowerCard extends AbstractSuperstitioPower {

    public EasyBuildAbstractPowerForPowerCard(int amount) {
        super();
        SetupPower(getPowerCard().cardID,
                getPowerStringsWithSFW(getPowerCard().cardID),
                AbstractDungeon.player,
                amount,
                PowerType.BUFF,
                false);
        this.name = getPowerCard().name;
        updateDescription();
    }

    protected abstract AbstractCard getPowerCard();

    @Override
    public String getDescriptionStrings() {
        if (powerStrings.getDESCRIPTIONS() != null && powerStrings.getDESCRIPTIONS().length != 0 && !Objects.equals(powerStrings.getDESCRIPTIONS()[0], LocalizedStrings.createMockStringArray(1)[0]))
            return powerStrings.getDESCRIPTIONS()[0];
        String desc;
        desc = getPowerCard().rawDescription.replace(DataManager.getModID().toLowerCase() + ":", "#y");
        desc = desc.replace("!M", "#b%d");
        desc = desc.replace("!D", "#b%d");
        desc = desc.replace("!B", "#b%d");
        desc = desc.replace("%d!", "%d");

        return desc;
    }
}
