package superstitio.cards.general.TempCard

import basemod.AutoAdd
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitioapi.cards.DamageActionMaker
import superstitioapi.utils.CreatureUtility

@AutoAdd.Ignore
class NullCard : GeneralCard {
    private val number: Int

    constructor() : super(ID, DEFAULT_TYPE, COST, DEFAULT_RARITY, CARD_TARGET) {
        this.number = 0
    }

    //
    //    public NullCard(CardType cardType, CardRarity cardRarity) {
    //        super(ID, cardType, COST, cardRarity, CARD_TARGET);
    //        this.cardID = ID + cardType + cardRarity;
    //        this.number = 0;
    //    }
    constructor(cardType: CardType, cardRarity: CardRarity, Number: Int) : super(
        ID,
        cardType,
        COST,
        cardRarity,
        CARD_TARGET
    ) {
        this.cardID = ID + cardType + cardRarity + Number
        this.number = Number
    }

    override fun makeCopy(): AbstractCard {
        return NullCard(this.type, this.rarity, this.number)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        for (abstractMonster in CreatureUtility.getAllAliveMonsters()) {
            DamageActionMaker.maker(114514, abstractMonster).addToBot()
        }
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(NullCard::class.java)

        val DEFAULT_TYPE: CardType = CardType.SKILL

        val DEFAULT_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 0
        fun makeNullCardPool(): List<AbstractCard> {
            val cards: MutableList<AbstractCard> = ArrayList()
            for (i in 0..7) {
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

        fun needMakeNullCardToFill(cardList: List<AbstractCard>?): Boolean {
            if (cardList == null || cardList.size < 20) return true
            if (cardList.stream()
                    .noneMatch { card: AbstractCard -> card.type == CardType.ATTACK && card.rarity == CardRarity.COMMON }
            ) return true
            if (cardList.stream()
                    .noneMatch { card: AbstractCard -> card.type == CardType.ATTACK && card.rarity == CardRarity.UNCOMMON }
            ) return true
            if (cardList.stream()
                    .noneMatch { card: AbstractCard -> card.type == CardType.ATTACK && card.rarity == CardRarity.RARE }
            ) return true
            if (cardList.stream()
                    .noneMatch { card: AbstractCard -> card.type == CardType.SKILL && card.rarity == CardRarity.COMMON }
            ) return true
            if (cardList.stream()
                    .noneMatch { card: AbstractCard -> card.type == CardType.SKILL && card.rarity == CardRarity.UNCOMMON }
            ) return true
            if (cardList.stream()
                    .noneMatch { card: AbstractCard -> card.type == CardType.SKILL && card.rarity == CardRarity.RARE }
            ) return true
            //        if (cardList.stream().noneMatch(card -> card.type == CardType.POWER && card.rarity == CardRarity.COMMON))
//            return true;
            if (cardList.stream()
                    .noneMatch { card: AbstractCard -> card.type == CardType.POWER && card.rarity == CardRarity.UNCOMMON }
            ) return true
            if (cardList.stream()
                    .noneMatch { card: AbstractCard -> card.type == CardType.POWER && card.rarity == CardRarity.RARE }
            ) return true
            return false
        }
    }
}
