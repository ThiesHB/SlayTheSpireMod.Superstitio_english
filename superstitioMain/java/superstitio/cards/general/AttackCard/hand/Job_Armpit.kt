package superstitio.cards.general.AttackCard.hand

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.general.GeneralCard
import superstitioapi.SuperstitioApiSetup
import superstitioapi.cards.DamageActionMaker
import superstitioapi.cards.patch.GoSomewhereElseAfterUse
import superstitioapi.hangUpCard.CardOrb_CardTrigger
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger
import superstitioapi.utils.CostSmart
import superstitioapi.utils.CreatureUtility
import java.util.function.Predicate

class Job_Armpit @JvmOverloads constructor(hasCardToPreview: Boolean = true) :
    GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card, GoSomewhereElseAfterUse
{
    init
    {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC)
        if (hasCardToPreview) this.cardsToPreview = Job_Armpit(false)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
    }

    override fun upgradeAuto()
    {
        upgradeCardsToPreview()
    }

    override fun afterInterruptMoveToCardGroup(cardGroup: CardGroup)
    {
        CardOrb_WaitCardTrigger(
            this,
            cardGroup,
            CostSmart(this.magicNumber)
        ) { orb: CardOrb_CardTrigger, card: AbstractCard? ->
            val creature = CreatureUtility.getMonsterOrRandomMonster(orb.lastTarget)
            orb.StartHitCreature(creature)
            DamageActionMaker.maker(orb.originCard.damage, creature)
                .setEffect(SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
                .setExampleCard(this).addToBot()
        }
            .setCardPredicate(Predicate { card: AbstractCard -> card.type == CardType.ATTACK })
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(1))
            .addToBot_HangCard()
        val copyCard = this.makeCopy()
        if (upgraded) copyCard.upgrade()
        CardOrb_WaitCardTrigger(
            copyCard,
            cardGroup,
            CostSmart(this.magicNumber)
        ) { orb: CardOrb_CardTrigger, card: AbstractCard? ->
            val creature = CreatureUtility.getMonsterOrRandomMonster(orb.lastTarget)
            orb.StartHitCreature(creature)
            DamageActionMaker.maker(orb.originCard.damage, creature)
                .setEffect(SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
                .setExampleCard(this).addToBot()
        }
            .setCardPredicate(Predicate { card: AbstractCard -> card.type == CardType.ATTACK })
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(1))
            .addToBot_HangCard()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Job_Armpit::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 0
        private const val DAMAGE = 4
        private const val UPGRADE_DAMAGE = 2
        private const val MAGIC = 2
    }
}
