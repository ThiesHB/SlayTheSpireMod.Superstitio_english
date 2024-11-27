package superstitio.cards.general.PowerCard.monsterGirl

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.SuperstitioCard
import superstitio.cards.general.AbstractTempCard
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitio.powers.sexualHeatNeedModifier.ChokeChokerPower
import superstitioapi.utils.setDescriptionArgs

class FishGirlMode : AbstractTempCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    init {
        this.setupMagicNumber(MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        this.onChoseThisOption()
    }

    override fun onChoseThisOption() {
        addToBot_applyPower(FishGirlModePower(this.magicNumber))
    }

    override fun upgradeAuto() {
    }

    class FishGirlModePower(amount: Int) : EasyBuildAbstractPowerForPowerCard(amount) {
        override fun atStartOfTurn() {
            addToBot_applyPower(ChokeChokerPower(owner, amount))
        }

        override fun updateDescriptionArgs() {
            setDescriptionArgs(amount)
        }

        override fun makePowerCard(): SuperstitioCard {
            return FishGirlMode()
        }
    }

    companion object {
        val ID: String = DataManager.MakeTextID(FishGirlMode::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = -2
        private const val MAGIC = 1
    }
}
