package superstitio.cards.lupa.PowerCard.defend

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.IsLupaCard
import superstitio.cards.SuperstitioCard
import superstitio.cards.lupa.LupaCard
import superstitio.delayHpLose.DelayHpLosePower
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitio.powers.SexualHeat
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onSquirt
import superstitioapi.utils.setDescriptionArgs
@IsLupaCard
class JuicyPussy : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(JuicyPussyPower(this.magicNumber))
    }

    override fun upgradeAuto()
    {
    }

    class JuicyPussyPower(amount: Int) : EasyBuildAbstractPowerForPowerCard(amount), OnOrgasm_onSquirt
    {
        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(amount)
        }

        override fun onSquirt(SexualHeatPower: SexualHeat, card: AbstractCard)
        {
            this.flash()
            DelayHpLosePower.addToBot_removePower(amount, AbstractDungeon.player, true)
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return JuicyPussy()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(JuicyPussy::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1

        private const val MAGIC = 3
        private const val UPGRADE_MAGIC = 1
    }
}

