package superstitio.cards.general.TempCard;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitioapi.cards.DamageActionMaker;
import superstitioapi.utils.CreatureUtility;

import java.util.ArrayList;
import java.util.List;

@AutoAdd.Ignore
public class NullCard extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(NullCard.class);

    public static final CardType DEFAULT_TYPE = CardType.SKILL;

    public static final CardRarity DEFAULT_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private final int number;

    public NullCard() {
        super(ID, DEFAULT_TYPE, COST, DEFAULT_RARITY, CARD_TARGET);
        this.number = 0;
    }
//
//    public NullCard(CardType cardType, CardRarity cardRarity) {
//        super(ID, cardType, COST, cardRarity, CARD_TARGET);
//        this.cardID = ID + cardType + cardRarity;
//        this.number = 0;
//    }

    public NullCard(CardType cardType, CardRarity cardRarity, int Number) {
        super(ID, cardType, COST, cardRarity, CARD_TARGET);
        this.cardID = ID + cardType + cardRarity + Number;
        this.number = Number;
    }

    public static List<AbstractCard> makeNullCardPool() {
        List<AbstractCard> cards = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            cards.add(new NullCard(CardType.ATTACK, CardRarity.COMMON, i));
            cards.add(new NullCard(CardType.ATTACK, CardRarity.UNCOMMON, i));
            cards.add(new NullCard(CardType.ATTACK, CardRarity.RARE, i));
            cards.add(new NullCard(CardType.SKILL, CardRarity.COMMON, i));
            cards.add(new NullCard(CardType.SKILL, CardRarity.UNCOMMON, i));
            cards.add(new NullCard(CardType.SKILL, CardRarity.RARE, i));
            cards.add(new NullCard(CardType.POWER, CardRarity.COMMON, i));
            cards.add(new NullCard(CardType.POWER, CardRarity.UNCOMMON, i));
            cards.add(new NullCard(CardType.POWER, CardRarity.RARE, i));
        }
        return cards;
    }

    public static boolean needMakeNullCardToFill(List<AbstractCard> cardList) {
        if (cardList == null || cardList.size() < 20)
            return true;
        if (cardList.stream().noneMatch(card -> card.type == CardType.ATTACK && card.rarity == CardRarity.COMMON))
            return true;
        if (cardList.stream().noneMatch(card -> card.type == CardType.ATTACK && card.rarity == CardRarity.UNCOMMON))
            return true;
        if (cardList.stream().noneMatch(card -> card.type == CardType.ATTACK && card.rarity == CardRarity.RARE))
            return true;
        if (cardList.stream().noneMatch(card -> card.type == CardType.SKILL && card.rarity == CardRarity.COMMON))
            return true;
        if (cardList.stream().noneMatch(card -> card.type == CardType.SKILL && card.rarity == CardRarity.UNCOMMON))
            return true;
        if (cardList.stream().noneMatch(card -> card.type == CardType.SKILL && card.rarity == CardRarity.RARE))
            return true;
        if (cardList.stream().noneMatch(card -> card.type == CardType.POWER && card.rarity == CardRarity.COMMON))
            return true;
        if (cardList.stream().noneMatch(card -> card.type == CardType.POWER && card.rarity == CardRarity.UNCOMMON))
            return true;
        if (cardList.stream().noneMatch(card -> card.type == CardType.POWER && card.rarity == CardRarity.RARE))
            return true;
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new NullCard(this.type, this.rarity, this.number);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        for (AbstractMonster abstractMonster : CreatureUtility.getAllAliveMonsters()) {
            DamageActionMaker.maker(114514, abstractMonster).addToBot();
        }
    }

    @Override
    public void upgradeAuto() {
    }
}
