package superstitio.cards.maso.SkillCard

import basemod.helpers.TooltipInfo
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.orbs.AbstractOrb
import com.megacrit.cardcrawl.powers.*
import superstitio.DataManager
import superstitio.cards.general.TempCard.GiveBirth
import superstitio.cards.general.TempCard.SelfReference
import superstitio.cards.maso.MasoCard
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.hangUpCard.CardOrb_BlockDamage
import superstitioapi.hangUpCard.HangUpCardGroup
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CardUtility
import superstitioapi.utils.CostSmart

//TODO 增加一个按照怪物体型获得格挡的效果
class UnBirth : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupBlock(BLOCK, UPGRADE_BLOCK)
        this.cardsToPreview = GiveBirth()
        //this.exhaust = true;
    }

    private fun ForPlayer(player: AbstractPlayer)
    {
        val sealPower = doSealPowers(player.powers)
        addToTop_HangUpOrb(sealPower, player)
        this.exhaust = true
        ActionUtility.addToBot_makeTempCardInBattle(SelfReference(), ActionUtility.BattleCardPlace.Hand, upgraded)
    }

    private fun ForMonsterBrokenSpaceStructure(monster: AbstractMonster)
    {
        val sealPower = doSealPowers(monster.powers)
        addToTop_HangUpOrb(sealPower, monster)
        this.exhaust = true
        ActionUtility.addToBot_makeTempCardInBattle(SelfReference(), ActionUtility.BattleCardPlace.Hand, upgraded)
    }

    private fun ForMonster(monster: AbstractMonster)
    {
        val sealPower = doSealPowers(monster.powers)
        addToTop_HangUpOrb(sealPower, monster)
        ActionUtility.addToBot_makeTempCardInBattle(GiveBirth(), ActionUtility.BattleCardPlace.Discard, upgraded)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val target = CardUtility.getSelfOrEnemyTarget(this, monster)
        if (target is AbstractPlayer)
            ForPlayer(AbstractDungeon.player)
        else if (HangUpCardGroup.forEachHangUpCard_Any { (it as? CardOrb_SealPower)?.sealCreature === target })
            ForMonsterBrokenSpaceStructure(target as AbstractMonster)
        else
            ForMonster(target as AbstractMonster)
    }

    override fun upgradeAuto()
    {
        upgradeCardsToPreview()
    }

    private fun addToTop_HangUpOrb(sealPower: List<AbstractPower>, sealCreature: AbstractCreature)
    {
        val powerNames =
            sealPower.joinToString(cardStrings.getEXTENDED_DESCRIPTION(2)) { it.amount.toString() + it.name }
        val copyCard = this.makeStatEquivalentCopy() as UnBirth
        copyCard.exhaust = true
        copyCard.purgeOnUse = true

        if (sealCreature is AbstractPlayer)
            copyCard.addedToolTipsTop.add(
                TooltipInfo(
                    cardStrings.getEXTENDED_DESCRIPTION(3),
                    cardStrings.getEXTENDED_DESCRIPTION(4)
                )
            )
        else
            copyCard.addedToolTipsTop.add(TooltipInfo(cardStrings.getEXTENDED_DESCRIPTION(3), sealCreature.name))

        if (powerNames.isNotEmpty())
            copyCard.addedToolTipsTop.add(TooltipInfo(cardStrings.getEXTENDED_DESCRIPTION(1), powerNames))


        CardOrb_SealPower(copyCard, null, CostSmart(this.block), sealPower, sealCreature)
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(0))
            .addToBot_HangCard()
    }

    class CardOrb_SealPower(
        card: AbstractCard,
        cardGroupReturnAfterEvoke: CardGroup?,
        OrbCounter: CostSmart,
        private val sealPower: List<AbstractPower>, val sealCreature: AbstractCreature?
    ) :
        CardOrb_BlockDamage(card, cardGroupReturnAfterEvoke, OrbCounter, actionOnDamagedRemove = {
            if (sealCreature != null && !sealCreature.isDeadOrEscaped)
                for (power in sealPower)
                {
                    ActionUtility.addToBot(ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, power))
                }
        }), GiveBirth.IPregnantCardOrb
    {
        override fun makeCopy(): AbstractOrb
        {
            return CardOrb_SealPower(originCard, cardGroupReturnAfterEvoke, orbCounter, sealPower, sealCreature)
        }
    }


    internal sealed class MonsterBodyType
    {
        data object Tiny : MonsterBodyType()
        data object Small : MonsterBodyType()
        data object Middle : MonsterBodyType()
        data object Big : MonsterBodyType()
        data object VeryBig : MonsterBodyType()
        companion object
        {
            fun values(): Array<MonsterBodyType>
            {
                return arrayOf(Tiny, Small, Middle, Big, VeryBig)
            }

            fun valueOf(value: String): MonsterBodyType
            {
                return when (value)
                {
                    "Tiny"    -> Tiny
                    "Small"   -> Small
                    "Middle"  -> Middle
                    "Big"     -> Big
                    "VeryBig" -> VeryBig
                    else      -> throw IllegalArgumentException("No object superstitio.cards.maso.SkillCard.UnBirth.MonsterBodyType.$value")
                }
            }
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(UnBirth::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = SelfOrEnemyTargeting.SELF_OR_ENEMY

        private const val COST = 1
        private const val BLOCK = 8
        private const val UPGRADE_BLOCK = 3

        private fun doSealPowers(monster: MutableList<AbstractPower>): MutableList<AbstractPower>
        {
            val sealPower = ArrayList<AbstractPower>()
            monster.forEach { power: AbstractPower ->
                if (power is WeakPower || power is VulnerablePower || power is FrailPower || power is ArtifactPower)
                {
                    if (power is InvisiblePower) return@forEach
                    power.owner = AbstractDungeon.player
                    power.amount *= 2
                    sealPower.add(power)
                    AutoDoneInstantAction.addToBotAbstract { monster.remove(power) }
                }
            }
            return sealPower
        }
    }
}
