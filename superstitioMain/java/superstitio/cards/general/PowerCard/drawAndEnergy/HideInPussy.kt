package superstitio.cards.general.PowerCard.drawAndEnergy

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.SuperstitioCard
import superstitio.cards.NormalCard
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitioapi.actions.ChoseCardFromHandCardSelectScreen
import superstitioapi.utils.setDescriptionArgs

class HideInPussy : NormalCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(HideInPussyPower(this.magicNumber))
    }

    override fun upgradeAuto()
    {
        upgradeBaseCost(COST_UPGRADED_NEW)
    }

    class HideInPussyPower(amount: Int) : EasyBuildAbstractPowerForPowerCard(amount)
    {
        override fun atEndOfTurn(isPlayer: Boolean)
        {
            if (!isPlayer || AbstractDungeon.player.hand.isEmpty) return
            ChoseCardFromHandCardSelectScreen { card: AbstractCard ->
                card.freeToPlayOnce = true
                card.retain = true
            }
                .setWindowText(String.format(powerCard.cardStrings.getEXTENDED_DESCRIPTION(0), this.amount))
                .setChoiceAmount(this.amount)
                .setAnyNumber(true)
                .setCanPickZero(true)
                .addToBot()
        }

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(amount)
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return HideInPussy()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(HideInPussy::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2
        private const val COST_UPGRADED_NEW = 1
        private const val MAGIC = 1
    }
}

