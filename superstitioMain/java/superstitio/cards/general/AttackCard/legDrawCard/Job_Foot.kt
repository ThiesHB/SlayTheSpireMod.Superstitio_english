package superstitio.cards.general.AttackCard.legDrawCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.NormalCard
import superstitio.cards.lupa.BaseCard.Masturbate
import superstitioapi.SuperstitioApiSetup
import superstitioapi.cards.patch.GoSomewhereElseAfterUse
import superstitioapi.hangUpCard.CardOrb_CardTrigger
import superstitioapi.hangUpCard.CardOrb_EachCardTrigger
import superstitioapi.utils.CostSmart
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.setDescriptionArgs

class Job_Foot : NormalCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card, GoSomewhereElseAfterUse
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

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(DRAW_CARD)
    }

    override fun afterInterruptMoveToCardGroup(cardGroup: CardGroup)
    {
        CardOrb_EachCardTrigger(
            this,
            cardGroup,
            CostSmart(this.magicNumber)
        ) { orb: CardOrb_CardTrigger, card: AbstractCard? ->
            orb.StartHitCreature(AbstractDungeon.player)
            addToBot_drawCards(DRAW_CARD)
            PowerUtility.BubbleMessage(
                orb.originCard.hb, false,
                getCardStringsWithSFWAndFlavor(
                    DataManager.MakeTextID(Masturbate::class.java)
                )
                    .getEXTENDED_DESCRIPTION(0)
            )
        }
            .setDiscardOnEndOfTurn()
            .setCardPredicate({ it is FuckJob_Card })
            .setTargetType(CardTarget.SELF)
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(1), DRAW_CARD)
            .addToBot_HangCard()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Job_Foot::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 1
        private const val DAMAGE = 4
        private const val UPGRADE_DAMAGE = 2
        private const val DRAW_CARD = 1
        private const val MAGIC = 3
        private const val UPGRADE_MAGIC = 1
    }
}
