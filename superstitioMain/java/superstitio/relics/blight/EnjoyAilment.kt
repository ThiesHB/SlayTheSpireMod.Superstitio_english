package superstitio.relics.blight

import basemod.AutoAdd.Seen
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.powers.FrailPower
import com.megacrit.cardcrawl.powers.VulnerablePower
import com.megacrit.cardcrawl.powers.WeakPower
import superstitio.DataManager
import superstitio.powers.AbstractSuperstitioPower
import superstitio.relics.SuperstitioRelic
import superstitio.relics.blight.EnjoyAilment.DoubleRemoveHpLostWhenHasVulnerablePower
import superstitioapi.relicToBlight.InfoBlight
import superstitioapi.relicToBlight.InfoBlight.BecomeInfoBlight
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.setDescriptionArgs

@Seen
class EnjoyAilment : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND), BecomeInfoBlight
{
    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(
            DoubleRemoveHpLostWhenHasVulnerablePower.BLOCK_RATE,
            DoubleRemoveHpLostWhenHasVulnerablePower.DAMAGE_RATE,
            DoubleRemoveHpLostWhenHasVulnerablePower.BLOCK_RATE,
            DoubleRemoveHpLostWhenHasVulnerablePower.DAMAGE_RATE
        )
    }

    override fun atBattleStart()
    {
        ActionUtility.addToBot_applyPower(DoubleRemoveHpLostWhenHasVulnerablePower(AbstractDungeon.player))
    }

    override fun obtain()
    {
        InfoBlight.obtain(this)
    }

    override fun instantObtain(p: AbstractPlayer, slot: Int, callOnEquip: Boolean)
    {
        InfoBlight.instanceObtain(this, callOnEquip)
    }

    override fun instantObtain()
    {
        InfoBlight.instanceObtain(this, true)
    }

    private class DoubleRemoveHpLostWhenHasVulnerablePower(owner: AbstractCreature) :
        AbstractSuperstitioPower(
            POWER_ID, owner, -1
        ), InvisiblePower
    {
        override fun modifyBlockLast(blockAmount: Float): Float
        {
            var blockLast = super.modifyBlockLast(blockAmount)
            if (owner.powers.stream().anyMatch { power: AbstractPower? -> power is WeakPower })
            {
                blockLast *= (1 + 1f / BLOCK_RATE)
            }
            if (owner.powers.stream().anyMatch { power: AbstractPower? -> power is VulnerablePower })
            {
                blockLast *= (1 + 1f / BLOCK_RATE)
            }
            return blockLast
        }

        override fun atDamageGive(damage: Float, type: DamageType): Float
        {
            var damageLast = super.atDamageGive(damage, type)
            if (owner.powers.stream().anyMatch { power: AbstractPower? -> power is FrailPower })
            {
                damageLast *= (1 + 1f / DAMAGE_RATE)
            }
            if (owner.powers.stream().anyMatch { power: AbstractPower? -> power is VulnerablePower })
            {
                damageLast *= (1 + 1f / DAMAGE_RATE)
            }
            return damageLast
        }

        override fun updateDescriptionArgs()
        {
        }

        companion object
        {
            val POWER_ID: String =
                DataManager.MakeTextID(DoubleRemoveHpLostWhenHasVulnerablePower::class.java)
            const val BLOCK_RATE: Int = 3
            const val DAMAGE_RATE: Int = 3
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(EnjoyAilment::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.SPECIAL

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT
    }
}