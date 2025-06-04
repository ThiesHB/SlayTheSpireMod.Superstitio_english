package superstitio.cards.general.SkillCard

import basemod.helpers.TooltipInfo
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.NormalCard
import superstitio.cards.general.TempCard.GiveBirth
import superstitio.monster.ChibiKindMonster
import superstitioapi.hangUpCard.CardOrb
import superstitioapi.hangUpCard.onDamage.CardOrb_BlockDamage
import superstitioapi.pet.Minion
import superstitioapi.pet.PetManager
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CardUtility
import superstitioapi.utils.CostSmart

//@AutoAdd.Ignore
class HaveBirthWith : NormalCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupBlock(BLOCK, UPGRADE_BLOCK)
        this.cardsToPreview = GiveBirth()
    }

    private fun ForPlayer(player: AbstractPlayer)
    {
        addToTop_HangUpOrb(ChibiKindMonster(), ChibiKindMonster.MinionChibi(ChibiKindMonster()))
    }

    private fun ForMonster(monster: AbstractMonster)
    {
        addToTop_HangUpOrb(monster)
    }

    private fun addToTop_HangUpOrb(father: AbstractMonster, sealCreature: Minion? = null)
    {
        val copyCard = this.makeStatEquivalentCopy() as HaveBirthWith
        copyCard.exhaust = true
        copyCard.purgeOnUse = true

        if (father !is ChibiKindMonster)
            copyCard.addedToolTipsTop.add(TooltipInfo(cardStrings.getEXTENDED_DESCRIPTION(1), father.name))
        else
            copyCard.addedToolTipsTop.add(
                TooltipInfo(
                    cardStrings.getEXTENDED_DESCRIPTION(1),
                    cardStrings.getEXTENDED_DESCRIPTION(2)
                )
            )

        Card_Orb_HaveBirth(copyCard, null, CostSmart(this.block), father, sealCreature)
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(0))
            .addToBot_HangCard()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val target = CardUtility.getSelfOrEnemyTarget(this, monster)
        if (target is AbstractPlayer)
            ForPlayer(AbstractDungeon.player)
        else
            ForMonster(target as AbstractMonster)
        ActionUtility.addToBot_makeTempCardInBattle(GiveBirth(), ActionUtility.BattleCardPlace.Discard, upgraded)
    }

    override fun upgradeAuto()
    {
        upgradeCardsToPreview()
    }

    class Card_Orb_HaveBirth @JvmOverloads constructor(
        card: AbstractCard,
        cardGroupReturnAfterEvoke: CardGroup?,
        OrbCounter: CostSmart,
        private val father: AbstractMonster,
        private val sealCreature: Minion? = null
    ) :
        CardOrb_BlockDamage(card, cardGroupReturnAfterEvoke, OrbCounter, actionOnNaturalRemove = {
            if (sealCreature == null)
                PetManager.spawnMinion(father.javaClass)
            else
                PetManager.spawnMonster(sealCreature)
        }), GiveBirth.IPregnantCardOrb
    {
        override fun makeCopy(): CardOrb
        {
            return Card_Orb_HaveBirth(originCard, cardGroupReturnAfterEvoke, orbCounter, father, sealCreature)
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(HaveBirthWith::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = SelfOrEnemyTargeting.SELF_OR_ENEMY

        private const val COST = 1
        private const val BLOCK = 10
        private const val UPGRADE_BLOCK = 3
    }
}
