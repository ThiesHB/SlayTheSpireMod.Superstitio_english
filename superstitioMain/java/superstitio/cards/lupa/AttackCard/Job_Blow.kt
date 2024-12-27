package superstitio.cards.lupa.AttackCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.lupa.LupaCard
import superstitio.powers.SexualHeat
import superstitioapi.SuperstitioApiSetup
import superstitioapi.cards.patch.GoSomewhereElseAfterUse
import superstitioapi.hangUpCard.CardOrb_CardTrigger
import superstitioapi.hangUpCard.CardOrb_EachCardTrigger
import superstitioapi.utils.CostSmart
import java.util.function.Predicate

class Job_Blow : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card, GoSomewhereElseAfterUse
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
            SexualHeat.addToBot_addSexualHeat(AbstractDungeon.player, HEAT_GIVE)
        }
            .setCardPredicate( { it is FuckJob_Card })
            .setTargetType(CardTarget.SELF)
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(1))
            .addToBot_HangCard()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Job_Blow::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 0
        private const val DAMAGE = 5
        private const val UPGRADE_DAMAGE = 3
        private const val MAGIC = 4
        private const val UPGRADE_MAGIC = 4
        private const val HEAT_GIVE = 1
    }
}
