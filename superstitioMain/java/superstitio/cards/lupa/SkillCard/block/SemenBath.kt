package superstitio.cards.lupa.SkillCard.block

import basemod.cardmods.RetainMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitio.cards.lupa.LupaCard
import superstitio.delayHpLose.DelayHpLosePower
import superstitio.delayHpLose.RemoveDelayHpLoseBlock
import superstitio.powers.lupaOnly.FloorSemen
import superstitio.powers.lupaOnly.InsideSemen
import superstitio.powers.lupaOnly.OutsideSemen

class SemenBath : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupBlock(BLOCK, UPGRADE_BLOCK, RemoveDelayHpLoseBlock())
        CardModifierManager.addModifier(this, RetainMod())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        DelayHpLosePower.addToBot_removePower(this.block * totalSemenValue, AbstractDungeon.player, true)
        getPower(FloorSemen.POWER_ID)?.let(this::addToBot_removeSpecificPower)
        getPower(OutsideSemen.POWER_ID)?.let(this::addToBot_removeSpecificPower)
        getPower(InsideSemen.POWER_ID)?.let(this::addToBot_removeSpecificPower)
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(SemenBath::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 3
        private const val BLOCK = 3
        private const val UPGRADE_BLOCK = 1

        private fun getPower(powerID: String): AbstractPower?
        {
            val power = AbstractDungeon.player.getPower(powerID)
            return power
        }
    }
}

