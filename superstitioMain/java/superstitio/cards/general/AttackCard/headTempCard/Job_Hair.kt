package superstitio.cards.general.AttackCard.headTempCard

import basemod.cardmods.EtherealMod
import basemod.cardmods.ExhaustMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.general.GeneralCard
import superstitioapi.SuperstitioApiSetup
import superstitioapi.cards.patch.GoSomewhereElseAfterUse
import superstitioapi.hangUpCard.CardOrb_CardTrigger
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CardUtility

class Job_Hair private constructor(damage: Int, upgradeDamage: Int) :
    GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card, GoSomewhereElseAfterUse
{
    constructor() : this(DAMAGE, UPGRADE_DAMAGE)
    {
        this.cardsToPreview = this.makeCardCopyWithDamageDecrease()
    }

    init
    {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(damage, upgradeDamage)
        CardModifierManager.addModifier(this, EtherealMod())
        this.setupMagicNumber(MAGIC)
    }

    private fun makeCardCopyWithDamageDecrease(): Job_Hair
    {
        val card = Job_Hair(DAMAGE, UPGRADE_DAMAGE)
        card.freeToPlayOnce = true
        CardModifierManager.addModifier(card, ExhaustMod())
        card.initializeDescription()
        return card
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
    }

    override fun upgradeAuto()
    {
        upgradeCardsToPreview()
    }

    override fun afterInterruptMoveToCardGroup(cardGroup: CardGroup)
    {
        val job_hair = this.makeCardCopyWithDamageDecrease()
        CardOrb_WaitCardTrigger(
            this,
            cardGroup,
            CardUtility.CostSmart(this.magicNumber)
        ) { orb: CardOrb_CardTrigger, card: AbstractCard? ->
            orb.StartHitCreature(AbstractDungeon.player)
            ActionUtility.addToBot_makeTempCardInBattle(job_hair, ActionUtility.BattleCardPlace.Hand, this.upgraded)
        }
            .setDiscardOnEndOfTurn()
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(1))
            .setTargetType(CardTarget.SELF)
            .addToBot_HangCard()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Job_Hair::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 1
        private const val DAMAGE = 5
        private const val UPGRADE_DAMAGE = 2
        private const val MAGIC = 3
    }
}
