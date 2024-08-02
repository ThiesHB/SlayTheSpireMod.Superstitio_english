package superstitio.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;

import java.util.Objects;

public abstract class EasyBuildAbstractPowerForPowerCard extends AbstractSuperstitioPower {

    protected final SuperstitioCard powerCard;

    public EasyBuildAbstractPowerForPowerCard(int amount) {
        this(AbstractDungeon.player, amount, true);
    }
    public EasyBuildAbstractPowerForPowerCard(int amount, boolean shouldUpdateDesc) {
        this(AbstractDungeon.player, amount, shouldUpdateDesc);
    }

    public EasyBuildAbstractPowerForPowerCard(AbstractCreature owner, int amount) {
        this(owner, amount, true);
    }

    public EasyBuildAbstractPowerForPowerCard(AbstractCreature owner, int amount, boolean shouldUpdateDesc) {
        super(true);
        this.powerCard = makePowerCard();
        SetupPower(powerCard.cardID,
                getPowerStringsWithSFW(powerCard.cardID),
                owner,
                amount,
                PowerType.BUFF,
                false);
        this.name = powerCard.name;
        if (shouldUpdateDesc)
            updateDescription();
    }

    public EasyBuildAbstractPowerForPowerCard upgradeCardInThis(boolean shouldUpgrade) {
        if (shouldUpgrade)
            this.powerCard.upgrade();
        return this;
    }

    protected abstract SuperstitioCard makePowerCard();

    protected String getDesc() {
        String desc;
        if (powerCard.cardStrings.getDESCRIPTION().contains("%s"))
            desc = powerCard.cardStrings.getDESCRIPTION();
        else
            desc = powerCard.rawDescription;
        return desc;
    }

    @Override
    public String getDescriptionStrings() {
        if (powerStrings.getDESCRIPTIONS() != null && powerStrings.getDESCRIPTIONS().length != 0 && !Objects.equals(powerStrings.getDESCRIPTIONS()[0], LocalizedStrings.createMockStringArray(1)[0]))
            return powerStrings.getDESCRIPTIONS()[0];
        String desc = getDesc();
        desc = desc.replace(DataManager.getModID().toLowerCase() + ":", "#y");
        desc = desc.replace("*", " ");
        desc = desc.replace("!M", "#b%d");
        desc = desc.replace("!D", "#b%d");
        desc = desc.replace("!B", "#b%d");
        desc = desc.replace("%d!", "%d");
//        desc = desc.replace("%s", "");

        return desc;
    }
}
