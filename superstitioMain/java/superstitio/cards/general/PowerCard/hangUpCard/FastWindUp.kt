package superstitio.cards.general.PowerCard.hangUpCard

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.NormalCard
import superstitio.powers.AbstractSuperstitioPower
import superstitio.powers.SexualHeat
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm
import superstitioapi.hangUpCard.HangUpCardGroup
import superstitioapi.hangUpCard.ICardOrb_EachTime
import superstitioapi.hangUpCard.ICardOrb_WaitTime
import superstitioapi.utils.setDescriptionArgs

class FastWindUp : NormalCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(FastWindUpPower(AbstractDungeon.player, this.magicNumber))
    }

    override fun upgradeAuto()
    {
        upgradeBaseCost(COST_UPGRADED_NEW)
    }

    class FastWindUpPower(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(POWER_ID, owner, amount),
        OnOrgasm_onOrgasm
    {
        override fun onOrgasm(SexualHeatPower: SexualHeat)
        {
            for (i in 0 until this.amount)
            {
                this.flash()
                HangUpCardGroup.forEachHangUpCard {
                    if (it is ICardOrb_WaitTime || it is ICardOrb_EachTime)
                        it.forceAcceptAction(FastWindUp())
                }
                    .addToBotAsAbstractAction()
            }
        }

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(this.amount)
        }

        companion object
        {
            val POWER_ID: String = DataManager.MakeTextID(FastWindUpPower::class.java)
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(FastWindUp::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val COST_UPGRADED_NEW = 0
        private const val MAGIC = 1
    }
}

