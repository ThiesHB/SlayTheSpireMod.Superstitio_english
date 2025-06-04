package superstitio.cards.maso.PowerCard

import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitio.cardModifier.modifiers.card.BodyModificationTag
import superstitio.cards.IsMasoCard
import superstitio.cards.SuperstitioCard
import superstitio.cards.maso.MasoCard
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitio.powers.SexualHeat
import superstitioapi.SuperstitioApiSubscriber.AtManualDiscardSubscriber.AtManualDiscardPower
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.ActionUtility.VoidSupplier
import superstitioapi.utils.ListUtility
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.setDescriptionArgs
import java.util.function.Consumer
@IsMasoCard
class BodyModification_TattooAndPiercing : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        CardModifierManager.addModifier(this, BodyModificationTag())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        repeat(this.magicNumber) {
            addToBot_applyPower(BodyModification_TattooAndPiercingPower(1, randomName))
        }
    }

    override fun upgradeAuto()
    {
    }

    class BodyModification_TattooAndPiercingPower(amount: Int, name: String) :
        EasyBuildAbstractPowerForPowerCard(amount, false), AtManualDiscardPower
    {
        var tattooNames: MutableMap<String, Int>

        init
        {
            this.tattooNames = HashMap()
            tattooNames[name] = amount
            updateDescription()
        }

        override fun onExhaust(card: AbstractCard)
        {
            SexualHeat.useConsumer_addSexualHeat(
                this.owner,
                this.amount,
                Consumer<VoidSupplier>(AutoDoneInstantAction.Companion::addToTopAbstract)
            )
        }

        override fun atManualDiscard()
        {
            SexualHeat.useConsumer_addSexualHeat(
                this.owner,
                this.amount,
                Consumer<VoidSupplier>(AutoDoneInstantAction.Companion::addToTopAbstract)
            )
        }

        override fun updateDescriptionArgs()
        {
            val strings = arrayOf("")
            tattooNames.forEach { (name: String?, num: Int) ->
                strings[0] += String.format(
                    powerCard.cardStrings.getEXTENDED_DESCRIPTION(1), name, num
                )
            }
            setDescriptionArgs(this.amount, strings[0])
        }

        override fun onApplyPower(power: AbstractPower, target: AbstractCreature, source: AbstractCreature)
        {
            if (power !is BodyModification_TattooAndPiercingPower)
            {
                return
            }
            this.tattooNames = PowerUtility.mergeAndSumMaps(this.tattooNames, power.tattooNames)
            updateDescription()
        }

        override fun getDesc(): String = super.getDesc() + powerCard.cardStrings.getEXTENDED_DESCRIPTION(0)

        override fun makePowerCard(): SuperstitioCard
        {
            return BodyModification_TattooAndPiercing()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(BodyModification_TattooAndPiercing::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF
        val TattooNames: Array<String> = getCardStringsWithSFWAndFlavor(ID)
            .getEXTENDED_DESCRIPTION(2).split(",".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()

        private const val COST = 1
        private const val MAGIC = 2
        private const val UPGRADE_MAGIC = 1

        private val randomName: String
            get() = ListUtility.getRandomFromList(TattooNames, AbstractDungeon.cardRandomRng)
    }
}
