package superstitio.cards.general.TempCard

import basemod.AutoAdd
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.NormalCard
import superstitioapi.cards.DamageActionMaker
import superstitioapi.utils.CreatureUtility

@AutoAdd.Ignore
class NullCard : NormalCard
{
    private val number: Int

    constructor() : super(ID, DEFAULT_TYPE, COST, DEFAULT_RARITY, CARD_TARGET)
    {
        this.number = 0
    }

    constructor(cardType: CardType, cardRarity: CardRarity, Number: Int) : super(
        ID,
        cardType,
        COST,
        cardRarity,
        CARD_TARGET
    )
    {
        this.cardID = ID + cardType + cardRarity + Number
        this.number = Number
    }

    override fun makeCopy(): AbstractCard
    {
        return NullCard(this.type, this.rarity, this.number)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        for (abstractMonster in CreatureUtility.getAllAliveMonsters())
        {
            DamageActionMaker.maker(114514, abstractMonster).addToBot()
        }
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(NullCard::class.java)

        val DEFAULT_TYPE: CardType = CardType.SKILL

        val DEFAULT_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 0
        fun makeNullCardPool(): List<AbstractCard>
        {
            val cards: MutableList<AbstractCard> = ArrayList()
            for (i in 0..7)
            {
                cards.add(NullCard(CardType.ATTACK, CardRarity.COMMON, i))
                cards.add(NullCard(CardType.ATTACK, CardRarity.UNCOMMON, i))
                cards.add(NullCard(CardType.ATTACK, CardRarity.RARE, i))
                cards.add(NullCard(CardType.SKILL, CardRarity.COMMON, i))
                cards.add(NullCard(CardType.SKILL, CardRarity.UNCOMMON, i))
                cards.add(NullCard(CardType.SKILL, CardRarity.RARE, i))
                cards.add(NullCard(CardType.POWER, CardRarity.COMMON, i))
                cards.add(NullCard(CardType.POWER, CardRarity.UNCOMMON, i))
                cards.add(NullCard(CardType.POWER, CardRarity.RARE, i))
            }
            return cards
        }

        fun needMakeNullCardToFill(cardList: List<AbstractCard>?): Boolean
        {
            if (cardList == null || cardList.size < 20)
                return true
            // 定义需要检查的卡片类型和稀有度组合
            val requiredCards = setOf(
                Pair(CardType.ATTACK, CardRarity.COMMON),
                Pair(CardType.ATTACK, CardRarity.UNCOMMON),
                Pair(CardType.ATTACK, CardRarity.RARE),
                Pair(CardType.SKILL, CardRarity.COMMON),
                Pair(CardType.SKILL, CardRarity.UNCOMMON),
                Pair(CardType.SKILL, CardRarity.RARE),
                Pair(CardType.POWER, CardRarity.UNCOMMON),
                Pair(CardType.POWER, CardRarity.RARE)
            )

            // 检查是否存在所有所需的卡片类型和稀有度组合
            return requiredCards.any { (type, rarity) -> cardList.none { it.type == type && it.rarity == rarity } }
        }
    }
}
