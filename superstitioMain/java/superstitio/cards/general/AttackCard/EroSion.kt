package superstitio.cards.general.AttackCard

import com.megacrit.cardcrawl.actions.common.LoseHPAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitioapi.SuperstitioApiSetup
import superstitioapi.actions.AutoDoneInstantAction

class EroSion : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupDamage(DAMAGE)
        this.setupMagicNumber(MAGIC)
    }

    private fun updateDamage()
    {
        val damageUp = sumAllDelayHpLosePower()
        //        if (damageUp >= 1)
//            this.isDamageModified = true;
        this.baseDamage = (originDamage + damageUp).toInt()
    }

    override fun upgradeAuto()
    {
        upgradeBaseCost(COST_UPGRADED_NEW)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot(LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber))
        val self: AbstractCard = this
        AutoDoneInstantAction.addToBotAbstract {
            calculateCardDamage(monster!!)
            addToBot_dealDamage(monster, self.damage, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
        }
    }

    override fun applyPowers()
    {
        super.applyPowers()
        updateDamage()
    }

    override fun onMoveToDiscard()
    {
        this.initializeDescription()
    }

    override fun calculateCardDamage(monster: AbstractMonster?)
    {
        updateDamage()
        super.calculateCardDamage(monster)
        this.initializeDescription()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(EroSion::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 1
        private const val COST_UPGRADED_NEW = 0
        private const val DAMAGE = 0
        private const val originDamage = DAMAGE
        private const val MAGIC = 5
    }
}
