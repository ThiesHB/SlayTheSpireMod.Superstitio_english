package superstitio.cards.lupa.SkillCard.block

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.SuperstitioImg.NoNeedImg
import superstitio.cards.IsLupaCard
import superstitio.cards.lupa.LupaCard
import superstitio.powers.AbstractSuperstitioPower
import superstitioapi.utils.addToBot_reducePowerToOwner
import superstitioapi.utils.setDescriptionArgs

//春药
@IsLupaCard
class Philter : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun upgradeAuto()
    {
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(
            SexPlateArmorPower(
                AbstractDungeon.player,
                AbstractDungeon.player.currentBlock / this.magicNumber
            )
        )
        addToBot(RemoveAllBlockAction(AbstractDungeon.player, AbstractDungeon.player))
    }

    @NoNeedImg
    class SexPlateArmorPower(owner: AbstractCreature, amount: Int) :
        AbstractSuperstitioPower(POWER_ID, owner, amount)
    {
        init
        {
            this.loadRegion("platedarmor")
        }

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(this.amount)
        }

        override fun playApplyPowerSfx()
        {
            CardCrawlGame.sound.play("POWER_PLATED", 0.05f)
        }

        override fun wasHPLost(info: DamageInfo, damageAmount: Int)
        {
            if (info.owner != null && info.owner !== owner && info.type != DamageType.HP_LOSS && info.type != DamageType.THORNS && damageAmount > 0)
            {
                this.flash()
                addToBot_reducePowerToOwner(POWER_ID, 1)
            }
        }

        override fun atEndOfTurnPreEndTurnCards(isPlayer: Boolean)
        {
            this.flash()
            this.addToBot(GainBlockAction(this.owner, this.owner, this.amount))
        }

        companion object
        {
            val POWER_ID: String = DataManager.MakeTextID(SexPlateArmorPower::class.java)
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Philter::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val MAGIC = 3
        private const val UPGRADE_MAGIC = -1
    }
}
