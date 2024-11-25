package superstitio.powers

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.localization.LocalizedStrings
import superstitio.DataManager
import superstitio.cards.SuperstitioCard
import java.util.*

abstract class EasyBuildAbstractPowerForPowerCard @JvmOverloads constructor(
    amount: Int,
    shouldUpdateDesc: Boolean = true,
    owner: AbstractCreature = AbstractDungeon.player,
) : AbstractSuperstitioPower() {
    protected val powerCard: SuperstitioCard

    init {
        this.powerCard = makePowerCard()
        SetupPower(
            powerCard.cardID,
            getPowerStringsWithSFW(powerCard.cardID),
            owner,
            amount,
            PowerType.BUFF,
            false
        )
        this.name = powerCard.name
        if (shouldUpdateDesc)
            updateDescription()
    }

    fun upgradeCardInThis(shouldUpgrade: Boolean): EasyBuildAbstractPowerForPowerCard {
        if (shouldUpgrade) powerCard.upgrade()
        return this
    }

    protected abstract fun makePowerCard(): SuperstitioCard

    protected open fun getDesc(): String {
        val desc =
            if (powerCard.cardStrings.getDESCRIPTION().contains("%s") || powerCard.cardStrings.getDESCRIPTION()
                    .contains(
                        "%%"
                    )
            ) powerCard.cardStrings.getDESCRIPTION()
            else powerCard.rawDescription
        return desc
    }

    override fun getDescriptionStrings(): String {
        if (powerStrings.getDESCRIPTIONS().isNotEmpty() && powerStrings.getDESCRIPTION(
                0
            ) != LocalizedStrings.createMockStringArray(1)[0]
        ) return powerStrings.getDESCRIPTION(0)
        var desc = getDesc()
        desc = desc.replace(DataManager.getModID().lowercase(Locale.getDefault()) + ":", "#y")
        desc = desc.replace("*", " ")
        desc = desc.replace("!M", "#b%d")
        desc = desc.replace("!D", "#b%d")
        desc = desc.replace("!B", "#b%d")
        desc = desc.replace("%d!", "%d")

        //        desc = desc.replace("%s", "");
        return desc
    }
}
