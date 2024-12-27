package superstitio.cards.maso.PowerCard

import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitio.cardModifier.modifiers.card.BodyModificationTag
import superstitio.cards.SuperstitioCard
import superstitio.cards.maso.MasoCard
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitioapi.hangUpCard.CardOrb
import superstitioapi.hangUpCard.HangUpCardGroup
import superstitioapi.utils.ListUtility
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.setDescriptionArgs
import kotlin.math.pow

class BodyModification_Prolapse : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        CardModifierManager.addModifier(this, BodyModificationTag())
        initializeDescription()
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(2.0.pow(magicNumber).toInt())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        repeat(this.magicNumber) { addToBot_applyPower(BodyModification_ProlapsePower(1, randomName)) }
    }

    override fun upgradeAuto()
    {
    }

    class BodyModification_ProlapsePower(amount: Int, name: String) :
        EasyBuildAbstractPowerForPowerCard(amount, false)
    {
        var prolapseNames: MutableMap<String, Int>

        init
        {
            this.prolapseNames = HashMap()
            prolapseNames[name] = amount
            updateDescription()
        }

        override fun onAttacked(info: DamageInfo, damageAmount: Int): Int
        {
            if (info.type != DamageType.NORMAL) return damageAmount
            if (!AbstractDungeon.player.hand.isEmpty)
            {
                val card = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng)
                AbstractDungeon.player.hand.moveToDiscardPile(card)
                card.triggerOnManualDiscard()
                GameActionManager.incrementDiscard(false)
                return (damageAmount.toDouble() / 2.0.pow(amount)).toInt()
            }
            val cards = ArrayList<CardOrb>()
            HangUpCardGroup.getEachCardOrb().forEach { cardOrb ->
                if (!cardOrb.ifShouldRemove())
                    cards.add(cardOrb)
            }
            if (cards.isEmpty())
                return damageAmount
            val cardOrb = ListUtility.getRandomFromList(cards, AbstractDungeon.cardRandomRng)
            cardOrb.cardGroupReturnAfterEvoke = AbstractDungeon.player.discardPile
            cardOrb.setTriggerDiscardIfMoveToDiscard()
            cardOrb.setShouldRemove()
            return (damageAmount.toDouble() / 2.0.pow(amount)).toInt()
        }

        override fun updateDescriptionArgs()
        {
            val strings = arrayOf("")
            prolapseNames.forEach { (name: String?, num: Int) ->
                strings[0] += String.format(powerCard.cardStrings.getEXTENDED_DESCRIPTION(1), name, num)
            }
            setDescriptionArgs(2.0.pow(amount).toInt(), strings[0])
        }

        override fun onApplyPower(power: AbstractPower, target: AbstractCreature, source: AbstractCreature)
        {
            if (power !is BodyModification_ProlapsePower)
            {
                return
            }
            this.prolapseNames = PowerUtility.mergeAndSumMaps(this.prolapseNames, power.prolapseNames)
            updateDescription()
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return BodyModification_Prolapse()
        }

        override fun getDesc(): String = super.getDesc() + powerCard.cardStrings.getEXTENDED_DESCRIPTION(0)
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(BodyModification_Prolapse::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        //    private static final int UPGRADE_MAGIC = 1;
        val ProlapseNames: Array<String> = getCardStringsWithSFWAndFlavor(ID)
            .getEXTENDED_DESCRIPTION(2).split(",".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
        private const val COST = 1

        //    private static final int COST_UPGRADED_NEW = 0;
        private const val MAGIC = 1
        private const val UPGRADE_MAGIC = 1

        private val randomName: String
            get() = ListUtility.getRandomFromList(ProlapseNames, AbstractDungeon.cardRandomRng)
    }
}
