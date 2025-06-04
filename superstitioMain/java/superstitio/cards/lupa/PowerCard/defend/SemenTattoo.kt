package superstitio.cards.lupa.PowerCard.defend

import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitio.cardModifier.modifiers.block.SexBlock
import superstitio.cards.IsLupaCard
import superstitio.cards.SuperstitioCard
import superstitio.cards.lupa.LupaCard
import superstitio.cards.lupa.OnAddSemenPower
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitio.powers.lupaOnly.SemenPower
import superstitioapi.utils.setDescriptionArgs
@IsLupaCard
class SemenTattoo : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        BlockModifierManager.addModifier(this, SexBlock())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(SemenTattooPower(this.magicNumber))
    }

    override fun upgradeAuto()
    {
    }

    class SemenTattooPower(amount: Int) : EasyBuildAbstractPowerForPowerCard(amount), OnAddSemenPower
    {
        override fun updateDescriptionArgs()
        {
            this.setDescriptionArgs(amount)
        }

        override fun onAddSemen_shouldApply(power: AbstractPower?): Boolean
        {
            if (power !is SemenPower) return true
            val blockAdd = this.amount * power.amount * (power as SemenPower).getSemenValue()
            AbstractDungeon.actionManager.addToBottom(GainBlockAction(this.owner, blockAdd))
            return true
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return SemenTattoo()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(SemenTattoo::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2

        private const val MAGIC = 1
        private const val UPGRADE_MAGIC = 1
    }
}
