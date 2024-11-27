package superstitio.cards.general.AttackCard.breast

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.general.GeneralCard
import superstitio.powers.Milk
import superstitioapi.SuperstitioApiSetup

class Job_Breast : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card
{
    init
    {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_dealDamage(monster!!, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
        addToBot_applyPower(Milk(monster, this.magicNumber))
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Job_Breast::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 2
        private const val DAMAGE = 18
        private const val UPGRADE_DAMAGE = 7
        private const val MAGIC = 4
    }
}
