package superstitio.cards.general.PowerCard.monsterGirl

import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.SuperstitioCard
import superstitio.cards.general.AbstractTempCard
import superstitio.powers.AbstractSuperstitioPower
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitioapi.utils.addToBot_applyPower
import superstitioapi.utils.setDescriptionArgs

class SlimeGirlMode : AbstractTempCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        this.onChoseThisOption()
    }

    override fun onChoseThisOption()
    {
        addToBot_applyPower(SlimeGirlModePower(this.magicNumber))
    }

    override fun upgradeAuto()
    {
    }

    class SlimeGirlModePower(amount: Int) : EasyBuildAbstractPowerForPowerCard(amount)
    {
        override fun atStartOfTurn()
        {
            addToBot_applyPower(PlayerFlightPower(AbstractDungeon.player, this.amount))
        }

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(amount)
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return SlimeGirlMode()
        }
    }

    class PlayerFlightPower(owner: AbstractPlayer, amount: Int) : AbstractSuperstitioPower(POWER_ID, owner, amount)
    {
        private fun calculateDamageTakenAmount(damage: Float, type: DamageType): Float
        {
            if (type == DamageType.NORMAL) return damage / 2.0f
            return damage
        }

        override fun atDamageFinalReceive(damage: Float, type: DamageType): Float
        {
            return this.calculateDamageTakenAmount(damage, type)
        }

        override fun atStartOfTurn()
        {
            //Nothing Happened
        }

        override fun onAttacked(info: DamageInfo, damageAmount: Int): Int
        {
            val willLive =
                this.calculateDamageTakenAmount(damageAmount.toFloat(), info.type) < owner.currentHealth.toFloat()
            if (info.owner != null && info.type == DamageType.NORMAL && damageAmount > 0 && willLive)
            {
                this.flash()
                this.addToBot(ReducePowerAction(this.owner, this.owner, this.ID, 1))
            }

            return damageAmount
        }

        override fun onRemove()
        {
            //Nothing Happened
        }

        //        public void playApplyPowerSfx() {
        //            CardCrawlGame.sound.play("POWER_FLIGHT", 0.05F);
        //        }
        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(this.amount)
        }


        companion object
        {
            val POWER_ID: String = DataManager.MakeTextID(PlayerFlightPower::class.java)
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(SlimeGirlMode::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = -2
        private const val MAGIC = 2
    }
}
