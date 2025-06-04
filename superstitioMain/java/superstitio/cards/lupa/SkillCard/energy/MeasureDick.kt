package superstitio.cards.lupa.SkillCard.energy

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction
import com.megacrit.cardcrawl.actions.common.GainEnergyAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.IsLupaCard
import superstitio.cards.lupa.LupaCard
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.actions.ChoseCardFromHandCardSelectScreen
import kotlin.math.max
@IsLupaCard
class MeasureDick : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        ChoseCardFromHandCardSelectScreen { targetCard: AbstractCard ->
            AutoDoneInstantAction.addToBotAbstract {
                addToTop(
                    GainEnergyAction(
                        max((targetCard.makeCopy().costForTurn - targetCard.costForTurn), 0)
                    )
                )
                addToTop(DiscardSpecificCardAction(targetCard))
            }
        }
            .setAnyNumber(true)
            .setCanPickZero(true) //                        .setRetainFilter(card -> card.isCostModifiedForTurn || card.isCostModified)
            .setChoiceAmount(this.magicNumber)
            .setWindowText(String.format(cardStrings.getEXTENDED_DESCRIPTION(0), this.magicNumber))
            .addToBot()
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(MeasureDick::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 0
        private const val MAGIC = 3
        private const val UPGRADE_MAGIC = 1
    }
}
