package superstitio.cards.lupa.PowerCard.defend

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower
import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitio.SuperstitioImg.NoNeedImg
import superstitio.cards.SuperstitioCard
import superstitio.cards.lupa.LupaCard
import superstitio.cards.lupa.PowerCard.defend.DrinkSemenBeer.DrinkSemenBeerPower
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitio.powers.lupaOnly.SemenPower
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.powers.barIndepend.HasBarRenderOnCreature_Power
import superstitioapi.powers.interfaces.OnPostApplyThisPower
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleTips
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.setDescriptionArgs

//排出，然后喝
class DrinkSemenBeer : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.exhaust = true
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(DrinkSemenBeerPower(this.magicNumber))
    }

    override fun upgradeAuto()
    {
    }

    @NoNeedImg
    class DrinkSemenBeerPower(maxAmount: Int) : EasyBuildAbstractPowerForPowerCard(-1, false),
        InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount, HasBarRenderOnCreature_Power,
        OnPostApplyThisPower<DrinkSemenBeerPower>, BetterOnApplyPowerPower
    {
        //绘制相关
        private var maxAmount: Int
        private var semenAmount: Int

        init
        {
            this.name = powerCard.cardStrings.getEXTENDED_DESCRIPTION(0)
            this.maxAmount = maxAmount
            this.semenAmount = 0
            updateDescription()
        }

        fun CheckFull()
        {
            if (semenAmount >= maxBarAmount())
            {
                this.Full()
            }
        }

        private fun Full()
        {
            this.flash()
            val power: AbstractPower = this
            AutoDoneInstantAction.addToBotAbstract {
                PowerUtility.BubbleMessageHigher(
                    power,
                    false,
                    powerCard.cardStrings.getEXTENDED_DESCRIPTION(2)
                )
            }
            this.semenAmount = 0
            addToBot(HealAction(this.owner, this.owner, maxBarAmount()))
            AutoDoneInstantAction.addToBotAbstract {
                PowerUtility.BubbleMessageHigher(
                    power,
                    false,
                    powerCard.cardStrings.getEXTENDED_DESCRIPTION(3)
                )
            }
            addToBot_removeSpecificPower(this)
        }

        override fun renderAmount(sb: SpriteBatch, x: Float, y: Float, c: Color)
        {
        }

        override fun onApplyPower(power: AbstractPower, target: AbstractCreature?, source: AbstractCreature?)
        {
            if (power is DrinkSemenBeerPower)
            {
                this.maxAmount += power.maxAmount
            }
        }

        override fun InitializePostApplyThisPower(addedPower: DrinkSemenBeerPower)
        {
            CheckFull()
            updateDescription()
        }

        override fun getAmountForDraw(): Int = this.semenAmount

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(maxBarAmount())
        }

        override fun onVictory()
        {
            val p = AbstractDungeon.player
            if (p.currentHealth > 0)
            {
                p.heal(this.semenAmount)
            }
        }

        override var self = this

        override fun Height(): Float
        {
            return 20 * Settings.scale
        }

        override fun setupBarOriginColor(): Color
        {
            return Color.WHITE.cpy()
        }

        override fun maxBarAmount(): Int
        {
            return maxAmount
        }

        override fun betterOnApplyPower(
            power: AbstractPower,
            creature: AbstractCreature,
            creature1: AbstractCreature
        ): Boolean
        {
            if ((power is SemenPower) && power.amount > 0)
            {
                AutoDoneInstantAction.addToBotAbstract {
                    PowerUtility.BubbleMessageHigher(
                        power,
                        false,
                        powerCard.cardStrings.getEXTENDED_DESCRIPTION(1)
                    )
                }
                val semenPower = power as SemenPower
                this.semenAmount += power.amount * semenPower.getSemenValue()
                CheckFull()
                return true
            }
            return true
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return DrinkSemenBeer()
        }

        companion object
        {
            val POWER_ID: String = DataManager.MakeTextID(DrinkSemenBeerPower::class.java)
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(DrinkSemenBeer::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 0
        private const val MAGIC = 10
        private const val UPGRADE_MAGIC = 5
    }
}

