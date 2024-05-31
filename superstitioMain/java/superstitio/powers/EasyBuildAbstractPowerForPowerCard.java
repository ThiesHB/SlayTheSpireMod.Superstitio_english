package superstitio.powers;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;

import java.util.Objects;

public abstract class EasyBuildAbstractPowerForPowerCard extends AbstractSuperstitioPower {

    protected final SuperstitioCard powerCard;

    public EasyBuildAbstractPowerForPowerCard(int amount) {
        super();
        this.powerCard = makePowerCard();
        SetupPower(powerCard.cardID,
                getPowerStringsWithSFW(powerCard.cardID),
                AbstractDungeon.player,
                amount,
                PowerType.BUFF,
                false);
        this.name = powerCard.name;
        updateDescription();
    }

    //    @NotNull
    protected abstract SuperstitioCard makePowerCard();

    @Override
    public String getDescriptionStrings() {
        if (powerStrings.getDESCRIPTIONS() != null && powerStrings.getDESCRIPTIONS().length != 0 && !Objects.equals(powerStrings.getDESCRIPTIONS()[0], LocalizedStrings.createMockStringArray(1)[0]))
            return powerStrings.getDESCRIPTIONS()[0];
        String desc;
        if (powerCard.cardStrings.getDESCRIPTION().contains("%s"))
            desc = powerCard.cardStrings.getDESCRIPTION();
        else
            desc = powerCard.rawDescription;
        desc = desc.replace(DataManager.getModID().toLowerCase() + ":", "#y");
        desc = desc.replace("*", " ");
        desc = desc.replace("!M", "#b%d");
        desc = desc.replace("!D", "#b%d");
        desc = desc.replace("!B", "#b%d");
        desc = desc.replace("%d!", "%d");
        desc = desc.replace("%s", "");

        return desc;
    }
}
