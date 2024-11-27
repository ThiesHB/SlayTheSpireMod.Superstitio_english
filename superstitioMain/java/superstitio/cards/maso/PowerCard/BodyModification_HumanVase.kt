package superstitio.cards.maso.PowerCard

import basemod.AutoAdd
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.card.BodyModificationTag
import superstitio.cards.SuperstitioCard
import superstitio.cards.maso.MasoCard
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitio.powers.Milk
import superstitioapi.utils.setDescriptionArgs

//瓶女
@AutoAdd.Ignore
class BodyModification_HumanVase : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        CardModifierManager.addModifier(this, BodyModificationTag())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(BodyModification_HumanVasePower(this.magicNumber))
    }

    override fun upgradeAuto()
    {
    }

    class BodyModification_HumanVasePower(amount: Int) : EasyBuildAbstractPowerForPowerCard(amount)
    {
        override fun onAttacked(info: DamageInfo, damageAmount: Int): Int
        {
            if (info.type != DamageType.NORMAL) return damageAmount
            addToBot_applyPower(Milk(owner, this.amount))
            return damageAmount
        }

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(this.amount)
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return BodyModification_HumanVase()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(BodyModification_HumanVase::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 3
        private const val MAGIC = 3
        private const val UPGRADE_MAGIC = 1
    }
}
