package superstitio.cards.maso.PowerCard

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower
import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.IsMasoCard
import superstitio.cards.SuperstitioCard
import superstitio.cards.maso.MasoCard
import superstitio.powers.AbstractSuperstitioPower
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitioapi.utils.addToBot_AutoRemoveOne
import superstitioapi.utils.addToBot_applyPower
import superstitioapi.utils.setDescriptionArgs
@IsMasoCard
class DeathDoor : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(DeathDoorPower(this.magicNumber))
    }

    override fun upgradeAuto()
    {
    }

    class DeathDoorPower(amount: Int) : EasyBuildAbstractPowerForPowerCard(amount), OnPlayerDeathPower
    {
        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(this.amount)
        }

        override fun onPlayerDeath(abstractPlayer: AbstractPlayer, DamageInfo: DamageInfo?): Boolean
        {
            if (this.amount == 0) return true
            this.flash()
            addToBot(HealAction(this.owner, this.owner, 1))
            addToBot_applyPower(AtDeathDoor(this.owner))
            addToBot_AutoRemoveOne(this)
            return false
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return DeathDoor()
        }
    }

    class AtDeathDoor(owner: AbstractCreature) : AbstractSuperstitioPower(POWER_ID, owner, -1)
    {
        override fun updateDescriptionArgs()
        {
        }

        override fun onAttackedToChangeDamage(info: DamageInfo, damageAmount: Int): Int
        {
            this.flash()
            if (info.type == DamageType.HP_LOSS) return 0
            if (info.type == DamageType.THORNS) return 0
            return super.onAttackedToChangeDamage(info, damageAmount)
        }

        companion object
        {
            val POWER_ID: String = DataManager.MakeTextID(AtDeathDoor::class.java)
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(DeathDoor::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1

        private const val MAGIC = 1
        private const val UPGRADE_MAGIC = 1
    }
}

