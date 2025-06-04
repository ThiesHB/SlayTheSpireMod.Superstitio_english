package superstitio.cards.maso.PowerCard

import com.megacrit.cardcrawl.actions.common.GainEnergyAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.IsMasoCard
import superstitio.cards.SuperstitioCard
import superstitio.cards.maso.MasoCard
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitio.powers.SexualHeat
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm
import superstitioapi.utils.setDescriptionArgs
@IsMasoCard
class Sensitive3000 : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(JuicyPussy_TempPower(this.magicNumber))
    }

    override fun upgradeAuto()
    {
        upgradeBaseCost(COST_UPGRADED_NEW)
    }

    class JuicyPussy_TempPower(amount: Int) : EasyBuildAbstractPowerForPowerCard(amount), OnOrgasm
    {
        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(amount)
        }

        override fun onOrgasm(SexualHeatPower: SexualHeat)
        {
            this.flash()
            this.addToBot(GainEnergyAction(this.amount))
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return Sensitive3000()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Sensitive3000::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2
        private const val COST_UPGRADED_NEW = 1

        private const val MAGIC = 1
        private const val UPGRADE_MAGIC = 0
    }
}

