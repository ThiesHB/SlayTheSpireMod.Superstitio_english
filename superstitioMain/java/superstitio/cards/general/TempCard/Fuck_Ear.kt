package superstitio.cards.general.TempCard

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.FrailPower
import superstitio.DataManager
import superstitio.cards.general.AbstractTempCard
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.maso.AttackCard.Fuck_Eye
import superstitio.delayHpLose.RemoveDelayHpLoseBlock
import superstitioapi.SuperstitioApiSetup
import superstitioapi.cards.addExhaustMod
import superstitioapi.utils.ActionUtility

class Fuck_Ear(blank: Boolean) : AbstractTempCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card
{
    constructor() : this(false)
    {
        this.cardsToPreview = Fuck_Eye(false)
    }

    init
    {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.addExhaustMod()
        this.setupBlock(BLOCK, UPGRADE_BLOCK, RemoveDelayHpLoseBlock())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
        addToBot_gainBlock()
        ActionUtility.addToBot_makeTempCardInBattle(Fuck_Eye(), ActionUtility.BattleCardPlace.Hand, this.upgraded)
        addToBot_applyPower(FrailPower(AbstractDungeon.player, 1, false))
    }

    override fun upgradeAuto()
    {
        upgradeCardsToPreview()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Fuck_Ear::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 0
        private const val DAMAGE = 2
        private const val UPGRADE_DAMAGE = 1
        private const val BLOCK = 4
        private const val UPGRADE_BLOCK = 1
    }
}
