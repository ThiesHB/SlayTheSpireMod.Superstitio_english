package superstitio.cards.maso.SkillCard

import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.DataManager.CanOnlyDamageDamageType
import superstitio.cards.SuperstitioCard
import superstitio.cards.maso.MasoCard
import superstitio.delayHpLose.DelayRemoveDelayHpLoseBlock
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitio.powers.SexualDamage
import superstitioapi.utils.*

class STDRoulette : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupBlock(BLOCK, DelayRemoveDelayHpLoseBlock())
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_gainBlock()
        addToBot_applyPower(STDRoulettePower(this.magicNumber))
    }

    override fun upgradeAuto()
    {
    }

    class STDRoulettePower(amount: Int) : EasyBuildAbstractPowerForPowerCard(amount)
    {
        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(this.amount)
        }

        override fun getDescriptionStrings(): String
        {
            return powerCard.cardStrings.getEXTENDED_DESCRIPTION(0)
        }

        override fun atEndOfRound()
        {
            addToBot_removeSelf()
        }

        override fun wasHPLost(info: DamageInfo, damageAmount: Int)
        {
            if (info.type == CanOnlyDamageDamageType.UnBlockAbleDamageType) return
            for (monster in CreatureUtility.getAllAliveMonsters())
            {
                addToBot_applyPower(SexualDamage(monster, this.amount, this.owner))
            }
            if (AbstractDungeon.cardRandomRng.randomBoolean())
            {
                PowerUtility.BubbleMessage(
                    owner.hb, false,
                    powerCard.cardStrings.getEXTENDED_DESCRIPTION(1)
                )
            }
            else
            {
                PowerUtility.BubbleMessage(
                    owner.hb, false,
                    powerCard.cardStrings.getEXTENDED_DESCRIPTION(2)
                )
            }
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return STDRoulette()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(STDRoulette::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2
        private const val BLOCK = 16
        private const val MAGIC = 2
        private const val UPGRADE_MAGIC = 2
    }
}
