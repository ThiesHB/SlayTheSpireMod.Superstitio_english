package superstitio.cards.maso.PowerCard

import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.StrengthPower
import superstitio.DataManager
import superstitio.DataManager.CanOnlyDamageDamageType
import superstitio.cards.SuperstitioCard
import superstitio.cards.maso.MasoCard
import superstitio.powers.AbstractSuperstitioPower
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitioapi.utils.addToBot_applyPower
import superstitioapi.utils.addToBot_removeSelf
import superstitioapi.utils.setDescriptionArgs

//每当造成攻击伤害或失去生命时，获得 !M! 力量
class SadismForm : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(SadismFormPower(this.magicNumber))
    }

    override fun upgradeAuto()
    {
    }

    class SadismFormPower(amount: Int) : EasyBuildAbstractPowerForPowerCard(amount)
    {
        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(this.amount)
        }

        override fun wasHPLost(info: DamageInfo, damageAmount: Int)
        {
            if (info.type == CanOnlyDamageDamageType.UnBlockAbleDamageType) return
            if (damageAmount > 0)
            {
                addToBot_applyPower(GetStrengthNextTurn(this.owner, this.amount))
            }
        }

        override fun onAttack(info: DamageInfo, damageAmount: Int, target: AbstractCreature)
        {
            if (damageAmount > 0 && info.type == DamageType.NORMAL)
            {
                addToBot_applyPower(GetStrengthNextTurn(this.owner, this.amount))
            }
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return SadismForm()
        }
    }

    class GetStrengthNextTurn(owner: AbstractCreature, amount: Int) :
        AbstractSuperstitioPower(POWER_ID, owner, amount)
    {
        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(this.amount)
        }

        override fun atStartOfTurn()
        {
            addToBot_applyPower(StrengthPower(this.owner, this.amount))
            //            addToBot_applyPower(new LoseStrengthPower(this.owner, this.amount));
            addToBot_removeSelf()
        }

        companion object
        {
            val POWER_ID: String = DataManager.MakeTextID(GetStrengthNextTurn::class.java)
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(SadismForm::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 3
        private const val MAGIC = 2
        private const val UPGRADE_MAGIC = 1
    }
}

