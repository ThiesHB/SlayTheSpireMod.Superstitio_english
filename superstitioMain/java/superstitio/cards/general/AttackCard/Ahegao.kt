package superstitio.cards.general.AttackCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.damage.SexDamage
import superstitio.cards.general.GeneralCard
import superstitioapi.SuperstitioApiSetup
import superstitioapi.cards.DamageActionMaker
import superstitioapi.cards.patch.GoSomewhereElseAfterUse
import superstitioapi.hangUpCard.CardOrb_CardTrigger
import superstitioapi.hangUpCard.CardOrb_EachCardTrigger
import superstitioapi.utils.CostSmart
import superstitioapi.utils.CreatureUtility

class Ahegao : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), GoSomewhereElseAfterUse
{
    init
    {
        this.setupDamage(DAMAGE, SexDamage())
//        this.setupMagicNumber(MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
    }

    override fun upgradeAuto()
    {
        upgradeBaseCost(COST_UPGRADED_NEW)
    }

    override fun afterInterruptMoveToCardGroup(cardGroup: CardGroup)
    {
        CardOrb_EachCardTrigger(this, cardGroup, CostSmart(CostSmart.CostType.NaN))
        { orb: CardOrb_CardTrigger, card: AbstractCard? ->
            val creature = CreatureUtility.getMonsterOrRandomMonster(orb.lastTarget)
            orb.StartHitCreature(creature)
            DamageActionMaker.maker(orb.originCard.damage, creature).setExampleCard(this)
                .setEffect(SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
                .addToBot()
        }
            .setCardPredicate({ card: AbstractCard -> card.type == CardType.ATTACK })
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(0))
            .addToBot_HangCard()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Ahegao::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.ALL_ENEMY

        private const val COST = 2
        private const val COST_UPGRADED_NEW = 1
        private const val DAMAGE = 1
//        private const val MAGIC = 99
    }
}
