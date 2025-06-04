package superstitio.cards.maso.PowerCard

import basemod.AutoAdd
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.card.BodyModificationTag
import superstitio.cards.IsMasoCard
import superstitio.cards.SuperstitioCard
import superstitio.cards.general.AbstractTempCard
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.general.FuckJob_Card.BodyPart
import superstitio.cards.maso.MasoCard
import superstitio.delayHpLose.RemoveDelayHpLoseBlock
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitio.powers.masoOnly.*
import superstitioapi.cards.HasMultiCardsToPreview
import superstitioapi.utils.CardUtility
import superstitioapi.utils.setDescriptionArgs
import java.util.*
import java.util.stream.Collectors
@IsMasoCard
class BodyModification_CutOff : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.setupBlock(BLOCK, UPGRADE_BLOCK, RemoveDelayHpLoseBlock())
        CardModifierManager.addModifier(this, BodyModificationTag())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val bodyParts = ArrayList<AbstractCard>()
        if (!AbstractDungeon.player.hasPower(LostBodyPart_Arm.POWER_ID)) bodyParts.add(
            BodyModification_CutOff_Chose(LostBodyPart_Arm())
        )
        if (!AbstractDungeon.player.hasPower(LostBodyPart_Breast.POWER_ID)) bodyParts.add(
            BodyModification_CutOff_Chose(LostBodyPart_Breast())
        )
        if (!AbstractDungeon.player.hasPower(LostBodyPart_Castration.POWER_ID)) bodyParts.add(
            BodyModification_CutOff_Chose(LostBodyPart_Castration())
        )
        if (!AbstractDungeon.player.hasPower(LostBodyPart_Head.POWER_ID)) bodyParts.add(
            BodyModification_CutOff_Chose(LostBodyPart_Head())
        )
        if (!AbstractDungeon.player.hasPower(LostBodyPart_Leg.POWER_ID)) bodyParts.add(
            BodyModification_CutOff_Chose(LostBodyPart_Leg())
        )
        if (bodyParts.isEmpty()) return
        addToBot_gainBlock()
        addToBot_applyPower(BodyModification_CutOffPower(this.magicNumber))
        this.addToBot(ChooseOneAction(bodyParts))
    }

    override fun upgradeAuto()
    {
    }

    class BodyModification_CutOffPower(amount: Int) : EasyBuildAbstractPowerForPowerCard(amount)
    {
        override fun atDamageFinalGive(damage: Float, type: DamageType): Float
        {
            return super.atDamageFinalGive(damage + damage * this.amount / PERCENTAGE, type)
        }

        override fun atDamageFinalGive(damage: Float, type: DamageType, card: AbstractCard): Float
        {
            return super.atDamageFinalGive(damage, type, card)
        }

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(amount)
        }

        override fun getDesc(): String = powerCard.cardStrings.getEXTENDED_DESCRIPTION(0)

        override fun makePowerCard(): SuperstitioCard
        {
            return BodyModification_CutOff()
        }

        companion object
        {
            const val PERCENTAGE: Int = 100
        }
    }

    @AutoAdd.Ignore
    class BodyModification_CutOff_Chose @JvmOverloads constructor(private val lostBodyPart: LostBodyPart? = null) :
        AbstractTempCard(
            ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET
        ), HasMultiCardsToPreview
    {
        override val multiCardsToPreview: MutableList<AbstractCard> = ArrayList()
        override var cardsToPreviewTimer = 0.0f

        init
        {
            if (lostBodyPart != null)
            {
                this.name = lostBodyPart.name
                this.rawDescription = lostBodyPart.description
                CardUtility.AllCardInBattle().forEach { card: AbstractCard ->
                    if (card !is FuckJob_Card) return@forEach
                    if (!Arrays.stream<BodyPart?>(lostBodyPart.banedBodyPart()).collect(Collectors.toList<BodyPart?>())
                            .contains(FuckJob_Card.getBodyPartType(card as FuckJob_Card))
                    ) return@forEach
                    if (multiCardsToPreview.stream()
                            .anyMatch { cardInList: AbstractCard -> cardInList.cardID == card.cardID }
                    ) return@forEach
                    multiCardsToPreview.add(card.makeCopy())
                }
                initializeDescription()
            }
        }

        override fun update()
        {
            super<AbstractTempCard>.update()
            super<HasMultiCardsToPreview>.update()
        }

        override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
        {
            this.onChoseThisOption()
        }

        override fun onChoseThisOption()
        {
            if (lostBodyPart != null)
                addToBot_applyPower(lostBodyPart)
        }

        override fun upgradeAuto()
        {
        }

        override fun setCardsToPreview(cardsToPreview: AbstractCard)
        {
            this.cardsToPreview = cardsToPreview
        }

        companion object
        {
            val ID: String = DataManager.MakeTextID(BodyModification_CutOff_Chose::class.java)

            val CARD_TYPE: CardType = CardType.POWER

            val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

            val CARD_TARGET: CardTarget = CardTarget.SELF

            private const val COST = -2
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(BodyModification_CutOff::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val MAGIC = 15
        private const val UPGRADE_MAGIC = 5
        private const val BLOCK = 14
        private const val UPGRADE_BLOCK = 4
    }
}
