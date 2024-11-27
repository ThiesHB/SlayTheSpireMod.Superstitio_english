package superstitioapi.utils

import basemod.BaseMod
import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget
import com.megacrit.cardcrawl.cards.AbstractCard.CardType
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.CardLibrary
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.ui.panels.EnergyPanel
import superstitioapi.DataUtility
import java.util.stream.Collectors

object CardUtility
{

    open class CostSmart
    {
        constructor(cost: Int)
        {
            this.costType = when (cost)
            {
                -2   -> CostType.NaN
                -1   -> CostType.XCost
                else -> CostType.Int
            }
            this._cost = cost
        }

        constructor(costType: CostType)
        {
            this.costType = costType
            this._cost = when (costType)
            {
                CostType.XCost -> -1
                CostType.NaN   -> -2
                else           -> throw IllegalArgumentException("Unsupported cost type: $costType")
            }
        }

        sealed class CostType
        {
            data object NaN : CostType()
            data object Int : CostType()
            data object XCost : CostType()
            companion object
            {
                fun values(): Array<CostType>
                {
                    return arrayOf(NaN, Int, XCost)
                }

                fun valueOf(value: String): CostType
                {
                    return when (value)
                    {
                        "NaN"   -> NaN
                        "Int"   -> Int
                        "XCost" -> XCost
                        else    -> throw IllegalArgumentException("No object superstitioapi.utils.CardUtility.CostSmart.cost.$value")
                    }
                }
            }
        }

        var costType: CostType
        var _cost: Int
        open var cost: Int
            get() = _cost
            set(value)
            {
                _cost = when (costType)
                {
                    CostType.Int   -> value
                    CostType.XCost -> -1
                    CostType.NaN   -> -2
                }
            }


//        open fun onCostChange() {
//
//        }

        fun isInt(): Boolean
        {
            return costType == CostType.Int
        }

        fun toInt(): Int?
        {
            return when (costType)
            {
                CostType.Int -> cost
                else         -> null
            }
        }

        fun toInt(transform: (Int) -> Int): Int = transform(this.cost)

        override fun toString(): String
        {
            return cost.toString()
        }

        operator fun compareTo(int: Int): Int
        {
            return cost.compareTo(int)
        }

        operator fun dec(): CostSmart
        {
            if (this.costType == CostType.Int)
                this.cost--
            return this
        }

        override fun equals(other: Any?): Boolean
        {
            if (other == null) return false
            if (other is Int) return this.cost == other
            if (other is CostType) return this.costType == other
            if (other is CostSmart) return this === other
            return false
        }

        override fun hashCode(): Int
        {
            var result = costType.hashCode()
            result = 31 * result + _cost
            return result
        }

        operator fun inc(): CardUtility.CostSmart
        {
            if (this.costType == CostType.Int)
                this.cost++
            return this
        }

        operator fun timesAssign(magicNumber: Int)
        {
            if (this.costType == CostType.Int)
                this.cost *= magicNumber
        }
    }

    internal val AgressiveColor: Color = Color.RED.cpy()
    internal val HospitableColor: Color = Color.GREEN.cpy()

    /**
     * @param ThisMod 当为true时， IsCardColorVanilla()被短路
     * @param Vanilla 当为true时， IsCardColorThisMod()被短路
     * @return 输入为true ture时，输出为满足两个条件之一的结果。输入为1true1false时，输出由true项决定。输入为false false时，输出两者都不满足的结果。
     */
    fun getRandomStatusCard(ThisMod: Boolean, Vanilla: Boolean): AbstractCard
    {
        val list = getCardsListForMod(ThisMod, Vanilla).stream()
            .filter { card: AbstractCard -> card.type == CardType.STATUS }
            .collect(Collectors.toList())
        return ListUtility.getRandomFromList(list, AbstractDungeon.cardRandomRng).makeCopy()
    }

    fun getSelfOrEnemyTarget(card: AbstractCard, monster: AbstractMonster?): AbstractCreature
    {
        if (card.target != SelfOrEnemyTargeting.SELF_OR_ENEMY)
        {
            return CreatureUtility.getRandomMonsterSafe()
        }
        val target = SelfOrEnemyTargeting.getTarget(card)
        if (target != null) return target
        if (monster != null) return monster
        return AbstractDungeon.player
    }

    fun getCardsListForMod(ThisMod: Boolean, Vanilla: Boolean): List<AbstractCard>
    {
        val list = CardLibrary.cards.values.stream()
            .filter { card: AbstractCard ->
                if (ThisMod && Vanilla) return@filter IsCardColorVanilla(card) || IsCardColorThisMod(card)
                else if (ThisMod) return@filter !IsCardColorVanilla(card) && IsCardColorThisMod(card)
                else if (Vanilla) return@filter IsCardColorVanilla(card) && !IsCardColorThisMod(card)
                else return@filter !IsCardColorVanilla(card) && !IsCardColorThisMod(card)
            }
            .collect(Collectors.toList())
        return list
    }

    fun IsCardColorVanilla(card: AbstractCard): Boolean
    {
        return card.javaClass.getPackage().name.contains("com.megacrit.cardcrawl")
        //        return !card.cardID.contains(":");
    }

    fun IsCardColorThisMod(card: AbstractCard): Boolean
    {
        return !card.cardID.contains(DataUtility.MakeTextID(""))
    }

    fun AllCardInBattle(): MutableList<AbstractCard>
    {
        val cards = ArrayList<AbstractCard>()
        cards.add(AbstractDungeon.player.cardInUse)
        cards.addAll(AbstractDungeon.player.hand.group)
        cards.addAll(AbstractDungeon.player.discardPile.group)
        cards.addAll(AbstractDungeon.player.drawPile.group)
        return cards
    }

    fun AllCardGroupInBattle(): Array<CardGroup>
    {
        return arrayOf(
            AbstractDungeon.player.hand,
            AbstractDungeon.player.drawPile,
            AbstractDungeon.player.discardPile,
            AbstractDungeon.player.exhaustPile
        )
    }

    fun AllCardInBattle_ButWithoutCardInUse(): MutableList<AbstractCard>
    {
        val cards = ArrayList<AbstractCard>()
        cards.addAll(AbstractDungeon.player.hand.group)
        cards.addAll(AbstractDungeon.player.discardPile.group)
        cards.addAll(AbstractDungeon.player.drawPile.group)
        return cards
    }

    //    public static AbstractCard makeStatEquivalentCopy(final AbstractCard c) {
    //        final AbstractCard card = c.makeStatEquivalentCopy();
    //        card.retain = c.retain;
    //        card.selfRetain = c.selfRetain;
    //        card.purgeOnUse = c.purgeOnUse;
    //        card.isEthereal = c.isEthereal;
    //        card.exhaust = c.exhaust;
    //        card.glowColor = c.glowColor;
    //        card.rawDescription = c.rawDescription;
    //        card.cardsToPreview = c.cardsToPreview;
    //        card.initializeDescription();
    //        return card;
    //    }
    fun flashIfInHand(card: AbstractCard)
    {
        if (AbstractDungeon.player.hand.contains(card)) card.flash()
    }

    fun getColorFormCard(card: AbstractCard): Color
    {
        return when (card.target)
        {
            CardTarget.ENEMY, CardTarget.ALL_ENEMY                     -> AgressiveColor.cpy()
            CardTarget.SELF                                            -> HospitableColor.cpy()
            CardTarget.NONE, CardTarget.ALL, CardTarget.SELF_AND_ENEMY -> Color.PURPLE.cpy()
            else                                                       -> Color.PURPLE.cpy()
        }
    }

    fun getColorFormCard(target: CardTarget?): Color
    {
        return when (target)
        {
            CardTarget.ENEMY, CardTarget.ALL_ENEMY                     -> AgressiveColor.cpy()
            CardTarget.SELF                                            -> HospitableColor.cpy()
            CardTarget.NONE, CardTarget.ALL, CardTarget.SELF_AND_ENEMY -> Color.PURPLE.cpy()
            else                                                       -> Color.PURPLE.cpy()
        }
    }

    fun canUseWithoutEnvironment(card: AbstractCard): Boolean
    {
        if (card.canUse(AbstractDungeon.player, null)) return true
        //不是因为能量不够或者对象不对而无法打出
//            if (!(card.cardPlayable(null) && hasEnoughEnergyOrTurnEnd(card))) return;
        //似乎检测对象是否正确会导致攻击牌出问题，所以只加了这个检测，但是可能会引发其他错误
        return !(hasEnoughEnergyOrTurnEnd(card))
    }

    private fun hasEnoughEnergyOrTurnEnd(card: AbstractCard): Boolean
    {
        if (AbstractDungeon.actionManager.turnHasEnded)
        {
            return false
        }
        return EnergyPanel.totalCount >= card.costForTurn || card.freeToPlay() || card.isInAutoplay
    }

    fun moveToHandOrDiscardWhenMaxHand(cardHolder: CardGroup, originCard: AbstractCard?)
    {
        if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE)
        {
            AbstractDungeon.player.createHandIsFullDialog()
            cardHolder.moveToDiscardPile(originCard)
        }
        else cardHolder.moveToHand(originCard)
    }
}
