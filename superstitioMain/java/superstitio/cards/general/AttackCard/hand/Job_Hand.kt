package superstitio.cards.general.AttackCard.hand

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.StrengthPower
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.general.GeneralCard
import superstitioapi.SuperstitioApiSetup
import superstitioapi.cards.patch.GoSomewhereElseAfterUse
import superstitioapi.hangUpCard.CardOrb_CardTrigger
import superstitioapi.hangUpCard.CardOrb_EachCardTrigger
import superstitioapi.utils.CostSmart
import superstitioapi.utils.setDescriptionArgs

class Job_Hand : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card, GoSomewhereElseAfterUse
{
    init
    {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
    }

    override fun upgradeAuto()
    {
    }

    override fun afterInterruptMoveToCardGroup(cardGroup: CardGroup)
    {
        CardOrb_EachCardTrigger(
            this,
            cardGroup,
            CostSmart(this.magicNumber)
        ) { orb: CardOrb_CardTrigger, card: AbstractCard? ->
            orb.StartHitCreature(AbstractDungeon.player)
            addToBot_applyPower(StrengthPower(AbstractDungeon.player, STRENGTH_GET))
        }
            .setDiscardOnEndOfTurn()
            .setCardPredicate({ it.type == CardType.ATTACK })
            .setTargetType(CardTarget.SELF)
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(1), STRENGTH_GET)
            .addToBot_HangCard()
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(STRENGTH_GET)
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Job_Hand::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 1
        private const val DAMAGE = 4
        private const val UPGRADE_DAMAGE = 2
        private const val MAGIC = 3
        private const val UPGRADE_MAGIC = 1
        private const val STRENGTH_GET = 1
    }
}
