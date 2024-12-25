package superstitio.cards.lupa.SkillCard.getSemen

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.lupa.LupaCard
import superstitio.cards.lupa.SkillCard.getSemen.ExposeSelf.ExposeSelfPower
import superstitio.delayHpLose.RemoveDelayHpLoseBlock
import superstitio.powers.AbstractSuperstitioPower
import superstitio.powers.lupaOnly.OutsideSemen
import superstitioapi.utils.addToBot_AutoRemoveOne

/**
 * 抵消敌人的攻击，转换为精液
 */
class ExposeSelf : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupBlock(BLOCK, UPGRADE_BLOCK, RemoveDelayHpLoseBlock())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_gainBlock()
        addToBot_applyPower(ExposeSelfPower(AbstractDungeon.player, 1))
    }

    override fun upgradeAuto()
    {
    }

    class ExposeSelfPower(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(POWER_ID, owner, amount)
    {
        override fun onAttacked(info: DamageInfo, damageAmount: Int): Int
        {
            if (info.type == DamageType.NORMAL)
            {
                this.flash()
                this.addToBot(
                    ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        OutsideSemen(AbstractDungeon.player, damageAmount / 2)
                    )
                )
            }
            return super.onAttacked(info, damageAmount)
        }

        override fun atStartOfTurn()
        {
            addToBot_AutoRemoveOne(this)
        }

        override fun updateDescriptionArgs()
        {
        }

        companion object
        {
            val POWER_ID: String = DataManager.MakeTextID(ExposeSelfPower::class.java)
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(ExposeSelf::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val BLOCK = 9
        private const val UPGRADE_BLOCK = 3
    }
}
