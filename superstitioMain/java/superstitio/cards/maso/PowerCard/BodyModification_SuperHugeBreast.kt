package superstitio.cards.maso.PowerCard

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
import superstitioapi.utils.addToBot_applyPower
import superstitioapi.utils.setDescriptionArgs

class BodyModification_SuperHugeBreast : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        CardModifierManager.addModifier(this, BodyModificationTag())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(BodyModification_SuperHugeBreastPower(this.magicNumber))
    }

    override fun upgradeAuto()
    {
    }

    class BodyModification_SuperHugeBreastPower(amount: Int) : EasyBuildAbstractPowerForPowerCard(amount)
    {
        override fun wasHPLost(info: DamageInfo, damageAmount: Int)
        {
            if (info.type != DamageType.NORMAL) return
            addToBot_applyPower(Milk(owner, this.amount))
        }

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(this.amount)
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return BodyModification_SuperHugeBreast()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(BodyModification_SuperHugeBreast::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2
        private const val MAGIC = 4
        private const val UPGRADE_MAGIC = 2
    }
}
