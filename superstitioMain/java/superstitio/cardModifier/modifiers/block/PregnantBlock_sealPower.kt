package superstitio.cardModifier.modifiers.block

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitioapi.cards.DamageActionMaker

class PregnantBlock_sealPower(private val sealPower: MutableList<AbstractPower>, val sealCreature: AbstractCreature?) :
    PregnantBlock(ID)
{
    override fun onThisBlockDamaged(info: DamageInfo, lostAmount: Int)
    {
        super.onThisBlockDamaged(info, lostAmount)
        if (sealCreature == null || sealCreature.isDeadOrEscaped) return
        if (info.type != DamageType.NORMAL) return
        DamageActionMaker.maker(lostAmount, sealCreature)
            .setDamageType(DamageType.THORNS)
            .addToBot()
        super.onAttacked(info, lostAmount)
    }

    override fun getDescription(): String?
    {
        if (sealCreature == null) return super.getDescription()
        if (sealCreature is AbstractPlayer) return super.getDescription() + blockStrings!!.getEXTENDED_DESCRIPTION(0) + blockStrings.getEXTENDED_DESCRIPTION(
            1
        )
        return super.getDescription() + blockStrings!!.getEXTENDED_DESCRIPTION(0) + sealCreature.name
    }

    override fun makeCopy(): AbstractBlockModifier
    {
        return PregnantBlock_sealPower(this.sealPower, this.sealCreature)
    }

    override fun removeUnNaturally(info: DamageInfo?, remainingDamage: Int): Int
    {
        if (sealCreature == null || sealCreature.isDeadOrEscaped) return super.removeUnNaturally(info, remainingDamage)
        for (power in sealPower)
        {
            addToBot(ApplyPowerAction(this.owner, this.owner, power))
        }
        return super.removeUnNaturally(info, remainingDamage)
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(PregnantBlock_sealPower::class.java)
        const val THORNS_RATE: Int = 2
    }
}